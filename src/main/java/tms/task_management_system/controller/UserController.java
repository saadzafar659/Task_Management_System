package tms.task_management_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tms.task_management_system.entity.Users;
import tms.task_management_system.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController 
{
	
	
	
	private final UserService userService;
	
	public UserController(UserService userService)
	{
		this.userService = userService;
	}

    @GetMapping("/getAll")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

}
