package com.goencom.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.goencom.dao.AuctionRepository;
import com.goencom.dao.BidRepository;
import com.goencom.dao.InterestRepository;
import com.goencom.dao.ItemRepository;
import com.goencom.dao.NotificationRepository;
import com.goencom.dao.UserRepository;
import com.goencom.entities.Auction;
import com.goencom.entities.Bid;
import com.goencom.entities.Interest;
import com.goencom.entities.Item;
import com.goencom.entities.Notification;
import com.goencom.entities.User;
import com.goencom.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private BidRepository bidRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private InterestRepository interestRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private NotificationRepository notificationRepository;
	
	@RequestMapping(path = "/")
	public String profile(Model model, Principal principal) {
		User user = userRepository.getUserByEmail(principal.getName());
		model.addAttribute("user", user);
		List<Notification> notifications = notificationRepository.findNotificationsUserId(user.getUserId());
		Collections.reverse(notifications);
		model.addAttribute("notifications",notifications);
		return "user-profile";
	}

	@PostMapping("/bid/{auctionId}/")
	public String bid(@PathVariable("auctionId") Integer auctionId, @RequestParam("amount") Integer amount,
			Principal principal, HttpSession session) {
		try {
			User user = userRepository.getUserByEmail(principal.getName());
			Auction auction = auctionRepository.findById(auctionId).get();
			if(amount < auction.getItem().getBasePrice()) {
				session.setAttribute("message", new Message("Entered Amount Less than Base Price!!!", "alert-danger"));
				return "redirect:/product/" + auctionId;
			}
			Bid bid = bidRepository.findBidbyEmailAndAuctionId(principal.getName(), auctionId);
			if (bid == null) {
				bid = new Bid();
				bid.setAuction(auction);
				bid.setUser(user);
				bid.setAmount(amount);
				bid.setStatus(Bid.PENDING_STATUS);
				bidRepository.save(bid);
				session.setAttribute("message", new Message("successfully registered!!!", "alert-success"));
			} else {
				session.setAttribute("message", new Message("Already Bidded!!!", "alert-warning"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("oops something went wrong!!!", "alert-danger"));
		}

		return "redirect:/product/" + auctionId;
	}

	@GetMapping("/bidded-items/{page}")
	public String biddedItems(@PathVariable("page") Integer page, Model model, Principal principal) {
		Pageable pageable = PageRequest.of(page, 4);
		Page<Bid> bids = bidRepository.findActiveBidsByUser(principal.getName(), pageable);
		model.addAttribute("bids", bids);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", bids.getTotalPages());
		return "user-bidded-items-list";
	}

	@GetMapping("/bid-history/{page}")
	public String bidHistory(@PathVariable("page") Integer page, Model model, Principal principal) {
		Pageable pageable = PageRequest.of(page, 4);
		Page<Bid> bids = bidRepository.findInactiveBidsByUser(principal.getName(), pageable);
		model.addAttribute("bids", bids);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", bids.getTotalPages());
		return "user-bidding-history";
	}

	@PostMapping("/add-interested/{itemId}/")
	public String addInterested(@PathVariable("itemId") Integer itemId, Model model, Principal principal,
			HttpSession session) {
		try {
			Interest interest = interestRepository.findInterestbyEmailAndItemId(principal.getName(), itemId);
			if (interest == null) {
				User user = userRepository.getUserByEmail(principal.getName());
				Item item = itemRepository.findById(itemId).get();
				interest = new Interest();
				user.getInterests().add(interest);
				interest.setItem(item);
				interest.setUser(user);
				interestRepository.save(interest);
				session.setAttribute("message", new Message("successfully registered!!!", "alert-success"));
			} else {
				session.setAttribute("message", new Message("Already Bidded!!!", "alert-warning"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("oops something went wrong!!!", "alert-danger"));
		}
		return "redirect:/upcomming/" + itemId + "/";
	}

	@GetMapping("/interest-list/{page}")
	public String interestList(@PathVariable("page") Integer page, Model model, Principal principal) {
		Pageable pageable = PageRequest.of(page, 4);
		Page<Interest> interests = interestRepository.findInterestbyEmail(principal.getName(), pageable);
		Page<Interest> activeInterests = interestRepository.findActiveInterestbyEmail(principal.getName(), pageable);
		model.addAttribute("interests", interests);
		model.addAttribute("active", activeInterests);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", interests.getTotalPages());
		return "user-interest-list";
	}

	@GetMapping("/fetch-product/{itemId}")
	public String fetchProduct(@PathVariable("itemId") Integer itemId) {
		Auction auction = auctionRepository.findAuctionByItemId(itemId);
		return "redirect:/product/" + auction.getAuctionId();
	}

	@GetMapping("/edit-user")
	public String editUser(Model model, Principal principal) {
		User user = userRepository.getUserByEmail(principal.getName());
		model.addAttribute("user", user);
		return "edit-user";
	}

	@PostMapping("/update-user")
	public String updateUser(@ModelAttribute("user") User user, HttpSession session, Model model) {
		try {
			this.userRepository.save(user);
			session.setAttribute("message", new Message("successfully updated your details!", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("something went wrong!", "alert-danger"));
			return "edit-user";
		}
		return "redirect:profile";
	}

	@GetMapping("/new-password")
	public String newPassword() {
		return "change-password";
	}

	@PostMapping("/change-password")
	public String changePassword(Principal principal, @RequestParam("old-password") String oldPassword,
			@RequestParam("new-password") String newPassword, @RequestParam("new-password2") String newPassword2,
			HttpSession session) {
		try {
			User user = userRepository.getUserByEmail(principal.getName());
			if (this.passwordEncoder.matches(oldPassword, user.getPassword())) {
				if (newPassword.equals(newPassword2)) {
					user.setPassword(passwordEncoder.encode(newPassword));
					userRepository.save(user);
				} else {
					session.setAttribute("message", new Message("password don't match!", "alert-danger"));
					return "redirect:new-password";
				}

			} else {
				session.setAttribute("message", new Message("incorrect password!", "alert-danger"));
				return "redirect:new-password";
			}
			session.setAttribute("message", new Message("successfully updated your password!", "alert-success"));
		} catch (Exception e) {
			session.setAttribute("message", new Message("something went wrong!", "alert-danger"));
			return "redirect:new-password";
		}
		return "redirect:edit-user";
	}
}
