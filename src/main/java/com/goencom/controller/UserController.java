package com.goencom.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	public String profile() {
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
}
