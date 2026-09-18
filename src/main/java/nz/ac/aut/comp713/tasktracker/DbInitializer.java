package nz.ac.aut.comp713.tasktracker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DbInitializer implements ServletContextListener {

    @Resource(lookup = "jdbc/TaskTrackerPool")
    private DataSource dataSource;

    private static final String CREATE_PROJECTS_TABLE = """
            CREATE TABLE IF NOT EXISTS projects (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(100) NOT NULL
            )
            """;

    private static final String CREATE_TASKS_TABLE = """
            CREATE TABLE IF NOT EXISTS tasks (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                title VARCHAR(200) NOT NULL,
                status VARCHAR(20) NOT NULL DEFAULT 'todo',
                project_id BIGINT NOT NULL,
                FOREIGN KEY (project_id) REFERENCES projects(id)
            )
            """;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(CREATE_PROJECTS_TABLE);
            statement.execute(CREATE_TASKS_TABLE);

            System.out.println("DbInitializer: projects and tasks tables are ready.");
        } catch (SQLException error) {
            System.out.println("DbInitializer: failed to initialise database tables.");
            error.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // Nothing to clean up on shutdown.
    }
}