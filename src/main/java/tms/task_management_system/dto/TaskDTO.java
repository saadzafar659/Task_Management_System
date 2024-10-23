package tms.task_management_system.dto;

import tms.task_management_system.entity.Task;
import tms.task_management_system.entity.Users;

public class TaskDTO {

	private Long id;
	private String title;
	private String description;
	private String deadline;
	private String status;
	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Task toTask() {
		Task task = new Task();
		task.setId(this.id);
		task.setTitle(this.title);
		task.setDescription(this.description);
		task.setDeadline(this.deadline);
		task.setStatus(this.status);

		Users user = new Users();
		user.setId(this.userId);
		task.setUser(user);

		return task;
	}

	public TaskDTO(Long id, String title, String description, String deadline, String status, Long userId) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.deadline = deadline;
		this.status = status;
		this.userId = userId;
	}

}
