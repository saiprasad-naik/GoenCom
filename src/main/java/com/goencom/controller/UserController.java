package com.goencom.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.goencom.dao.AuctionRepository;
import com.goencom.dao.BidRepository;
import com.goencom.dao.UserRepository;
import com.goencom.entities.Auction;
import com.goencom.entities.Bid;
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
	
	@RequestMapping(path = "/profile")
	public String profile(Model model, Principal principal) {
		User user = userRepository.getUserByEmail(principal.getName());
		model.addAttribute("user", user);
		return "user-profile";
	}
	
	@PostMapping("/bid/{auctionId}/")
	public String bid(@PathVariable("auctionId") Integer auctionId, @RequestParam("amount") Integer amount, Principal principal, HttpSession session) {
		try {
			User user = userRepository.getUserByEmail(principal.getName());
			Auction auction = auctionRepository.findById(auctionId).get();
			Bid bid =  bidRepository.findBidbyEmailAndAuctionId(principal.getName(), auctionId);
			if(bid == null) {
				bid = new Bid();
				bid.setAuction(auction);
				bid.setUser(user);
				bid.setAmount(amount);
				bid.setStatus(Bid.PENDING_STATUS);
				bidRepository.save(bid);
				session.setAttribute("message", new Message("successfully registered!!!", "alert-success"));
			}else {
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
}
