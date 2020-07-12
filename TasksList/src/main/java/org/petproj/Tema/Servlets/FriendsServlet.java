package org.petproj.Tema.Servlets;

import com.google.gson.Gson;
import org.petproj.Tema.DB.OtherCommands;
import org.petproj.Tema.DB.FriendsCommands;
import org.petproj.Tema.Structure.Friend;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/friends")
public class FriendsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (Integer) request.getAttribute("userId");
        List<Friend> friendList = FriendsCommands.getFriends(userId);
        Gson gson = new Gson();
        request.setAttribute("friends", gson.toJson(friendList));
        getServletContext().getRequestDispatcher("/friends.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (Integer) request.getAttribute("userId");
        String mail = request.getParameter("friendMail").toLowerCase();
        //noinspection ConstantConditions
        if (mail == null || mail.equals("") || !FriendsCommands.addFriend(userId, mail)) {
            request.setAttribute("error", "Invalid email");
        }
        doGet(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (Integer) request.getAttribute("userId");
        try {
            int friendId = Integer.parseInt(request.getParameter("friendId"));
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            FriendsCommands.sendTaskToFriend(userId, taskId, friendId);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "You input invalid friendId or taskId");
        }
        doGet(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (Integer) request.getAttribute("userId");
        try {
            int friendId = Integer.parseInt(request.getParameter("friendId"));
            if (FriendsCommands.deleteFriend(userId, friendId)) {
                request.setAttribute("info", "Friend with ID " + friendId + " successfully deleted");
            } else request.setAttribute("info", "Friend with ID " + friendId + " isn't exist");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid friend ID");
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