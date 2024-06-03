package tms.task_management_system.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tms.task_management_system.dto.UserDTO;
import tms.task_management_system.entity.Users;
import tms.task_management_system.service.UserService;

@Controller
public class UserWebController {

	private static final String REDIRECT_USERS = "redirect:/users";
	
	 private static final String FLASH_MESSAGE_KEY = "message";

	private final UserService userService;

	public UserWebController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/users")
	public String getAllUsers(Model model) {
		List<Users> userList = userService.getAllUsers();
		model.addAttribute("users", userList);
		return "allUsers";
	}

	@GetMapping("/users/create")
	public String showSaveUserForm() {
		return "saveUser";
	}

	@PostMapping("/users/create")
	public String saveUser(@RequestParam String name, @RequestParam String email, @RequestParam String password,
			@RequestParam String role, RedirectAttributes redirectAttributes) {
		UserDTO userDTO = new UserDTO(null, name, email, password, role);
		userService.saveUser(userDTO.toUser());
		redirectAttributes.addFlashAttribute(FLASH_MESSAGE_KEY, "User created successfully!");
		return REDIRECT_USERS;
	}

	@GetMapping("/users/edit/{id}")
	public String showEditUserForm(@PathVariable Long id, Model model) {
		Users user = userService.getUserById(id).orElse(null);
		model.addAttribute("user", user);
		return "editUser";
	}

	@PostMapping("/users/edit/{id}")
	public String updateUser(@PathVariable Long id, @RequestParam String name, @RequestParam String email,
			@RequestParam String password, @RequestParam String role, RedirectAttributes redirectAttributes) {
		UserDTO userDTO = new UserDTO(id, name, email, password, role);
		userService.saveUser(userDTO.toUser());
		redirectAttributes.addFlashAttribute(FLASH_MESSAGE_KEY, "User updated successfully!");
		return REDIRECT_USERS;
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUserById(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		userService.deleteUserById(id);
		redirectAttributes.addFlashAttribute(FLASH_MESSAGE_KEY, "User deleted successfully!");
		return REDIRECT_USERS;
	}

}
