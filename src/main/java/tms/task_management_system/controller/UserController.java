package tms.task_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tms.task_management_system.entity.Users;
import tms.task_management_system.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController 
{
	
	@Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

}
