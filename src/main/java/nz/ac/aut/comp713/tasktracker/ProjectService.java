package nz.ac.aut.comp713.tasktracker;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProjectService {

    @Inject
    private ProjectRepository projectRepository;

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(String rawName) {
        String name = normaliseName(rawName);
        return projectRepository.create(name);
    }

    public Project getProjectOrThrow(long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
    }

    private String normaliseName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name is required.");
        }
        String trimmed = name.trim();
        if (trimmed.length() > 100) {
            throw new IllegalArgumentException("Project name must be 100 characters or fewer.");
        }
        return trimmed;
    }
}