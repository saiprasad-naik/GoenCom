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
@Table(name = "item")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int itemId;
	private String name;
	private String description;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Auction auction;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private User user;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Image> images = new ArrayList<>();
	private int basePrice;
	private Boolean enabled;
	private Boolean deleted;
	private Boolean Visible;
	private Boolean auctioned;

	public Item() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Auction getAuction() {
		return auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Boolean getVisible() {
		return Visible;
	}

	public void setVisible(Boolean visible) {
		Visible = visible;
	}

	public int getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(int basePrice) {
		this.basePrice = basePrice;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getAuctioned() {
		return auctioned;
	}

	public void setAuctioned(Boolean auctioned) {
		this.auctioned = auctioned;
	}

}
