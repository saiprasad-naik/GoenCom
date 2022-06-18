package com.goencom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping(path = "/profile")
	public String profile() {
		return "user-profile";
	}
	
}
