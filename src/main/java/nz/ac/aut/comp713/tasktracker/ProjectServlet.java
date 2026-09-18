package nz.ac.aut.comp713.tasktracker;

import java.io.IOException;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/projects/*")
public class ProjectServlet extends HttpServlet {

    @Inject
    private ProjectService projectService;

    @Inject
    private TaskService taskService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        String pathInfo = request.getPathInfo(); // null, "/", "/5", or "/5/tasks"

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                writeProjectList(response, projectService.findAllProjects());
                return;
            }

            String[] parts = pathInfo.substring(1).split("/");

            if (parts.length == 2 && parts[1].equals("tasks")) {
                long projectId = Long.parseLong(parts[0]);
                writeTaskList(response, taskService.findTasksForProject(projectId));
                return;
            }

            writeError(response, HttpServletResponse.SC_NOT_FOUND, "Unknown route.");

        } catch (NumberFormatException e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Project id must be a number.");
        } catch (ProjectNotFoundException e) {
            writeError(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
        @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        String pathInfo = request.getPathInfo();

        try (JsonReader reader = Json.createReader(request.getReader())) {
            JsonObject input = reader.readObject();

            if (pathInfo == null || pathInfo.equals("/")) {
                String name = input.getString("name", null);
                Project created = projectService.createProject(name);
                writeProject(response, created, HttpServletResponse.SC_CREATED);
                return;
            }

            String[] parts = pathInfo.substring(1).split("/");
            if (parts.length == 2 && parts[1].equals("tasks")) {
                long projectId = Long.parseLong(parts[0]);
                String title = input.getString("title", null);
                Task created = taskService.createTask(projectId, title);
                writeTask(response, created, HttpServletResponse.SC_CREATED);
                return;
            }

            writeError(response, HttpServletResponse.SC_NOT_FOUND, "Unknown route.");

        } catch (JsonException e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Request body must contain valid JSON.");
        } catch (NumberFormatException e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Project id must be a number.");
        } catch (IllegalArgumentException e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (ProjectNotFoundException e) {
            writeError(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
        private void writeProjectList(HttpServletResponse response, List<Project> projects) throws IOException {
        JsonArrayBuilder array = Json.createArrayBuilder();
        for (Project project : projects) {
            array.add(toJson(project));
        }
        response.setStatus(HttpServletResponse.SC_OK);
        try (JsonWriter writer = Json.createWriter(response.getWriter())) {
            writer.writeArray(array.build());
        }
    }

    private void writeProject(HttpServletResponse response, Project project, int status) throws IOException {
        response.setStatus(status);
        try (JsonWriter writer = Json.createWriter(response.getWriter())) {
            writer.writeObject(toJson(project));
        }
    }

    private void writeTaskList(HttpServletResponse response, List<Task> tasks) throws IOException {
        JsonArrayBuilder array = Json.createArrayBuilder();
        for (Task task : tasks) {
            array.add(toJson(task));
        }
        response.setStatus(HttpServletResponse.SC_OK);
        try (JsonWriter writer = Json.createWriter(response.getWriter())) {
            writer.writeArray(array.build());
        }
    }

    private void writeTask(HttpServletResponse response, Task task, int status) throws IOException {
        response.setStatus(status);
        try (JsonWriter writer = Json.createWriter(response.getWriter())) {
            writer.writeObject(toJson(task));
        }
    }

    private JsonObject toJson(Project project) {
        return Json.createObjectBuilder()
                .add("id", project.getId())
                .add("name", project.getName())
                .build();
    }

    private JsonObject toJson(Task task) {
        return Json.createObjectBuilder()
                .add("id", task.getId())
                .add("title", task.getTitle())
                .add("status", task.getStatus())
                .add("projectId", task.getProjectId())
                .build();
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        JsonObject error = Json.createObjectBuilder().add("error", message).build();
        try (JsonWriter writer = Json.createWriter(response.getWriter())) {
            writer.writeObject(error);
        }
    }

}