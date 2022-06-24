package com.goencom.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.goencom.dao.AuctionRepository;
import com.goencom.dao.ItemRepository;
import com.goencom.dao.UserRepository;
import com.goencom.entities.Auction;
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
	private BCryptPasswordEncoder passwordEncoder;
	
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String home(Principal principal, Model model) {
		List<Auction> auctions = auctionRepository.findAllActiveAuction();
		model.addAttribute("auctions", auctions);
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
}