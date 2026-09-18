package nz.ac.aut.comp713.tasktracker;

import java.io.IOException;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/tasks/*")
public class TaskServlet extends HttpServlet {

    @Inject
    private TaskService taskService;

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        try {
            long taskId = extractId(request);

            try (JsonReader reader = Json.createReader(request.getReader())) {
                JsonObject input = reader.readObject();
                String status = input.getString("status", null);

                Task updated = taskService.updateStatus(taskId, status);

                response.setStatus(HttpServletResponse.SC_OK);
                try (JsonWriter writer = Json.createWriter(response.getWriter())) {
                    writer.writeObject(toJson(updated));
                }
            }

        } catch (IllegalStateException e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (NumberFormatException e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Task id must be a number.");
        } catch (JsonException e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Request body must contain valid JSON.");
        } catch (IllegalArgumentException e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (TaskNotFoundException e) {
            writeError(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
        @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        try {
            long taskId = extractId(request);
            taskService.deleteTask(taskId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (IllegalStateException e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (NumberFormatException e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Task id must be a number.");
        } catch (TaskNotFoundException e) {
            writeError(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    private long extractId(HttpServletRequest request) {
        String pathInfo = request.getPathInfo(); // expected "/{id}"
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new IllegalStateException("Task id is required in the URL, e.g. /api/tasks/5");
        }
        return Long.parseLong(pathInfo.substring(1));
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