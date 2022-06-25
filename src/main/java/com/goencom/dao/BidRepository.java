package com.goencom.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goencom.entities.Bid;

public interface BidRepository extends JpaRepository<Bid, Integer> {
	@Query("from Bid as b where b.user.email =:email and b.Auction.auctionId =:auctionId ")
	public Bid findBidbyEmailAndAuctionId(@Param("email")String email, @Param("auctionId")Integer auctionId);
	
	@Query("from Bid as b where b.Auction.auctionId =:auctionId ")
	public List<Bid> findBidsbyAuctionId(@Param("auctionId")Integer auctionId);
	
	@Query("from Bid as b where b.user.email =:email and b.status = 'PENDING' ")
	public Page<Bid> findActiveBidsByUser(@Param("email")String email, Pageable pageable);
	
	@Query("from Bid as b where b.user.email =:email and b.status <> 'PENDING' ")
	public Page<Bid> findInactiveBidsByUser(@Param("email")String email, Pageable pageable);
}
