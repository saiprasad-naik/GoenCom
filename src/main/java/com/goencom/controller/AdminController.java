package com.goencom.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.goencom.dao.UserRepository;
import com.goencom.entities.Item;
import com.goencom.entities.User;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/")
	public String home() {
		return "admin-dashboard";
	}
	
	@GetMapping("/manage-users/{page}")
	public String manageUsers(@PathVariable("page") Integer page, Model model) {
		Pageable pageable = PageRequest.of(page, 4);
		Page<User> users = userRepository.findAllUsers(pageable);
		model.addAttribute("users", users);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", users.getTotalPages());
		return "admin-manage-users";
	}
	
	@GetMapping("/manage-auction-house/{page}")
	public String manageAdmin(@PathVariable("page") Integer page, Model model) {
		Pageable pageable = PageRequest.of(page, 4);
		Page<User> users = userRepository.findAllAuctionHouses(pageable);
		model.addAttribute("users", users);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", users.getTotalPages());
		return "admin-manage-auction-house";
	}
	
	@GetMapping("/manage-items")
	public String manageItems() {
		return "admin-manage-items";
	}
	
}
