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
@Table(name = "interest")
public class Interest {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int interestID;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Item item;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private User user;

	public Interest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getInterestID() {
		return interestID;
	}

	public void setInterestID(int interestID) {
		this.interestID = interestID;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
