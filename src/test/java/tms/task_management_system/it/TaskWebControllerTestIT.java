package tms.task_management_system.it;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import tms.task_management_system.con.Conn;
import tms.task_management_system.entity.Task;
import tms.task_management_system.entity.Users;
import tms.task_management_system.repository.TaskRepository;
import tms.task_management_system.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskWebControllerTestIT extends Conn {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private List<Task> taskList;
    private Users user;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        user = new Users(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN", null);
        user = userRepository.save(user);

        Task task1 = new Task(1L, "Title1", "Description1", "2024-5-31", "Pending", user);
        Task task2 = new Task(2L, "Title2", "Description2", "2024-5-31", "Completed", user);
        taskList = List.of(task1, task2);
        taskRepository.saveAll(taskList);
    }

    @Test
    void testGetAllTasks() {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/tasks",
                String.class);
       
        // Then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        // Assert
        assertThat(response.getBody().contains("Title1"), is(true));
        assertThat(response.getBody().contains("Title2"), is(true));
    }

    @Test
    void testShowSaveTaskForm() {
        
        // When
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/tasks/create",
                String.class);

        // Then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        // Assert
        assertThat(response.getBody().contains("Create Task"), is(true));
    }

    @Test
    void testShowEditTaskForm() {
        // Given
        Task task = taskRepository.findAll().get(0);

        // When
        ResponseEntity<String> response = restTemplate
                .getForEntity("http://localhost:" + port + "/tasks/edit/" + task.getId(), String.class);

        // Then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        // Assert
        assertThat(response.getBody().contains("Edit Task"), is(true));
        assertThat(response.getBody().contains(task.getTitle()), is(true));
    }

    @Test
    void testDeleteTaskById() {
        // Given
        Task task = taskRepository.findAll().get(0);

        // When
        restTemplate.getForEntity("http://localhost:" + port + "/tasks/delete/" + task.getId(), String.class);

        // Then
        try {
            taskRepository.findById(task.getId()).orElseThrow();
        } catch (HttpClientErrorException e) {
            // Assert
            assertThat(e.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        }
    }
}
