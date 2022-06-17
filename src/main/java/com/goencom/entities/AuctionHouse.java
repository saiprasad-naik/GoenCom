package com.goencom.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "auction_house")
public class AuctionHouse {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int auctionHouseId;
	private String name;
	@Column(length = 35)
	private String street;
	@Column(length = 35)
	private String city;
	@Column(length = 35)
	private String state;
	@Column(length = 35)
	private String country;
	private int pincode;
	private String email;
	private String password;
	@Column(length = 10)
	private String phoneNo;
	private String website;
	@Column(length = 500)
	private String Description;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Item> items = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Auction> auctions = new ArrayList<>();
	private boolean enabled;
	private boolean deleted;

	public AuctionHouse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getAuctionHouseId() {
		return auctionHouseId;
	}

	public void setAuctionHouseId(int auctionHouseId) {
		this.auctionHouseId = auctionHouseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

}
