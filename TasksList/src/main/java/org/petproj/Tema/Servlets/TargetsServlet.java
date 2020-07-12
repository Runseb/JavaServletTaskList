package org.petproj.Tema.Servlets;

import com.google.gson.Gson;
import org.petproj.Tema.DB.OtherCommands;
import org.petproj.Tema.DB.TargetsCommands;
import org.petproj.Tema.Structure.Target;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/targets")
public class TargetsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        int userId = (Integer) request.getAttribute("userId");
        List<Target> list = TargetsCommands.getTargets(userId);
        Gson gson = new Gson();
        request.setAttribute("targets", gson.toJson(list));
        getServletContext().getRequestDispatcher("/targets.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        int userId = (Integer) request.getAttribute("userId");
        String description = request.getParameter("description");
        if (description == null || description.equals("")) {
            request.setAttribute("error", "You input invalid target description!");
        } else TargetsCommands.addTarget(userId, description);
        doGet(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        int userId = (Integer) request.getAttribute("userId");
        try {
            int targetId = Integer.parseInt(request.getParameter("targetId"));
            String description = request.getParameter("description");
            String mainTargetId = request.getParameter("mainTargetId");
            Target target = TargetsCommands.getTarget(userId, targetId);
            if (description != null) target.setDescription(description);
            if (mainTargetId != null) {
                target.setMainTargetId(Integer.parseInt(mainTargetId));
            }
            if (description != null && !description.equals("") || mainTargetId != null) {
                int result = TargetsCommands.updateTarget(userId, target);
                switch (result) {
                    case 0:
                        request.setAttribute("info", "Target was successfully updated");
                        break;
                    case 1:
                        request.setAttribute("info", "Target ID can't be equals main target ID");
                        break;
                    case 2:
                        request.setAttribute("error", "Target №" + targetId +
                                " can't be a sub target of target №" + mainTargetId + " because target №"
                                + mainTargetId + " is sub target of target №" + targetId);
                        break;
                    case 3:
                        request.setAttribute("error", "Target №" + mainTargetId + "not exist");
                        break;
                }
            } else request.setAttribute("info", "Target didn't update");
        } catch (NumberFormatException | NullPointerException e) {
            request.setAttribute("error", "Invalid target or main target ID");
        }
        doGet(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        int userId = (Integer) request.getAttribute("userId");
        try {
            TargetsCommands.deleteTarget(userId, Integer.parseInt(request.getParameter("targetId")));
        } catch (NumberFormatException | NullPointerException e) {
            request.setAttribute("error", "Invalid target or main target ID");
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
