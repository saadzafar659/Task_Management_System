package tms.task_management_system.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import tms.task_management_system.dto.TaskDTO;
import tms.task_management_system.entity.Task;
import tms.task_management_system.service.TaskService;
import tms.task_management_system.service.UserService;

@WebMvcTest(TaskWebController.class)
class TaskWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private TaskDTO taskDTO;
    private Task task;
    private List<Task> taskList;

    @Captor
    private ArgumentCaptor<Task> taskCaptor;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        taskDTO = new TaskDTO(1L, "Sample Task", "Sample Description", "2024-06-05", "IN_PROGRESS", 1L);
        task = new Task(1L, "Sample Task", "Sample Description", "2024-06-05", "IN_PROGRESS", null);

        Task task1 = new Task(1L, "Sample Task 1", "Sample Description 1", "2024-06-05", "IN_PROGRESS", null);
        Task task2 = new Task(2L, "Sample Task 2", "Sample Description 2", "2024-06-06", "DONE", null);
        taskList = Arrays.asList(task1, task2);
    }

    @Test
    void testGetAllTasks() throws Exception {
        // Given
        given(taskService.getAllTasks()).willReturn(taskList);

        // When
        // Then
        // GET request
        mockMvc.perform(get("/tasks")).andExpect(status().isOk()).andExpect(view().name("allTasks"))
                .andExpect(model().attributeExists("tasks")).andExpect(model().attribute("tasks", taskList));
    }

    @Test
    void testShowSaveTaskForm() throws Exception {
        
        // When
        // Then
        // GET request
        mockMvc.perform(get("/tasks/create")).andExpect(status().isOk()).andExpect(view().name("saveTask"));
    }

    @Test
    void testSaveTask() throws Exception {
        // Given
        given(userService.getAllUsers()).willReturn(Arrays.asList());

        // When
        // Then
        // POST request
        mockMvc.perform(post("/tasks/create").param("title", taskDTO.getTitle())
                .param("description", taskDTO.getDescription()).param("deadline", taskDTO.getDeadline())
                .param("status", taskDTO.getStatus()).param("userId", String.valueOf(taskDTO.getUserId())))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/tasks"))
                .andExpect(flash().attribute("message", "Task created successfully!"));

        // Verify
        verify(taskService, times(1)).saveTask(taskCaptor.capture());
        Task capturedTask = taskCaptor.getValue();
        assertEquals(taskDTO.getTitle(), capturedTask.getTitle());
        assertEquals(taskDTO.getDescription(), capturedTask.getDescription());
        assertEquals(taskDTO.getDeadline(), capturedTask.getDeadline());
        assertEquals(taskDTO.getStatus(), capturedTask.getStatus());
    }

    @Test
    void testShowEditTaskForm() throws Exception {
        // Given
        given(taskService.getTaskById(1L)).willReturn(Optional.of(task));

        // When
        // Then
        // GET request
        mockMvc.perform(get("/tasks/edit/1")).andExpect(status().isOk()).andExpect(view().name("editTask"))
                .andExpect(model().attributeExists("task")).andExpect(model().attribute("task", task));
    }

    @Test
    void testUpdateTask() throws Exception {
        // Given
        given(userService.getAllUsers()).willReturn(Arrays.asList());

        // When
        // Then
        // POST request
        mockMvc.perform(post("/tasks/edit/1").param("title", taskDTO.getTitle())
                .param("description", taskDTO.getDescription()).param("deadline", taskDTO.getDeadline())
                .param("status", taskDTO.getStatus()).param("userId", String.valueOf(taskDTO.getUserId())))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/tasks"))
                .andExpect(flash().attribute("message", "Task updated successfully!"));

        // Verify
        verify(taskService, times(1)).saveTask(taskCaptor.capture());
        Task capturedTask = taskCaptor.getValue();
        assertEquals(task.getId(), capturedTask.getId());
        assertEquals(taskDTO.getTitle(), capturedTask.getTitle());
        assertEquals(taskDTO.getDescription(), capturedTask.getDescription());
        assertEquals(taskDTO.getDeadline(), capturedTask.getDeadline());
        assertEquals(taskDTO.getStatus(), capturedTask.getStatus());
    }

    @Test
    void testDeleteTaskById() throws Exception {
        // When
        // Then
        // DELETE request
        mockMvc.perform(get("/tasks/delete/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks")).andExpect(flash().attribute("message", "Task deleted successfully!"));

        // Then
        verify(taskService, times(1)).deleteTaskById(1L);
    }
}
