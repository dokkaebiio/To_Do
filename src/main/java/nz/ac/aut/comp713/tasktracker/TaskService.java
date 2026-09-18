package nz.ac.aut.comp713.tasktracker;

import java.util.List;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TaskService {

    private static final Set<String> VALID_STATUSES = Set.of("todo", "in-progress", "done");

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private ProjectRepository projectRepository;

    public List<Task> findTasksForProject(long projectId) {
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return taskRepository.findByProjectId(projectId);
    }

    public Task createTask(long projectId, String rawTitle) {
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        String title = normaliseTitle(rawTitle);
        return taskRepository.create(title, projectId);
    }

    public Task updateStatus(long taskId, String rawStatus) {
        String status = normaliseStatus(rawStatus);

        boolean updated = taskRepository.updateStatus(taskId, status);
        if (!updated) {
            throw new TaskNotFoundException(taskId);
        }

        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    public void deleteTask(long taskId) {
        boolean deleted = taskRepository.delete(taskId);
        if (!deleted) {
            throw new TaskNotFoundException(taskId);
        }
    }

    private String normaliseTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title is required.");
        }
        String trimmed = title.trim();
        if (trimmed.length() > 200) {
            throw new IllegalArgumentException("Task title must be 200 characters or fewer.");
        }
        return trimmed;
    }

    private String normaliseStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Task status is required.");
        }
        String normalised = status.trim().toLowerCase();
        if (!VALID_STATUSES.contains(normalised)) {
            throw new IllegalArgumentException(
                    "Status must be one of: todo, in-progress, done."
            );
        }
        return normalised;
    }
}