package controller.account;

import dao.AccountDAO;
import model.Account;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ReadAccountServlet", urlPatterns = {"/readAccount"})
public class ReadAccountServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get session and check if the account is logged in
        HttpSession session = request.getSession(false); // false means don't create a new session
        if (session == null || session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp"); // Redirect to login page if session does not exist or account is not found
            return;
        }

        // Retrieve the logged-in account from the session
        model.Account account = (model.Account) session.getAttribute("account");

        if (account != null) {
            request.setAttribute("account", account);
            RequestDispatcher dispatcher = request.getRequestDispatcher("accountRead.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("login.jsp"); // Redirect to login if no account found in session
        }
    }
}
