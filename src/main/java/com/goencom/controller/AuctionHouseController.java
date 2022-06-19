package com.goencom.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.goencom.dao.ItemRepository;
import com.goencom.dao.UserRepository;
import com.goencom.entities.Image;
import com.goencom.entities.Item;
import com.goencom.entities.User;
import com.goencom.helper.Message;

@Controller
@RequestMapping("/auction-house")
public class AuctionHouseController {
	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String home(Model model, Principal principal) {
		User user = userRepository.getUserByEmail(principal.getName());
		model.addAttribute("user", user);
		return "auction-house";
	}

	@RequestMapping("/manage-items/{page}")
	public String manageItem( @PathVariable("page") Integer page, Model model, Principal principal) {
		User user = userRepository.getUserByEmail(principal.getName());
		Pageable pageable = PageRequest.of(page, 4);
		Page<Item> items = itemRepository.findItemsByUser(user.getUserId(), pageable);
		model.addAttribute("items", items);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", items.getTotalPages());
		return "manage-auction-items";
	}

	@RequestMapping("/new-item")
	public String newItem(Model model) {
		model.addAttribute("item", new Item());
		return "add-item";
	}

	@RequestMapping(path = "/add-item", method = RequestMethod.POST)
	public String addItem(@ModelAttribute("item") Item item, @RequestParam("image") MultipartFile file  , Principal principal, HttpSession session, Model model) {
		try {
			User user = userRepository.getUserByEmail(principal.getName());
			if(file.isEmpty()) {
				System.out.println("file is empty");
				Image image = new Image();
				image.setUrl("—Pngtree—a shoe store sells shoes_7256461.png");
				image.setItem(item);
				item.getImages().add(image);
			}else {
				Image image = new Image();
				image.setUrl(file.getOriginalFilename());
				image.setItem(item);
				item.getImages().add(image);
				File saveFile = new ClassPathResource("static/images").getFile();
				Path  path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			item.setUser(user);
			user.getItems().add(item);
			userRepository.save(user);
			session.setAttribute("message", new Message("successfully added item!", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("item", item);
			session.setAttribute("message", new Message("oops something went wrong!!!", "alert-danger"));
			return "add-item";
		}
		return "add-item";
	}
}
