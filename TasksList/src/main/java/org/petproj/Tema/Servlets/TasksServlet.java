package org.petproj.Tema.Servlets;

import com.google.gson.Gson;
import org.petproj.Tema.DB.OtherCommands;
import org.petproj.Tema.DB.TargetsCommands;
import org.petproj.Tema.DB.TasksCommands;
import org.petproj.Tema.Structure.Task;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/tasks")
public class TasksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (Integer) request.getAttribute("userId");
        String[] taskId = request.getParameterValues("taskId");
        Gson gson = new Gson();
        List<Task> tasks = new ArrayList<>();
        try {
            if (taskId != null && request.getMethod().equals("GET")) {
                for (String task : taskId) {
                    tasks.add(TasksCommands.getTask(userId, Integer.parseInt(task)));
                }
            } else {
                tasks = TasksCommands.getTasks(userId);
                request.setAttribute("tasks", gson.toJson(tasks));
            }
        } catch (NumberFormatException | NullPointerException e) {
            request.setAttribute("error", "Invalid taskID");
        }
        getServletContext().getRequestDispatcher("/tasks.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int userId = (Integer) request.getAttribute("userId");
            String description = request.getParameter("description");
            String text = request.getParameter("text");
            LocalDate deadline = LocalDate.parse(request.getParameter("deadline"));
            boolean state = request.getParameter("state") != null && request.getParameter("state").equals("ready");
            if (description != null && !description.equals("") && text != null && !text.equals("")) {
                TasksCommands.addTask(userId, description, text, deadline, state);
            } else request.setAttribute("info", "Enter a description and text to add the task");
        } catch (DateTimeParseException | NullPointerException e) {
            request.setAttribute("error", "Invalid date format");
        }
        doGet(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (Integer) request.getAttribute("userId");
        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            String text = request.getParameter("text");
            String description = request.getParameter("description");
            String deadline = request.getParameter("deadline");
            String state = request.getParameter("state");
            String targetId = request.getParameter("targetId");
            Task task = TasksCommands.getTask(userId, taskId);
            if (task != null) {
                if (text != null && !text.equals("")) task.setText(text);
                if (description != null && !description.equals("")) task.setDescription(description);
                if (deadline != null && !deadline.equals("")) task.setDeadline(LocalDate.parse(deadline));
                if (state != null && !state.equals("")) task.setState(state.equals("ready"));
                if (targetId != null && !targetId.equals("")) {
                    int tarId = Integer.parseInt(targetId);
                    if (TargetsCommands.getTarget(userId, tarId) != null || tarId == 0) {
                        task.setTarget(tarId);
                    } else request.setAttribute("info", "Target with ID " + tarId + " isn't exist");
                }
                TasksCommands.updateTask(userId, task);
            } else request.setAttribute("info", "Task with ID " + taskId + " isn't exist");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid taskID or targetID");
        } catch (DateTimeParseException | NullPointerException e) {
            request.setAttribute("error", "Invalid date format");
        }
        doGet(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (Integer) request.getAttribute("userId");
        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            if (TasksCommands.deleteTask(userId, taskId)) {
                request.setAttribute("info", "Task " + taskId + " successfully deleted");
            } else request.setAttribute("info", "Task with ID " + taskId + " isn't exist");
        } catch (NumberFormatException | NullPointerException e) {
            request.setAttribute("error", "Invalid taskID");
        }
        doGet(request, response);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        HttpSession session = request.getSession();
        int userId = OtherCommands.checkUserSessionId(session.getId());
        if (userId != 0) {
            request.setAttribute("userId", userId);
            super.service(request, response);
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("JSESSIONID")) {
                        userId = OtherCommands.checkUserSessionId(cookie.getValue());
                        if (userId != 0) {
                            OtherCommands.updateSessionId(userId, session.getId());
                            request.setAttribute("userId", userId);
                            super.service(request, response);
                        } else getServletContext().getRequestDispatcher("/index.html").forward(request, response);
                    }
                }
            } else getServletContext().getRequestDispatcher("/index.html").forward(request, response);
        }
    }
}