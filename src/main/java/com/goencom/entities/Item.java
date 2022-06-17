package com.goencom.entities;

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
	private String Description;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Auction auction;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private AuctionHouse auctionHouse;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Image> images;
	private Boolean enabled;
	private Boolean deleted;
	private Boolean Visible;

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
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Auction getAuction() {
		return auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}

	public AuctionHouse getAuctionHouse() {
		return auctionHouse;
	}

	public void setAuctionHouse(AuctionHouse auctionHouse) {
		this.auctionHouse = auctionHouse;
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

}
