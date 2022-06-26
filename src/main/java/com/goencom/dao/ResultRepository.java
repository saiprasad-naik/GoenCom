package com.goencom.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goencom.entities.Result;

public interface ResultRepository extends JpaRepository<Result, Integer>{
	@Query("from Result as r where r.bid.Auction.user.email =:email")
	public Page<Result> findAuctionResultsByAuctionHouse(@Param("email") String email, Pageable pageable);
}
