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
import tms.task_management_system.dto.TaskDTO;
import tms.task_management_system.entity.Task;
import tms.task_management_system.entity.Users;
import tms.task_management_system.repository.TaskRepository;
import tms.task_management_system.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTestIT extends Conn {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private UserRepository userRepository;

	private List<Task> taskList;
	private TaskDTO validTaskDTO;
	private Users user;

	@BeforeEach
	public void setUp() {
		taskRepository.deleteAll();
		userRepository.deleteAll();

		user = new Users(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN", null);
		user = userRepository.save(user);

		Task task1 = new Task(1L, "Title1", "Description1", "2024-5-31", "Pending", user);
		Task task2 = new Task(2L, "Title2", "Description2", "2024-5-31", "Completed", user);
		taskList = Arrays.asList(task1, task2);
		taskRepository.saveAll(taskList);

		validTaskDTO = new TaskDTO(null, "New Task Title", "Description of the new task", "2024-5-31", "Pending",
				user.getId());
	}

	@Test
	void testGetAllTasks() {
		// When
		ResponseEntity<Task[]> response = restTemplate.getForEntity("http://localhost:" + port + "/api/tasks/getAll",
				Task[].class);

		// Assert
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getBody().length, equalTo(2));
	}

	@Test
	void testGetTaskById() {
		// Given
		Task savedTask = taskRepository.findAll().get(0);

		// When
		ResponseEntity<Task> response = restTemplate
				.getForEntity("http://localhost:" + port + "/api/tasks/" + savedTask.getId(), Task.class);

		// Assert
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getBody().getId(), equalTo(savedTask.getId()));
		assertThat(response.getBody().getTitle(), equalTo(savedTask.getTitle()));
	}

	@Test
	void testCreateTask() {

		// When
		ResponseEntity<TaskDTO> response = restTemplate.postForEntity("http://localhost:" + port + "/api/tasks/create",
				validTaskDTO, TaskDTO.class);

		// Assert
		assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
		assertThat(response.getBody().getTitle(), equalTo(validTaskDTO.getTitle()));
		assertThat(taskRepository.findById(response.getBody().getId()).isPresent(), is(true));
	}

	@Test
	void testDeleteTask() {
		// Given
		Task savedTask = taskList.get(0);

		// When
		restTemplate.delete("http://localhost:" + port + "/api/tasks/delete/" + savedTask.getId());

		// Assert
		assertThat(taskRepository.findById(savedTask.getId()).isPresent(), is(false));
	}
}
