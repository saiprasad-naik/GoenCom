package com.goencom.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goencom.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer>{
	@Query("from Notification as n where n.user.userId =:userId")
	public List<Notification> findNotificationsUserId(@Param("userId") Integer userId);
}
