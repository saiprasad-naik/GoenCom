package com.goencom.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.goencom.dao.UserRepository;
import com.goencom.entities.User;
import com.goencom.helper.Message;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String home() {
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
	
	@RequestMapping("/auction-house-login")
	public String auctionHouseSignIn() {
		return "auction-house-login";
	}
}