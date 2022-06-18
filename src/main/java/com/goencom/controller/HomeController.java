package com.goencom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goencom.dao.UserRepository;
import com.goencom.entities.User;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepository;
	
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
}