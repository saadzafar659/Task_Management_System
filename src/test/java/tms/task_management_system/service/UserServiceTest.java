package tms.task_management_system.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import tms.task_management_system.entity.Users;
import tms.task_management_system.repository.UserRepository;

@SpringBootTest
class UserServiceTest {

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	private List<Users> userList;

	@BeforeEach
	public void setUp() {
		Users user1 = new Users(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN", null);
		Users user2 = new Users(2L, "Saad Khan", "skhan@gmail.com", "password2", "USER", null);
		userList = Arrays.asList(user1, user2);
	}

	@Test
	void testGetAllUsers() {
		// Given
		when(userRepository.findAll()).thenReturn(userList);

		// When
		List<Users> result = userService.getAllUsers();

		// Then
		// assert
		assertEquals(2, result.size());
		assertEquals("Saad Zafar", result.get(0).getName());
		assertEquals("Saad Khan", result.get(1).getName());
	}

	@Test
	void testGetUserById() {
		// Given
		Long userId = 1L;
		when(userRepository.findById(userId)).thenReturn(Optional.of(userList.get(0)));

		// When
		Optional<Users> result = userService.getUserById(userId);

		// Then
		// assert
		assertEquals("Saad Zafar", result.get().getName());
	}

	@Test
	void testSaveUser() {
		// Given
		Users user = new Users(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN", null);

		Users savedUser = new Users(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN", null);

		when(userRepository.save(user)).thenReturn(savedUser);

		// When
		Users result = userService.saveUser(user);

		// Then
		// assert
		assertEquals(savedUser.getId(), result.getId());
		assertEquals(savedUser.getName(), result.getName());
		verify(userRepository).save(user);
	}

	@Test
	void testDeleteUserById() {
		// Given
		Long userId = 1L;

		// When
		userService.deleteUserById(userId);

		// Then
		// verify
		verify(userRepository, times(1)).deleteById(userId);
	}
}
