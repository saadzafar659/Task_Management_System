package tms.task_management_system.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import tms.task_management_system.entity.Task;
import tms.task_management_system.repository.TaskRepository;

@SpringBootTest
class TaskServiceTest {

	@MockBean
	private TaskRepository taskRepository;

	@Autowired
	private TaskService taskService;

	private List<Task> taskList;

	@BeforeEach
	public void setUp() {
		Task task1 = new Task(1L, "Title1", "Description1", "2024-12-31", "Pending", null);
		Task task2 = new Task(2L, "Title2", "Description2", "2024-12-31", "Completed", null);
		taskList = Arrays.asList(task1, task2);
	}

	@Test
	void testGetAllTasks() {
		// Given
		when(taskRepository.findAll()).thenReturn(taskList);

		// When
		List<Task> result = taskService.getAllTasks();

		// Then
		// assert
		assertEquals(2, result.size());
		assertEquals("Title1", result.get(0).getTitle());
		assertEquals("Title2", result.get(1).getTitle());
	}

	@Test
	void testGetTaskById() {
		// Given
		Long taskId = 1L;
		when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskList.get(0)));

		// When
		Optional<Task> result = taskService.getTaskById(taskId);

		// Then
		// assert
		assertEquals("Title1", result.get().getTitle());
	}
	
	 @Test
	    void testSaveTask() {
	        // Given
	        Task task = new Task(1L, "Title1", "Description1", "2024-12-31", "Pending", null);

	        Task savedTask = new Task(1L, "Title1", "Description1", "2024-12-31", "Pending", null);
	        
	        when(taskRepository.save(task)).thenReturn(savedTask);

	        // When
	        Task result = taskService.saveTask(task);

	        // Then
	        // assert
	        assertEquals(savedTask.getId(), result.getId());
	        assertEquals(savedTask.getTitle(), result.getTitle());
	        verify(taskRepository).save(task);
	    }

}
