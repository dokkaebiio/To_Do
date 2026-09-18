package nz.ac.aut.comp713.tasktracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProjectRepository {

    @Resource(lookup = "jdbc/TaskTrackerPool")
    private DataSource dataSource;

        public List<Project> findAll() {
        String sql = "SELECT id, name FROM projects ORDER BY id";
        List<Project> projects = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                projects.add(new Project(results.getLong("id"), results.getString("name")));
            }
            return projects;

        } catch (SQLException error) {
            throw new IllegalStateException("The project database is temporarily unavailable.", error);
        }
    }

}