package nz.ac.aut.comp713.tasktracker;

public class Task {
    private final long id;
    private final String title;
    private final String status;
    private final long projectId;

    public Task(long id, String title, String status, long projectId) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.projectId = projectId;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public long getProjectId() {
        return projectId;
    }
}