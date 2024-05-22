package tms.task_management_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tms.task_management_system.entity.Users;
import tms.task_management_system.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<Users> getAllUsers() {
		return userRepository.findAll();
	}

}
