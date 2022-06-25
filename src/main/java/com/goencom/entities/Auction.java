package com.goencom.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "auction")
public class Auction {

	public static final String FINNISHED = "FINNISHED";
	public static final String RUNNING = "RUNNING";
	public static final String NOT_STARTED = "NOT_STARTED";
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int auctionId;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Item item;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private User user;
	private boolean enable;
	private String status;

	public Auction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
