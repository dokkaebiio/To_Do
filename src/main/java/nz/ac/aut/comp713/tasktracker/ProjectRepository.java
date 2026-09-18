package nz.ac.aut.comp713.tasktracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        public Optional<Project> findById(long id) {
        String sql = "SELECT id, name FROM projects WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return Optional.of(new Project(results.getLong("id"), results.getString("name")));
                }
                return Optional.empty();
            }

        } catch (SQLException error) {
            throw new IllegalStateException("The project database is temporarily unavailable.", error);
        }
    }
        public Project create(String name) {
        String sql = "INSERT INTO projects (name) VALUES (?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                long newId = keys.getLong(1);
                return new Project(newId, name);
            }

        } catch (SQLException error) {
            throw new IllegalStateException("The project database is temporarily unavailable.", error);
        }
    }

}