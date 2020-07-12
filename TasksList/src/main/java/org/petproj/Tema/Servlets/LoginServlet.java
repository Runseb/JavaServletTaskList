package org.petproj.Tema.Servlets;

import org.petproj.Tema.DB.OtherCommands;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    /**
     * <code>doPost</code> method for login user in app.
     * If parameter SaveSession = true than current JSESSIONID save in Cookies.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pat = "^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@" +
                "(?:[a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*(?:aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|" +
                "mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z])$";
        String mail = request.getParameter("mail").toLowerCase();
        String password = request.getParameter("password");
        String saveSession = request.getParameter("SaveSession");
        OtherCommands.createUsersTable();
        //noinspection ConstantConditions
        if (mail != null && password != null && !mail.equals("") && !password.equals("") &&
                Pattern.matches(pat, mail)) {
            int user_id = OtherCommands.checkUser(mail, password);
            if (user_id != 0) {
                HttpSession sesClose = request.getSession();
                sesClose.invalidate();
                HttpSession session = request.getSession();
                OtherCommands.updateSessionId(user_id, session.getId());
                if (saveSession != null && saveSession.equals("true")) {
                    //save session in Cookies
                    Cookie cookie = new Cookie("JSESSIONID", session.getId());
                    cookie.setMaxAge(60 * 60 * 24 * 365);
                    session.setMaxInactiveInterval(60 * 60 * 24);
                    response.addCookie(cookie);
                }
                response.sendRedirect(request.getContextPath() + "/tasks");
            } else response.sendRedirect(request.getContextPath());
        } else response.sendRedirect(request.getContextPath());
    }
    /**
     * <code>doDelete</code> method for invalidate current user session
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = OtherCommands.checkUserSessionId(session.getId());
        if (userId != 0) {
            OtherCommands.updateSessionId(userId, null);
        }
        session.invalidate();
        getServletContext().getRequestDispatcher("/index.html").forward(request, response);
    }
}