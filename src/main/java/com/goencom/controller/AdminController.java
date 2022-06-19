package com.goencom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@GetMapping("/")
	public String home() {
		return "admin-dashboard";
	}
	
	@GetMapping("/manage-users")
	public String manageUsers() {
		return "admin-manage-users";
	}
	
	@GetMapping("/manage-auction-house")
	public String manageAdmin() {
		return "admin-manage-auction-house";
	}
	
	@GetMapping("/manage-items")
	public String manageItems() {
		return "admin-manage-items";
	}
	
}
