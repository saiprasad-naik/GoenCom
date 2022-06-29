package com.goencom.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.goencom.dao.AuctionRepository;
import com.goencom.dao.BidRepository;
import com.goencom.dao.InterestRepository;
import com.goencom.dao.ItemRepository;
import com.goencom.dao.UserRepository;
import com.goencom.entities.Auction;
import com.goencom.entities.Bid;
import com.goencom.entities.Interest;
import com.goencom.entities.Item;
import com.goencom.entities.User;
import com.goencom.helper.Message;
import com.goencom.service.EmailService;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private BidRepository bidRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private InterestRepository interestRepository;
	@Autowired
	private EmailService emailService;

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String home(Principal principal, Model model) {
		List<Auction> auctions = auctionRepository.findAllActiveAuction();
		List<Item> items = itemRepository.findAllUpcommingItems();
		model.addAttribute("auctions", auctions);
		model.addAttribute("items", items);
		if (principal == null) {
			model.addAttribute("user", new User());
		} else {
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
	public String sign_up(Model model, HttpSession session) {
		String email = (String) session.getAttribute("email");
		session.removeAttribute("email");
		if(email.equals(null)) {
			return "redirect:/new-email";
		}
		User user = new User();
		user.setEmail(email);
		model.addAttribute("user", user);
		return "signup";
	}

	@RequestMapping(path = "/do-register", method = RequestMethod.POST)
	public String register(@Valid @ModelAttribute("user") User user,BindingResult result,Model model, HttpSession session) {
		if(result.hasErrors()) {
			model.addAttribute("user", user);
			return "signup";
		}
		
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

	@GetMapping("/upcomming/{itemId}")
	public String upcomming(@PathVariable("itemId") Integer itemId, Model model, Principal principal) {
		Optional<Item> optionalItem = itemRepository.findById(itemId);
		Item item = optionalItem.get();
		model.addAttribute("item", item);
		if (principal != null) {
			Interest interest = interestRepository.findInterestbyEmailAndItemId(principal.getName(), itemId);
			model.addAttribute("interest", interest);
			User user = userRepository.getUserByEmail(principal.getName());
			model.addAttribute("user", user);
		}else {
			model.addAttribute("user", new User());
		}
		return "upcoming-bids";
	}

	@GetMapping("/product/{auctionId}")
	public String product(@PathVariable("auctionId") Integer auctionId, Model model, Principal principal) {
		Optional<Auction> optionalAuction = auctionRepository.findById(auctionId);
		Auction auction = optionalAuction.get();
		model.addAttribute("auction", auction);

		if (principal != null) {
			Bid bid = bidRepository.findBidbyEmailAndAuctionId(principal.getName(), auctionId);
			model.addAttribute("bid", bid);
			User user = userRepository.getUserByEmail(principal.getName());
			model.addAttribute("user", user);
		}else {
			model.addAttribute("user", new User());
		}
		return "product-bid";
	}

	@GetMapping("/forgot-password")
	public String forgotPassword(HttpSession session) {
		session.removeAttribute("email");
		session.removeAttribute("otp");
		return "send-otp";
	}

	@PostMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email, HttpSession session) {
		User user = userRepository.getUserByEmail(email);
		if (user != null) {
			Random random = new Random();
			int otp = random.nextInt(99999);
			String subject = "GoenCom OTP";
			String message = "your otp is : " + otp;
			boolean result = this.emailService.sendEmail(subject, message, email);
			if (result) {
				session.setAttribute("message", new Message("otp sent!!!", "alert-success"));
				session.setAttribute("otp", otp);
				session.setAttribute("email", email);
			} else {
				session.setAttribute("message", new Message("otp not sent!!!", "alert-danger"));
				return "redirect:/send-otp";
			}
		} else {
			session.setAttribute("message", new Message("otp not sent!!!", "alert-danger"));
			return "redirect:/forgot-password";
		}
		return "verify-otp";
	}

	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") Integer userOtp, HttpSession session) {
		int otp = (int) session.getAttribute("otp");
		session.removeAttribute("otp");
		if (userOtp != otp) {
			session.setAttribute("message", new Message("invalid otp!!!", "alert-danger"));
			return "redirect:/forgot-password";
		}
		return "new-password";
	}

	@PostMapping("/set-new-password")
	public String setNewPassword(@RequestParam("new-password") String newPassword,
			@RequestParam("new-password2") String newPassword2, HttpSession session) {
		if (newPassword.equals(newPassword2)) {
			String email = (String) session.getAttribute("email");
			User user = userRepository.getUserByEmail(email);
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			session.setAttribute("message", new Message("password changed successfully!!!", "alert-success"));
			session.removeAttribute("email");
		}else {
			session.setAttribute("message", new Message("passwords don't match!!!", "alert-danger"));
			return "new-password";
		}
		return "redirect:/sign-in";
	}
	
	@GetMapping("/new-email")
	public String newEmail() {
		return "new-email";
	}
	
	@PostMapping("/email-otp")
	public String emailOtp(@RequestParam("email") String email, HttpSession session) {
		User user = userRepository.getUserByEmail(email);
		if (user == null) {
			Random random = new Random();
			int otp = random.nextInt(99999);
			String subject = "GoenCom OTP";
			String message = "your otp is : " + otp;
			boolean result = this.emailService.sendEmail(subject, message, email);
			if (result) {
				session.setAttribute("message", new Message("otp sent!!!", "alert-success"));
				session.setAttribute("otp", otp);
				session.setAttribute("email", email);
			} else {
				session.setAttribute("message", new Message("otp not sent!!!", "alert-danger"));
				return "redirect:/new-email";
			}
		} else {
			session.setAttribute("message", new Message("otp not sent!!!", "alert-danger"));
			return "redirect:/new-email";
		}
		return "email-otp";
	}
	
	@PostMapping("/confirm-email")
	public String confirmEmail(@RequestParam("otp") Integer userOtp, HttpSession session){
		int otp = (int) session.getAttribute("otp");
		session.removeAttribute("otp");
		if (userOtp != otp) {
			session.setAttribute("message", new Message("invalid otp!!!", "alert-danger"));
			session.removeAttribute("email");
			return "redirect:/new-email";
		}
		return "redirect:/sign-up";
	}
}