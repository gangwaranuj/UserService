package com.ms.userservice.services;

import java.util.List;

import com.ms.userservice.entities.User;

public interface IUserService {

	User save(User user);
	
	List<User> getAllUser();
	
	User getUserById(String id);
	
}
