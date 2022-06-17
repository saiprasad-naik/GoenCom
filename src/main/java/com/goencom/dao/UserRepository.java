package com.goencom.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goencom.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
