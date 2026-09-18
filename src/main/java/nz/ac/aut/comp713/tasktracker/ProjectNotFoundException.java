package nz.ac.aut.comp713.tasktracker;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(long projectId) {
        super("Project " + projectId + " does not exist.");
    }
}