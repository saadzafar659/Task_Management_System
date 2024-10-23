package tms.task_management_system.it;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import tms.task_management_system.con.Conn;
import tms.task_management_system.dto.UserDTO;
import tms.task_management_system.entity.Users;
import tms.task_management_system.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTestIT extends Conn {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private UserRepository userRepository;

	private List<Users> userList;
	private UserDTO userDTO;

	@BeforeEach
	public void setUp() {
		userRepository.deleteAll();
		
		userDTO = new UserDTO(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN");
		
		Users user1 = new Users(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN", null);
		Users user2 = new Users(2L, "Saad Khan", "skhan@gmail.com", "password2", "USER", null);
		userList = Arrays.asList(user1, user2);
		userRepository.saveAll(userList);

	}

	@Test
	void testGetAllUsers() {
		// When
		ResponseEntity<Users[]> response = restTemplate.getForEntity("http://localhost:" + port + "/api/users/getAll",
				Users[].class);

		// Assert
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getBody().length, equalTo(2));
	}
	
	@Test
	void testCreateUser() {
		// When
		ResponseEntity<UserDTO> response = restTemplate.postForEntity("http://localhost:" + port + "/api/users/create",
				userDTO, UserDTO.class);

		// Assert
		assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
		assertThat(response.getBody().getName(), equalTo(userDTO.getName()));
		assertThat(userRepository.findById(response.getBody().getId()).isPresent(), is(true));
	}

	@Test
	void testDeleteUser() {
		// Given
		Users savedUser = userList.get(0);

		// When
		restTemplate.delete("http://localhost:" + port + "/api/users/delete/" + savedUser.getId());

		// Assert
		assertThat(userRepository.findById(savedUser.getId()).isPresent(), is(false));
	}
}
