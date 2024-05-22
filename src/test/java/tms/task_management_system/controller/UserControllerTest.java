package tms.task_management_system.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import tms.task_management_system.entity.Users;
import tms.task_management_system.service.UserService;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<Users> userList;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        Users user1 = new Users(1L, "Saad Zafar", "saad@gmail.com", "password1", "ADMIN", null);
        Users user2 = new Users(2L, "Saad Khan", "skhan@gmail.com", "password2", "USER", null);
        userList = Arrays.asList(user1, user2);
    }

    @Test
    void testGetAllUsers() throws Exception {
        
    	//Given
        when(userService.getAllUsers()).thenReturn(userList);
        
        // When
        // Get request
        mockMvc.perform(get("/api/users/getAll"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].name").value("Saad Zafar"))
            .andExpect(jsonPath("$[0].email").value("saad@gmail.com"))
            .andExpect(jsonPath("$[0].role").value("ADMIN"))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].name").value("Saad Khan"))
            .andExpect(jsonPath("$[1].email").value("skhan@gmail.com"))
            .andExpect(jsonPath("$[1].role").value("USER"));
        
        //Then
        // Verify
        verify(userService, times(1)).getAllUsers();
    }
}
