package com.goencom.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goencom.entities.Item;

public interface AuctionHouseRepository extends JpaRepository<Item, Integer>{

}
