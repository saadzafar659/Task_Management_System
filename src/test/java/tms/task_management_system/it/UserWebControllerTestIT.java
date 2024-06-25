package tms.task_management_system.it;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import tms.task_management_system.con.Conn;
import tms.task_management_system.entity.Users;
import tms.task_management_system.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserWebControllerTestIT extends Conn {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private UserRepository userRepository;

	private Users user1;
	private Users user2;

	@BeforeEach
	@Transactional
	void setUp() {

		userRepository.deleteAll();
		user1 = new Users(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN", null);
		user2 = new Users(2L, "Saad Khan", "skhan@gmail.com", "password2", "USER", null);
		userRepository.saveAll(List.of(user1, user2));
	}

	@Test
	void testGetAllUsers() {

		// When
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/users",
				String.class);

		// Then
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		// Assert
		assertThat(response.getBody(), containsString("Saad Zafar"));
		assertThat(response.getBody(), containsString("Saad Khan"));
	}

	@Test
	void testShowSaveUserForm() {

		// When
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/users/create",
				String.class);

		// Then
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		// Assert
		assertThat(response.getBody(), containsString("Create User"));
	}

	@Test
	void testShowEditUserForm() {
		// Given
		Users user = userRepository.findAll().get(0);

		// When
		ResponseEntity<String> response = restTemplate
				.getForEntity("http://localhost:" + port + "/users/edit/" + user.getId(), String.class);

		// Then
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		// Assert
		assertThat(response.getBody(), containsString("Edit User"));
		assertThat(response.getBody(), containsString(user1.getName()));
	}

	@Test
	void testDeleteUserById() {
		// Given
		Users user = userRepository.findAll().get(0);

		// When
		restTemplate.getForEntity("http://localhost:" + port + "/users/delete/" + user.getId(), String.class);

		// Then
		// Assert
		assertThat(userRepository.findById(user.getId()).isEmpty(), equalTo(true));
	}
}
