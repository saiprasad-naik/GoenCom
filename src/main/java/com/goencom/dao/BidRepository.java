package com.goencom.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goencom.entities.Bid;
import com.goencom.entities.Item;

public interface BidRepository extends JpaRepository<Bid, Integer> {
	@Query("from Bid as b where b.user.email =:email and b.Auction.auctionId =:auctionId ")
	public Bid findBidbyEmailAndAuctionId(@Param("email")String email, @Param("auctionId")Integer auctionId);
}
