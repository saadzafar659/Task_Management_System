package tms.task_management_system.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return usersOptional.map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }

}
