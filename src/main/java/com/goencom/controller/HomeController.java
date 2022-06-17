package com.goencom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goencom.dao.UserRepository;
import com.goencom.entities.User;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/test")
	@ResponseBody
	public String test() {
		User user = new User();
		user.setfName("vaibhav");
		user.setlName("kalal");
		userRepository.save(user); 
		return "working";
	}
}
