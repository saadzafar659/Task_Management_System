package tms.task_management_system.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tms.task_management_system.dto.UserDTO;
import tms.task_management_system.entity.Users;
import tms.task_management_system.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/getAll")
	public List<Users> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Users> getUserById(@PathVariable Long id) {
		Optional<Users> usersOptional = userService.getUserById(id);
		return usersOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/create")
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
		Users user = userDTO.toUser();
		Users savedUser = userService.saveUser(user);
		UserDTO savedUserDTO = new UserDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail(),
				savedUser.getPassword(), savedUser.getRole());
		return new ResponseEntity<>(savedUserDTO, HttpStatus.CREATED);
	}


	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		Optional<Users> existingUserOpt = userService.getUserById(id);
		if (!existingUserOpt.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		userService.deleteUserById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
