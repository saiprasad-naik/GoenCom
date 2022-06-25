package com.goencom.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goencom.entities.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Integer>{
	@Query("from Auction as a where a.user.userId =:userId and a.enable = true and a.status = 'RUNNING'")
	public Page<Auction> findActiveAuctionByUser(@Param("userId") int userId, Pageable pageable);
	
	@Query("from Auction as a where a.status = 'RUNNING'")
	public List<Auction> findAllActiveAuction();
	
	@Query("from Auction as a where a.item.itemId =:itemId")
	public Auction findAuctionByItemId(@Param("itemId") int itemId);
}
