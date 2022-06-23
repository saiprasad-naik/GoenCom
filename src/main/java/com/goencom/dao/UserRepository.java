package com.goencom.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goencom.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("select u from User u where u.email = :email")
	public User getUserByEmail(@Param("email")String email);
	
	@Query("select u from User u where u.role = 'ROLE_USER'")
	public Page<User> findAllUsers(Pageable pageable);
	
	@Query("select u from User u where u.role = 'ROLE_AUCTIONEER'")
	public Page<User> findAllAuctionHouses(Pageable pageable);
}
