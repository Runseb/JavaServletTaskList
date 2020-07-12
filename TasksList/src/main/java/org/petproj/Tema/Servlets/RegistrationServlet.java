package org.petproj.Tema.Servlets;

import org.petproj.Tema.DB.OtherCommands;
import org.petproj.Tema.Structure.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    /**
     * <code>doPost</code> method for registration new user in app.
     * Creating basic structure in BD.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String pat = "^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@" +
                "(?:[a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*(?:aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|" +
                "mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z])$";
        String userName = request.getParameter("userName");
        String mail = request.getParameter("mail").toLowerCase();
        String password = request.getParameter("password");
        OtherCommands.createUsersTable();
        //noinspection ConstantConditions
        if (mail == null || password == null || userName == null || mail.equals("") || password.equals("") ||
                userName.equals("") || !Pattern.matches(pat, mail) || OtherCommands.checkUserContains(mail)) {
            String path = request.getContextPath() + "/";
            response.sendRedirect(path);
        } else {
            int userId = OtherCommands.addUser(new User(userName, mail, password));
            HttpSession sesClose = request.getSession();
            sesClose.invalidate();
            HttpSession session = request.getSession();
            OtherCommands.updateSessionId(userId, session.getId());
            response.sendRedirect(request.getContextPath() + "/tasks");
        }
    }
}