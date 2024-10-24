package tms.task_management_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import tms.task_management_system.entity.Users;
import tms.task_management_system.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<Users> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<Users> getUserById(Long id) {
		return userRepository.findById(id);
	}

	public Users saveUser(Users user) {
		return userRepository.save(user);
	}

	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

}
