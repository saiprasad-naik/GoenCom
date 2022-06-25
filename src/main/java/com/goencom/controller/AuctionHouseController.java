package com.goencom.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
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
import com.goencom.dao.ItemRepository;
import com.goencom.dao.ResultRepository;
import com.goencom.dao.UserRepository;
import com.goencom.entities.Auction;
import com.goencom.entities.Bid;
import com.goencom.entities.Image;
import com.goencom.entities.Item;
import com.goencom.entities.Result;
import com.goencom.entities.User;
import com.goencom.helper.*;

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
			System.out.println(visible);
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
			item.setAuction(auction);
			auction.setItem(item);
			auction.setEnable(true);
			user.getAuctions().add(auction);
			auction.setUser(user);
			auction.setStatus(Auction.RUNNING);
			auctionRepository.save(auction);
			auction = auctionRepository.findAuctionByItemId(itemId);
			/*
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
		List<Bid> candidates = highestBids(bids);
		Bid bid = bids.get(0);
		if (candidates.size() != 1) {
			bid = lowestBidId(candidates);
		}
		System.out.println(bid.getBidId());
		bid.getAuction().setStatus(Auction.FINNISHED);
		bid.setStatus(Bid.WON_STATUS);
		Result result = new Result();
		bid.setResult(result);
		result.setBid(bid);
		resultRepository.save(result);
		if (bids.size() != 1) {
			bids = exceptHighestBid(bids, bid);
			bidRepository.saveAll(bids);
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
}
