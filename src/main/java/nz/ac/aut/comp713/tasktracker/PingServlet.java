package nz.ac.aut.comp713.tasktracker;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Minimal servlet used only to confirm the toolchain works end to end:
 * Maven build -> WAR -> Payara deploy -> HTTP response.
 *
 * Visit http://localhost:8080/task-tracker/api/ping after deploying.
 */
@WebServlet("/api/ping")
public class PingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        JsonObject output = Json.createObjectBuilder()
                .add("status", "ok")
                .add("message", "Task Tracker backend is running")
                .build();

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(output.toString());
    }
}
