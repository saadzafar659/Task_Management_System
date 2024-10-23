package tms.task_management_system.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.github.bonigarcia.wdm.WebDriverManager;
import tms.task_management_system.con.Conn;
import tms.task_management_system.entity.Task;
import tms.task_management_system.entity.Users;
import tms.task_management_system.repository.TaskRepository;
import tms.task_management_system.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestTaskWebControllerE2E extends Conn {

	@LocalServerPort
	private int port;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private UserRepository userRepository;

	private WebDriver driver;
	private Users user;

	@BeforeEach
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		driver = new ChromeDriver(options);

		taskRepository.deleteAll();
		userRepository.deleteAll();

		user = new Users(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN", null);
		user = userRepository.save(user);

		Task task1 = new Task(1L, "Title1", "Description1", "2024-5-31", "Pending", user);
		Task task2 = new Task(2L, "Title2", "Description2", "2024-5-31", "Completed", user);
		List<Task> taskList = List.of(task1, task2);
		taskRepository.saveAll(taskList);
	}

	@AfterEach
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	void testGetAllTasks() {
		// When
		driver.get("http://localhost:" + port + "/tasks");
		// Then
		List<WebElement> tasks = driver.findElements(By.className("task-title"));
		// Assert
		assertEquals(2, tasks.size());
		assertEquals("Title1", tasks.get(0).getText());
		assertEquals("Title2", tasks.get(1).getText());
	}

	@Test
	void testShowSaveTaskForm() {
		// When
		driver.get("http://localhost:" + port + "/tasks/create");
		// Then
		WebElement formHeader = driver.findElement(By.tagName("h1"));
		// Assert
		assertEquals("Create Task", formHeader.getText());
	}

	@Test
	void testSaveTask() {
		// When
		driver.get("http://localhost:" + port + "/tasks/create");

		driver.findElement(By.name("title")).sendKeys("New Task");
		driver.findElement(By.name("description")).sendKeys("New Description");
		driver.findElement(By.name("deadline")).sendKeys("2024-12-31");
		driver.findElement(By.name("status")).sendKeys("Pending");

		Select userSelect = new Select(driver.findElement(By.name("userId")));
		userSelect.selectByVisibleText("Saad Zafar");

		driver.findElement(By.name("submit")).click();
		// Then
		List<Task> tasks = taskRepository.findAll();
		// Assert
		assertEquals(3, tasks.size());
		assertEquals("New Task", tasks.get(2).getTitle());
	}

	@Test
	void testShowEditTaskForm() {
		// When
		Task task = taskRepository.findAll().get(0);
		driver.get("http://localhost:" + port + "/tasks/edit/" + task.getId());
		// Then
		WebElement formHeader = driver.findElement(By.tagName("h1"));
		// Assert
		assertEquals("Edit Task", formHeader.getText());
	}

	@Test
	void testUpdateTask() {
		// When
		Task task = taskRepository.findAll().get(0);
		driver.get("http://localhost:" + port + "/tasks/edit/" + task.getId());
		// Then
		WebElement titleField = driver.findElement(By.name("title"));
		titleField.clear();
		titleField.sendKeys("Updated Task");
		driver.findElement(By.name("submit")).click();
		Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
		// Assert
		assertEquals("Updated Task", updatedTask.getTitle());
	}

}
