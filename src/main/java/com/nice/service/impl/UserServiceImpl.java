package com.nice.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.nice.domain.User;
import com.nice.exception.UserNotFoundException;
import com.nice.exception.UsernameTakenException;
import com.nice.repository.UserRepository;
import com.nice.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	private UserRepository repository;

	public UserServiceImpl(UserRepository reposioty) {
		this.repository = reposioty;
	}

	@Override
	public User findOne(Long id) {
		Objects.requireNonNull(id, "Id is required to find user");
		Optional<User> user = this.repository.findById(id);
		if (user.isPresent()) {
			logger.info(String.format("User with id %s retrieved", id));
			return user.get();
		}
		logger.error(String.format("User with id %s not found", id));
		throw new UserNotFoundException(String.format("User with id %s not found", id));
	}

	@Override
	public User findOne(String username) {
		Assert.hasText(username, "Username is requied to find user");
		Optional<User> user = this.repository.findByUserName(username);
		if (user.isPresent()) {
			logger.info(String.format("User with username %s retrieved", username));
			return user.get();
		}
		logger.error(String.format("User with username %s not found", username));
		throw new UserNotFoundException(String.format("User with username %s not found", username));
	}

	@Override
	public List<User> findAll() {
		return repository.findAll();
	}
	
	@Override
	public User create(@Valid User user) {
		String username = user.getUserName();
		try{
			logger.info(String.format("Checking if username %s is available", username));
			findOne(username);
			throw new UsernameTakenException(String.format("Username %s already taken", username));
		} catch(UserNotFoundException exception) {
			User persistedUser = repository.save(user);
			logger.info("User saved");
			return persistedUser;
		}
	}

	@Override
	public void update(Long id, User user) {
		Objects.requireNonNull(id, "Id is required to update");
		User retrievedUser = findOne(id);
		retrievedUser.setFirstName(user.getFirstName());
		retrievedUser.setLastName(user.getLastName());
		retrievedUser.setEmail(user.getEmail());
		retrievedUser.setUserName(user.getUserName());
		this.repository.save(retrievedUser);
		logger.info(String.format("User with id %s updated", id));
	}

	@Override
	public void delete(Long id) {
		Objects.requireNonNull(id, "Id must not be null");
		findOne(id);
		repository.deleteById(id);
		logger.info(String.format("User with id %s deleted", id));
	}


}
