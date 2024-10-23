package tms.task_management_system.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import tms.task_management_system.dto.UserDTO;
import tms.task_management_system.entity.Users;
import tms.task_management_system.service.UserService;

@WebMvcTest(UserWebController.class)
class UserWebControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private UserDTO userDTO;
	private Users user;
	private List<Users> userList;

	@Captor
	private ArgumentCaptor<Users> userCaptor;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

		userDTO = new UserDTO(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN");
		user = new Users(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN", null);

		Users user1 = new Users(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN", null);
		Users user2 = new Users(2L, "Saad Khan", "skhan@gmail.com", "password2", "USER", null);
		userList = Arrays.asList(user1, user2);
	}

	@Test
	void testGetAllUsers() throws Exception {
		// Given
		given(userService.getAllUsers()).willReturn(userList);

		// When
		// Then
		// GET request
		mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(view().name("allUsers"))
				.andExpect(model().attributeExists("users")).andExpect(model().attribute("users", userList));
	}

	@Test
	void testShowSaveUserForm() throws Exception {
		// When
		// Then
		// GET request
		mockMvc.perform(get("/users/create")).andExpect(status().isOk()).andExpect(view().name("saveUser"));
	}

	@Test
	void testSaveUser() throws Exception {
		// When
		// Then
		// POST request
		mockMvc.perform(post("/users/create").param("name", userDTO.getName()).param("email", userDTO.getEmail())
				.param("password", userDTO.getPassword()).param("role", userDTO.getRole()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/users"))
				.andExpect(flash().attribute("message", "User created successfully!"));

		// Verify
		verify(userService, times(1)).saveUser(userCaptor.capture());
		Users capturedUser = userCaptor.getValue();
		assertEquals(userDTO.getName(), capturedUser.getName());
		assertEquals(userDTO.getEmail(), capturedUser.getEmail());
		assertEquals(userDTO.getPassword(), capturedUser.getPassword());
		assertEquals(userDTO.getRole(), capturedUser.getRole());
	}

	@Test
	void testShowEditUserForm() throws Exception {
		// Given
		given(userService.getUserById(1L)).willReturn(Optional.of(user));

		// When
		// Then
		// GET request
		mockMvc.perform(get("/users/edit/1")).andExpect(status().isOk()).andExpect(view().name("editUser"))
				.andExpect(model().attributeExists("user")).andExpect(model().attribute("user", user));
	}

	@Test
	void testUpdateUser() throws Exception {
		// When
		// Then
		// Edit request
		mockMvc.perform(post("/users/edit/1").param("name", userDTO.getName()).param("email", userDTO.getEmail())
				.param("password", userDTO.getPassword()).param("role", userDTO.getRole()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/users"))
				.andExpect(flash().attribute("message", "User updated successfully!"));

		// Verify
		verify(userService, times(1)).saveUser(userCaptor.capture());
		Users capturedUser = userCaptor.getValue();
		assertEquals(user.getId(), capturedUser.getId());
		assertEquals(userDTO.getName(), capturedUser.getName());
		assertEquals(userDTO.getEmail(), capturedUser.getEmail());
		assertEquals(userDTO.getPassword(), capturedUser.getPassword());
		assertEquals(userDTO.getRole(), capturedUser.getRole());
	}

	@Test
	void testDeleteUserById() throws Exception {
		// When
		// Then
		// DELETE request
		mockMvc.perform(get("/users/delete/1")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/users"))
				.andExpect(flash().attribute("message", "User deleted successfully!"));

		// Then
		verify(userService, times(1)).deleteUserById(1L);
	}
}
