package controller.account;

import dao.AccountDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");

        AccountDAO accountDAO = new AccountDAO();

        if (accountDAO.getAccountByUsername(username) != null) {
            request.setAttribute("message", "Username already exists!");
            return;
        }

        if (accountDAO.isEmailExistForEmail(email)) {
            request.setAttribute("message", "The email address is already in use by another account.");
            return;
        }

        boolean success = accountDAO.register(username, password, firstName, lastName, email, phoneNumber, birthDate,"customer");

        if (success) {
            request.setAttribute("message", "Registration successful! Please log in.");
        } else {
            request.setAttribute("message", "Username or email is already in use.");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
        dispatcher.forward(request, response);
    }
}
