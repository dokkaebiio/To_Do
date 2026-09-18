package nz.ac.aut.comp713.tasktracker;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(long taskId) {
        super("Task " + taskId + " does not exist.");
    }
}