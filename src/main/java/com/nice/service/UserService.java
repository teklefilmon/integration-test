package com.nice.service;

import java.util.List;

import com.nice.domain.User;

public interface UserService {
	User findOne(Long id);
	User findOne(String username);
	List<User> findAll();
	User create(User user);
	void update(Long id, User user);
	void delete(Long id);
}
