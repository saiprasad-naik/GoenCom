package com.goencom.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.goencom.dao.AuctionRepository;
import com.goencom.dao.BidRepository;
import com.goencom.dao.InterestRepository;
import com.goencom.dao.ItemRepository;
import com.goencom.dao.NotificationRepository;
import com.goencom.dao.ResultRepository;
import com.goencom.dao.UserRepository;
import com.goencom.entities.Auction;
import com.goencom.entities.Bid;
import com.goencom.entities.Image;
import com.goencom.entities.Interest;
import com.goencom.entities.Item;
import com.goencom.entities.Notification;
import com.goencom.entities.Result;
import com.goencom.entities.User;
import com.goencom.helper.*;
import com.goencom.service.EmailService;

@Controller
@RequestMapping("/auction-house")
public class AuctionHouseController {
	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuctionRepository auctionRepository;

	/*
	 * @Autowired private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	 */
	@Autowired
	private BidRepository bidRepository;

	@Autowired
	private ResultRepository resultRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private InterestRepository interestRepository;

	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/*
	 * @Autowired private AuctionResultTask auctionResultTask;
	 */
	@RequestMapping("/")
	public String home(Model model, Principal principal) {
		User user = userRepository.getUserByEmail(principal.getName());
		model.addAttribute("user", user);
		return "auction-house";
	}

	@RequestMapping("/manage-items/{page}")
	public String manageItem(@PathVariable("page") Integer page, Model model, Principal principal) {
		User user = userRepository.getUserByEmail(principal.getName());
		Pageable pageable = PageRequest.of(page, 4);
		Page<Item> items = itemRepository.findItemsByUser(user.getUserId(), pageable);
		model.addAttribute("items", items);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", items.getTotalPages());
		return "manage-auction-items";
	}

	@RequestMapping("/new-item")
	public String newItem(Model model) {
		model.addAttribute("item", new Item());
		return "add-item";
	}

	@RequestMapping(path = "/add-item", method = RequestMethod.POST)
	public String addItem(@ModelAttribute("item") Item item, @RequestParam("image") MultipartFile file,
			@RequestParam(value = "visible", required = false) String visible, Principal principal, HttpSession session,
			Model model) {
		try {
			User user = userRepository.getUserByEmail(principal.getName());
			if (file.isEmpty()) {
				Image image = new Image();
				image.setUrl("—Pngtree—a shoe store sells shoes_7256461.png");
				image.setItem(item);
				item.getImages().add(image);
			} else {
				Image image = new Image();
				image.setUrl(file.getOriginalFilename());
				image.setItem(item);
				item.getImages().add(image);
				File saveFile = new ClassPathResource("static/images").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			item.setEnabled(true);
			item.setAuctioned(false);
			item.setDeleted(false);
			if (visible != null) {
				item.setVisible(true);
			} else {
				item.setVisible(false);
			}
			item.setUser(user);
			user.getItems().add(item);
			userRepository.save(user);
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("successfully added item!", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("item", item);
			session.setAttribute("message", new Message("oops something went wrong!!!", "alert-danger"));
			return "new-item";
		}
		return "redirect:new-item";
	}

	@RequestMapping("/edit-details")
	public String editDetails(Model model, Principal principal) {
		User user = this.userRepository.getUserByEmail(principal.getName());
		model.addAttribute("user", user);
		return "edit-auction-house";
	}

	@PostMapping("/update-details")
	public String updateDetails(@ModelAttribute("user") User user, HttpSession session, Model model) {
		try {
			this.userRepository.save(user);
			session.setAttribute("message", new Message("successfully updated your details!", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("something went wrong!", "alert-danger"));
			return "edit-auction-house";
		}
		return "redirect:/auction-house/";
	}

	@GetMapping("/manage-auction/{page}")
	public String manageAuction(@PathVariable("page") Integer page, Model model, Principal principal) {
		User user = userRepository.getUserByEmail(principal.getName());
		Pageable pageable = PageRequest.of(page, 4);
		Page<Auction> auctions = auctionRepository.findActiveAuctionByUser(user.getUserId(), pageable);
		model.addAttribute("auctions", auctions);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", auctions.getTotalPages());
		return "manage-auction";
	}

	@GetMapping("/new-auction/{page}")
	public String newAuction(@PathVariable("page") Integer page, Model model, Principal principal) {
		User user = userRepository.getUserByEmail(principal.getName());
		Pageable pageable = PageRequest.of(page, 4);
		Page<Item> items = itemRepository.findItemsByUserForAuction(user.getUserId(), pageable);
		model.addAttribute("items", items);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", items.getTotalPages());
		return "item-list-auction-house";
	}

	@GetMapping("/{itemId}/addToAuction")
	public String addToAuction(@PathVariable("itemId") Integer itemId, Principal principal) {
		try {
			Optional<Item> optionItem = this.itemRepository.findById(itemId);
			User user = this.userRepository.getUserByEmail(principal.getName());
			Item item = optionItem.get();
			Auction auction = new Auction();
			item.setAuctioned(true);
			item.setVisible(false);
			item.setAuction(auction);
			auction.setItem(item);
			auction.setEnable(true);
			user.getAuctions().add(auction);
			auction.setUser(user);
			auction.setStatus(Auction.RUNNING);
			auctionRepository.save(auction);
			List<Interest> interests = interestRepository.findInterestbyItemId(itemId);
			if (interests != null) {
				List<Notification> notifications = new ArrayList<Notification>();
				for (int i = 0; i < interests.size(); i++) {
					Notification temp = new Notification();
					temp.setUser(interests.get(i).getUser());
					temp.setMessage(item.getName() + " is up for the auction please go and check out!!!");
					notifications.add(temp);
				}
				notificationRepository.saveAll(notifications);
			}
			/*
			 * auction = auctionRepository.findAuctionByItemId(itemId);
			 * auctionResultTask.setAuctionId(auction.getAuctionId());
			 * threadPoolTaskScheduler.schedule(auctionResultTask, new
			 * Date(System.currentTimeMillis() + 60000));
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/auction-house/new-auction/0";
	}

	@GetMapping("/generate/{auctionId}")
	public String generateResults(@PathVariable("auctionId") Integer auctionId) {
		List<Bid> bids = bidRepository.findBidsbyAuctionId(auctionId);
		if (bids.size() <= 0) {
			Auction auction = auctionRepository.findById(auctionId).get();
			auction.getItem().setEnabled(false);
			auction.setStatus(Auction.FINNISHED);
			Bid bid = new Bid();
			bid.setAuction(auction);
			Result result = new Result();
			result.setBid(bid);
			resultRepository.save(result);
			return "redirect:/auction-house/manage-auction/0";
		} else {
			List<Bid> candidates = highestBids(bids);
			Bid bid = candidates.get(0);
			if (candidates.size() != 1) {
				bid = lowestBidId(candidates);
			}
			System.out.println(bid.getBidId());
			bid.getAuction().getItem().setEnabled(false);
			bid.getAuction().setStatus(Auction.FINNISHED);
			bid.setStatus(Bid.WON_STATUS);
			Result result = new Result();
			bid.setResult(result);
			result.setBid(bid);
			resultRepository.save(result);
			emailService.sendEmail("Congratulations From Team GoenCom.", emailService.bidWonMessage(),
					bid.getUser().getEmail());
			if (bids.size() != 1) {
				bids = exceptHighestBid(bids, bid);
				bidRepository.saveAll(bids);
				List<Notification> notifications = new ArrayList<Notification>();
				for (int i = 0; i < bids.size(); i++) {
					Notification temp = new Notification();
					temp.setUser(bids.get(i).getUser());
					temp.setMessage("Sorry, you lost bid for " + bids.get(i).getAuction().getItem().getName());
					notifications.add(temp);
				}
				notificationRepository.saveAll(notifications);
			}
		}
		return "redirect:/auction-house/manage-auction/0";
	}

	private List<Bid> highestBids(List<Bid> bids) {
		int max = 0;
		for (int i = 0; i < bids.size(); i++) {
			System.out.println(bids.get(i).getBidId());
			if (bids.get(i).getAmount() > max) {
				max = bids.get(i).getAmount();
			}
		}
		List<Bid> results = new ArrayList<>();
		for (int i = 0; i < bids.size(); i++) {
			if (bids.get(i).getAmount() == max) {
				results.add(bids.get(i));
			}
		}
		return results;
	}

	private Bid lowestBidId(List<Bid> bids) {
		Bid min = bids.get(0);

		for (int i = 0; i < bids.size(); i++) {
			if (bids.get(i).getBidId() < min.getBidId()) {
				min = bids.get(i);
			}
		}

		return min;
	}

	private List<Bid> exceptHighestBid(List<Bid> bids, Bid bid) {
		int id = 0;
		for (int i = 0; i < bids.size(); i++) {
			if (bid.getBidId() == bids.get(i).getBidId()) {
				id = i;
				continue;
			}
			bids.get(i).setStatus(Bid.LOST_STATUS);
		}
		bids.remove(id);
		return bids;
	}

	@GetMapping("auction-results/{page}")
	public String manageResults(@PathVariable("page") Integer page, Model model, Principal principal) {
		Pageable pageable = PageRequest.of(page, 4);
		Page<Result> results = resultRepository.findAuctionResultsByAuctionHouse(principal.getName(), pageable);
		model.addAttribute("results", results);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", results.getTotalPages());
		return "auction-results";
	}

	@GetMapping("edit-item/{itemId}")
	public String editItem(@PathVariable("itemId") Integer itemId, Model model, Principal principal) {
		User user = userRepository.getUserByEmail(principal.getName());
		Item item = itemRepository.findById(itemId).get();
		if (user.getUserId() != item.getUser().getUserId()) {
			return "redirect:/auction-house/manage-items/0";
		}
		model.addAttribute("item", item);
		return "edit-item";
	}

	@PostMapping("update-item")
	public String updateItem(@RequestParam("itemId") Integer itemId, @RequestParam("name") String name,
			@RequestParam("description") String description, @RequestParam("image") MultipartFile file,
			HttpSession session, Model model) {
		Item item = itemRepository.findById(itemId).get();
		try {
			item.setName(name);
			item.setDescription(description);
			itemRepository.save(item);
			session.setAttribute("message", new Message("successfully updated your details!", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong!!!", "alert-danger"));
			model.addAttribute("item", item);
			return "redirect:edit-item/" + itemId;
		}
		return "redirect:manage-items/0";
	}

	@GetMapping("delete-item/{itemId}")
	public String deleteItem(@PathVariable("itemId") Integer itemId, Principal principal, HttpSession session) {
		Item item = itemRepository.findItemByEmailAndItemId(principal.getName(), itemId);
		if (item == null) {
			return "redirect:/auction-house/manage-items/0";
		}
		try {
			item.setDeleted(true);
			itemRepository.save(item);
			session.setAttribute("message", new Message("Item Successfully Deleted!", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("something went wrong!", "alert-danger"));
		}
		return "redirect:/auction-house/manage-items/0";
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
				if (newPassword.length() < 8) {
					session.setAttribute("message",
							new Message("password length must be greater than 8 characters", "alert-danger"));
					return "redirect:new-password";
				}
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
		return "redirect:edit-details";
	}
}
