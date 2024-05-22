package tms.task_management_system.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import tms.task_management_system.entity.Users;
import tms.task_management_system.repository.UserRepository;

@SpringBootTest
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private List<Users> userList;

    @BeforeEach
    public void setUp() {
        Users user1 = new Users(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN", null);
        Users user2 = new Users(2L, "Saad Khan", "skhan@gmail.com", "password2", "USER", null);
        userList = Arrays.asList(user1, user2);
    }

    @Test
    void testGetAllUsers() {
        // Given
        when(userRepository.findAll()).thenReturn(userList);

        // When
        List<Users> result = userService.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals("Saad Zafar", result.get(0).getName());
        assertEquals("Saad Khan", result.get(1).getName());
    }
}
