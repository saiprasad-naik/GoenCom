package com.goencom.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goencom.entities.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>{
	
	@Query("from Item as i where i.user.userId =:userId ")
	public Page<Item> findItemsByUser(@Param("userId") int userId, Pageable pageable);
	
	@Query("from Item as i where i.user.userId =:userId and i.auctioned = false")
	public Page<Item> findItemsByUserForAuction(@Param("userId") int userId, Pageable pageable);
}
