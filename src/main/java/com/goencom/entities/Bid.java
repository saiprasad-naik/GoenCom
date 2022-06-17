package com.goencom.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bid")
public class Bid {
	public static final String WON_STATUS = "WON";
	public static final String LOST_STATUS = "LOST";
	public static final String PENDING_STATUS = "PENDING";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int bidId;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Auction Auction;
	private int amount;
	private String status;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private User user;

	public Bid() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getBidId() {
		return bidId;
	}

	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	public Auction getAuction() {
		return Auction;
	}

	public void setAuction(Auction auction) {
		Auction = auction;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
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
