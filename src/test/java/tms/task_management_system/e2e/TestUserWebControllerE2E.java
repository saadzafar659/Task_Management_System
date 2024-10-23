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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.github.bonigarcia.wdm.WebDriverManager;
import tms.task_management_system.con.Conn;
import tms.task_management_system.entity.Users;
import tms.task_management_system.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestUserWebControllerE2E extends Conn {

	@LocalServerPort
	private int port;

	@Autowired
	private UserRepository userRepository;

	private WebDriver driver;

	@BeforeEach
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		driver = new ChromeDriver(options);

		userRepository.deleteAll();

		Users user1 = new Users(1L, "Test User 1", "testuser1@gmail.com", "password1", "ADMIN", null);
		Users user2 = new Users(2L, "Test User 2", "testuser2@gmail.com", "password2", "USER", null);
		userRepository.saveAll(List.of(user1, user2));
	}

	@AfterEach
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	void testGetAllUsers() {
		// When
		driver.get("http://localhost:" + port + "/users");
		// Then
		List<WebElement> users = driver.findElements(By.tagName("tr"));
		// Assert
		assertEquals(3, users.size());
	}

	@Test
	void testShowSaveUserForm() {
		// When
		driver.get("http://localhost:" + port + "/users/create");
		// Then
		WebElement formHeader = driver.findElement(By.tagName("h1"));
		// Assert
		assertEquals("Create User", formHeader.getText());
	}

	@Test
	void testSaveUser() {
		// When
		driver.get("http://localhost:" + port + "/users/create");

		driver.findElement(By.id("name")).sendKeys("New User");
		driver.findElement(By.id("email")).sendKeys("newuser@gmail.com");
		driver.findElement(By.id("password")).sendKeys("newpassword");
		driver.findElement(By.id("role")).sendKeys("ADMIN");

		driver.findElement(By.cssSelector("input[type='submit']")).click();
		// Then
		List<Users> users = userRepository.findAll();
		// Assert
		assertEquals(3, users.size());
		assertEquals("New User", users.get(2).getName());
	}

	@Test
	void testShowEditUserForm() {
		// When
		Users user = userRepository.findAll().get(0);
		driver.get("http://localhost:" + port + "/users/edit/" + user.getId());
		// Then
		WebElement formHeader = driver.findElement(By.tagName("h1"));
		// Assert
		assertEquals("Edit User", formHeader.getText());
		assertEquals(user.getName(), driver.findElement(By.id("name")).getAttribute("value"));
	}

	@Test
	void testDeleteUserById() {
		// When
		Users user = userRepository.findAll().get(0);
		driver.get("http://localhost:" + port + "/users/delete/" + user.getId());
		// Then
		List<Users> users = userRepository.findAll();
		// Assert
		assertEquals(1, users.size());
	}
}
