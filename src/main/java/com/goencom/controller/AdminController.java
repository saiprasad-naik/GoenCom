package com.goencom.controller;

import java.security.Principal;

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

import com.goencom.dao.ItemRepository;
import com.goencom.dao.UserRepository;
import com.goencom.entities.Item;
import com.goencom.entities.User;
import com.goencom.helper.Message;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private ItemRepository itemRepository;

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

	@GetMapping("/manage-items/{page}")
	public String manageItems(@PathVariable("page") Integer page, Model model, Principal principal) {
		Pageable pageable = PageRequest.of(page, 4);
		Page<Item> items = itemRepository.findAllVisibleItems(pageable);
		model.addAttribute("items", items);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", items.getTotalPages());
		return "admin-manage-items";
	}

	@GetMapping("/new-auction-house")
	public String newAuctionHouse(Model model) {
		model.addAttribute("user", new User());
		return "admin-add-auction-house";
	}

	@PostMapping("/add-auction-house")
	public String addAuctionHouse(@ModelAttribute("user") User user, Principal principal, HttpSession session,
			Model model) {
		try {
			user.setEnabled(true);
			user.setRole("ROLE_AUCTIONEER");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			session.setAttribute("message", new Message("successfully registered!!!", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("oops something went wrong!!!", "alert-danger"));
			return "admin-add-auction-house";
		}
		return "redirect:/admin/manage-auction-house/0";
	}

}
