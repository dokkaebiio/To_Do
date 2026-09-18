package nz.ac.aut.comp713.tasktracker;

public class Project {
    private final long id;
    private final String name;

    public Project(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}