package tms.task_management_system.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import tms.task_management_system.dto.UserDTO;
import tms.task_management_system.entity.Users;
import tms.task_management_system.service.UserService;

@SpringBootTest
class UserControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	private List<Users> userList;

	@InjectMocks
	private UserController userController;

	private ObjectMapper objectMapper = new ObjectMapper();

	private UserDTO userDTO;
	private Users user;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
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
		when(userService.getAllUsers()).thenReturn(userList);

		// When
		// Get request
		mockMvc.perform(get("/api/users/getAll")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].name").value("Saad Zafar"))
				.andExpect(jsonPath("$[0].email").value("saad@gmail.com"))
				.andExpect(jsonPath("$[0].role").value("ADMIN")).andExpect(jsonPath("$[1].id").value(2L))
				.andExpect(jsonPath("$[1].name").value("Saad Khan"))
				.andExpect(jsonPath("$[1].email").value("skhan@gmail.com"))
				.andExpect(jsonPath("$[1].role").value("USER"));

		// Then
		// Verify
		verify(userService, times(1)).getAllUsers();
	}

	@Test
	void testGetUserById() throws Exception {
		// Given
		Long userId = 1L;
		when(userService.getUserById(userId)).thenReturn(Optional.of(userList.get(0)));

		// When
		// Get request
		mockMvc.perform(get("/api/users/{id}", userId)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.name").value("Saad Zafar"));

		// Then
		// Verify
		verify(userService, times(1)).getUserById(userId);
	}

	@Test
	void testGetUserById_NotFound() throws Exception {
		// Given
		Long userId = 1L;
		when(userService.getUserById(userId)).thenReturn(java.util.Optional.empty());

		// When and Then
		// Get request
		mockMvc.perform(get("/api/users/{id}", userId)).andExpect(status().isNotFound());
	}

	@Test
	void testCreateUser() throws Exception {
		// Given
		when(userService.saveUser(any(Users.class))).thenReturn(user);

		// When and Then
		// Post request
		mockMvc.perform(post("/api/users/create").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDTO))).andExpect(status().isCreated());
		// Verify
		verify(userService, times(1)).saveUser(any(Users.class));
	}

	@Test
	void testDeleteUser() throws Exception {
		// Given
		when(userService.getUserById(1L)).thenReturn(Optional.of(user));

		// When
		// DELETE request
		mockMvc.perform(delete("/api/users/delete/1"))
				// Then
				.andExpect(status().isNoContent());

		// Verify
		verify(userService, times(1)).getUserById(1L);
		verify(userService, times(1)).deleteUserById(1L);
	}

	@Test
	void testDeleteUser_NotFound() throws Exception {
		// Given
		when(userService.getUserById(1L)).thenReturn(Optional.empty());

		// When
		// DELETE request
		mockMvc.perform(delete("/api/users/delete/1"))
				// Then
				.andExpect(status().isNotFound());

		// Verify
		verify(userService, times(1)).getUserById(1L);
		verify(userService, times(0)).deleteUserById(1L);
	}

}
