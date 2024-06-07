package tms.task_management_system.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tms.task_management_system.dto.TaskDTO;
import tms.task_management_system.entity.Task;
import tms.task_management_system.service.TaskService;
import tms.task_management_system.service.UserService;

@Controller
public class TaskWebController {

    private static final String REDIRECT_TASKS = "redirect:/tasks";
    private static final String FLASH_MESSAGE_KEY = "message";

    private final TaskService taskService;
    private final UserService userService;

    public TaskWebController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public String getAllTasks(Model model) {
        List<Task> taskList = taskService.getAllTasks();
        model.addAttribute("tasks", taskList);
        return "allTasks";
    }

    @GetMapping("/tasks/create")
    public String showSaveTaskForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "saveTask";
    }

    @PostMapping("/tasks/create")
    public String saveTask(@RequestParam String title, @RequestParam String description, @RequestParam String deadline,
                           @RequestParam String status, @RequestParam Long userId, RedirectAttributes redirectAttributes) {
        TaskDTO taskDTO = new TaskDTO(null, title, description, deadline, status, userId);
        taskService.saveTask(taskDTO.toTask());
        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_KEY, "Task created successfully!");
        return REDIRECT_TASKS;
    }

    @GetMapping("/tasks/edit/{id}")
    public String showEditTaskForm(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id).orElse(null);
        model.addAttribute("task", task);
        model.addAttribute("users", userService.getAllUsers());
        return "editTask";
    }

    @PostMapping("/tasks/edit/{id}")
    public String updateTask(@PathVariable Long id, @RequestParam String title, @RequestParam String description,
                             @RequestParam String deadline, @RequestParam String status, @RequestParam Long userId,
                             RedirectAttributes redirectAttributes) {
        TaskDTO taskDTO = new TaskDTO(id, title, description, deadline, status, userId);
        taskService.saveTask(taskDTO.toTask());
        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_KEY, "Task updated successfully!");
        return REDIRECT_TASKS;
    }
    
    @GetMapping("/tasks/delete/{id}")
    public String deleteTaskById(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        taskService.deleteTaskById(id);
        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_KEY, "Task deleted successfully!");
        return REDIRECT_TASKS;
    }
}
