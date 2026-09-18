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
public class TaskRepository {

    @Resource(lookup = "jdbc/TaskTrackerPool")
    private DataSource dataSource;

    public List<Task> findByProjectId(long projectId) {
        String sql = "SELECT id, title, status, project_id FROM tasks WHERE project_id = ? ORDER BY id";
        List<Task> tasks = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, projectId);

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    tasks.add(mapRow(results));
                }
            }
            return tasks;

        } catch (SQLException error) {
            throw new IllegalStateException("The task database is temporarily unavailable.", error);
        }
    }

    private Task mapRow(ResultSet results) throws SQLException {
        return new Task(
                results.getLong("id"),
                results.getString("title"),
                results.getString("status"),
                results.getLong("project_id")
        );
    }
        public Optional<Task> findById(long id) {
        String sql = "SELECT id, title, status, project_id FROM tasks WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return Optional.of(mapRow(results));
                }
                return Optional.empty();
            }

        } catch (SQLException error) {
            throw new IllegalStateException("The task database is temporarily unavailable.", error);
        }
    }
        public Task create(String title, long projectId) {
        String sql = "INSERT INTO tasks (title, status, project_id) VALUES (?, 'todo', ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, title);
            statement.setLong(2, projectId);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                long newId = keys.getLong(1);
                return new Task(newId, title, "todo", projectId);
            }

        } catch (SQLException error) {
            throw new IllegalStateException("The task database is temporarily unavailable.", error);
        }
    }
        public boolean updateStatus(long id, String status) {
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setLong(2, id);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException error) {
            throw new IllegalStateException("The task database is temporarily unavailable.", error);
        }
    }
        public boolean delete(long id) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException error) {
            throw new IllegalStateException("The task database is temporarily unavailable.", error);
        }
    }
}