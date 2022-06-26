package com.goencom.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.goencom.dao.AuctionRepository;
import com.goencom.dao.BidRepository;
import com.goencom.dao.InterestRepository;
import com.goencom.dao.ItemRepository;
import com.goencom.dao.UserRepository;
import com.goencom.entities.Auction;
import com.goencom.entities.Bid;
import com.goencom.entities.Interest;
import com.goencom.entities.Item;
import com.goencom.entities.User;
import com.goencom.helper.Message;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private BidRepository bidRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private InterestRepository interestRepository;
	
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String home(Principal principal, Model model) {
		List<Auction> auctions = auctionRepository.findAllActiveAuction();
		List<Item> items = itemRepository.findAllUpcommingItems();
		model.addAttribute("auctions", auctions);
		model.addAttribute("items", items);
		if(principal == null) {
			model.addAttribute("user", new User());
		}else {
			User user = userRepository.getUserByEmail(principal.getName());
			model.addAttribute("user", user);
		}
		return "index";
	}
	
	@RequestMapping(path = "/about", method = RequestMethod.GET)
	public String about() {
		return "about-us";
	}
	
	@RequestMapping(path = "/sign-in", method = RequestMethod.GET)
	public String sign_in() {
		return "signin";
	}
	
	@RequestMapping(path = "/sign-up", method = RequestMethod.GET)
	public String sign_up(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping(path = "/do-register", method = RequestMethod.POST)
	public String register(@ModelAttribute("user") User user, Model model, HttpSession session) {
		try {
			user.setEnabled(true);
			user.setRole("ROLE_USER");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			session.setAttribute("message", new Message("successfully registered!!!", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("oops something went wrong!!!", "alert-danger"));
			return "signup";
		}
		
		return "signin";
	}
	
	@GetMapping("/upcomming/{itemId}")
	public String upcomming(@PathVariable("itemId") Integer itemId,Model model, Principal principal) {
		Optional<Item> optionalItem = itemRepository.findById(itemId);
		Item item = optionalItem.get();
		model.addAttribute("item", item);
		if(principal != null) {
			Interest interest = interestRepository.findInterestbyEmailAndItemId(principal.getName(), itemId);
			model.addAttribute("interest", interest);
		}
		return "upcoming-bids";
	}
	
	@GetMapping("/product/{auctionId}")
	public String product(@PathVariable("auctionId") Integer auctionId, Model model, Principal principal) {
		Optional<Auction> optionalAuction = auctionRepository.findById(auctionId);
		Auction auction = optionalAuction.get();
		model.addAttribute("auction", auction);
		
		if(principal != null) {
			Bid bid =  bidRepository.findBidbyEmailAndAuctionId(principal.getName(), auctionId);
			model.addAttribute("bid", bid);
		}
		return "product-bid";
	}
}