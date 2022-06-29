package com.goencom.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goencom.entities.Interest;

public interface InterestRepository extends JpaRepository<Interest, Integer> {

	@Query("from Interest as i where i.user.email =:email and i.item.itemId =:itemId ")
	public Interest findInterestbyEmailAndItemId(@Param("email") String email, @Param("itemId") Integer itemId);

	@Query("from Interest as i where i.user.email =:email and i.item.auctioned = false and i.item.enabled = true ")
	public Page<Interest> findInterestbyEmail(@Param("email") String email, Pageable pageable);
	
	@Query("from Interest as i where i.user.email =:email and i.item.auctioned = true and i.item.enabled = true ")
	public Page<Interest> findActiveInterestbyEmail(@Param("email") String email, Pageable pageable);
	
	@Query("from Interest as i where i.item.itemId =:itemId ")
	public List<Interest> findInterestbyItemId(@Param("itemId") Integer itemId);

}
