package com.goencom.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goencom.entities.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>{
	
	@Query("from Item as i where i.user.userId =:userId and i.enabled = true and i.deleted = false")
	public Page<Item> findItemsByUser(@Param("userId") int userId, Pageable pageable);
	
	@Query("from Item as i where i.user.userId =:userId and i.auctioned = false and i.deleted = false")
	public Page<Item> findItemsByUserForAuction(@Param("userId") int userId, Pageable pageable);
	
	@Query("from Item as i where i.visible = true and i.auctioned = false and i.deleted = false")
	public List<Item> findAllUpcommingItems();
	
	@Query("from Item as i where i.user.email =:email and i.itemId =:itemId and i.deleted = false")
	public Item findItemByEmailAndItemId(@Param("email") String email, @Param("itemId") int itemId);
	
	@Query("from Item as i where i.deleted = false order by i.itemId desc")
	public Page<Item> findAllItems(Pageable pageable);
}
