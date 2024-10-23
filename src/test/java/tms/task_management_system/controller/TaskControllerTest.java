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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import tms.task_management_system.dto.TaskDTO;
import tms.task_management_system.entity.Task;
import tms.task_management_system.entity.Users;
import tms.task_management_system.service.TaskService;
import tms.task_management_system.service.UserService;

@SpringBootTest
class TaskControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@MockBean
	private TaskService taskService;

	@MockBean
	private UserService userService;

	private List<Task> taskList;
	private TaskDTO validTaskDTO;
	private Users user;
	private Task task;

	@InjectMocks
	private TaskController taskController;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

		Task task1 = new Task(1L, "Title1", "Description1", "2024-5-31", "Pending", null);
		Task task2 = new Task(2L, "Title2", "Description2", "2024-5-31", "Completed", null);
		taskList = Arrays.asList(task1, task2);

		validTaskDTO = new TaskDTO(3L, "New Task Title", "Description of the new task", "2024-5-31", "Pending", 1L);

		user = new Users(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN", null);
		task = new Task(3L, "New Task Title", "Description of the new task", "2024-5-31", "Pending", user);
	}

	@Test
	void testGetAllTasks() throws Exception {
		// Given
		when(taskService.getAllTasks()).thenReturn(taskList);

		// When
		// Get request
		mockMvc.perform(get("/api/tasks/getAll")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].title").value("Title1")).andExpect(jsonPath("$[1].id").value(2L))
				.andExpect(jsonPath("$[1].title").value("Title2"));

		// Then
		// Verify
		verify(taskService, times(1)).getAllTasks();
	}

	@Test
	void testGetTaskById() throws Exception {
		// Given
		Long taskId = 1L;
		when(taskService.getTaskById(taskId)).thenReturn(Optional.of(taskList.get(0)));

		// When
		// Get request
		mockMvc.perform(get("/api/tasks/{id}", taskId)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.title").value("Title1"));

		// Then
		// Verify
		verify(taskService, times(1)).getTaskById(taskId);
	}

	@Test
	void testGetTaskById_NotFound() throws Exception {
		// Given
		Long taskId = 1L;
		when(taskService.getTaskById(taskId)).thenReturn(java.util.Optional.empty());

		// When and Then
		// Get request
		mockMvc.perform(get("/api/tasks/{id}", taskId)).andExpect(status().isNotFound());
	}

	@Test
	void testCreateTask() throws Exception {
		// Given
		when(taskService.saveTask(any(Task.class))).thenReturn(task);

		// When and Then
		// Post request
		mockMvc.perform(post("/api/tasks/create").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(validTaskDTO))).andExpect(status().isCreated());
		// Verify
		verify(taskService, times(1)).saveTask(any(Task.class));
	}

	@Test
	void testDeleteTask() throws Exception {
		Long taskId = 1L;

		// Given
		when(taskService.getTaskById(taskId)).thenReturn(Optional.of(task));
		// When
		// Delete request
		mockMvc.perform(delete("/api/tasks/delete/{id}", taskId))
				// Then
				.andExpect(status().isNoContent());
		// Verify
		verify(taskService, times(1)).getTaskById(taskId);
		verify(taskService, times(1)).deleteTaskById(taskId);
	}

	@Test
	void testDeleteTask_NotFound() throws Exception {
		Long taskId = 1L;
		// Given
		when(taskService.getTaskById(taskId)).thenReturn(Optional.empty());
		// When
		// Delete request
		mockMvc.perform(delete("/api/tasks/delete/{id}", taskId))
				// Then
				.andExpect(status().isNotFound());
		// Verify
		verify(taskService, times(1)).getTaskById(taskId);
		verify(taskService, times(0)).deleteTaskById(taskId);
	}
}
