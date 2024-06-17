package tms.task_management_system.con;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public abstract class Conn {

    private static final MySQL5Container mysql5Container = MySQL5Container.getInstance();

    @BeforeAll
    public static void startContainer() {
        mysql5Container.start();
        try (Connection connection = DriverManager.getConnection(
                mysql5Container.getJdbcUrl(),
                mysql5Container.getUsername(),
                mysql5Container.getPassword()
        )) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS mydb;");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database", e);
        }
    }

    @AfterAll
    public static void stopContainer() {
        mysql5Container.stop();
    }
}
