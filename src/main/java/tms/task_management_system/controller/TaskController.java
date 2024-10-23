package tms.task_management_system.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tms.task_management_system.dto.TaskDTO;
import tms.task_management_system.entity.Task;
import tms.task_management_system.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping("/getAll")
	public List<Task> getAllTasks() {
		return taskService.getAllTasks();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
		Optional<Task> taskOptional = taskService.getTaskById(id);
		return taskOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/create")
	public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
		Task task = taskDTO.toTask();
		Task savedTask = taskService.saveTask(task);
		TaskDTO savedTaskDTO = new TaskDTO(savedTask.getId(), savedTask.getTitle(), savedTask.getDescription(),
				savedTask.getDeadline(), savedTask.getStatus(), savedTask.getUser().getId());
		return new ResponseEntity<>(savedTaskDTO, HttpStatus.CREATED);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		Optional<Task> taskOptional = taskService.getTaskById(id);
		if (!taskOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		taskService.deleteTaskById(id);
		return ResponseEntity.noContent().build();
	}

}
