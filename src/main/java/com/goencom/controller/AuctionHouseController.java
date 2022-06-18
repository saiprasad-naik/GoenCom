package com.goencom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.goencom.dao.AuctionHouseRepository;
import com.goencom.entities.AuctionHouse;
import com.goencom.entities.Item;

@Controller
@RequestMapping("/auction-house")
public class AuctionHouseController {
	@Autowired
	private AuctionHouseRepository auctionHouseRepository;

	@RequestMapping("/")
	public String home(Model model) {
		AuctionHouse auctionHouse = new AuctionHouse();
		auctionHouse.setStreet("davorlim");
		auctionHouse.setCity("Margao");
		auctionHouse.setState("goa");
		auctionHouse.setCountry("india");
		auctionHouse.setPincode(403707);
		model.addAttribute("auctionHouse", auctionHouse);
		return "auction-house";
	}

	@RequestMapping("/manage-items")
	public String manageItem(Model model) {
		return "manage-auction-items";
	}

	@RequestMapping("/new-item")
	public String newItem(Model model) {
		model.addAttribute("item", new Item());
		return "add-item";
	}

	@RequestMapping(path = "/add-item", method = RequestMethod.POST)
	public String addItem(@ModelAttribute("item") Item item) {
		auctionHouseRepository.save(item);
		return "add-item";
	}
}
