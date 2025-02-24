package controller.account;

import dao.AccountDAO;
<<<<<<< HEAD
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String conformPassword = request.getParameter("conformPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");

        try {
            AccountDAO accountDAO = new AccountDAO();
            if (accountDAO.getAccountByUsername(username) != null) {
                request.setAttribute("message", "Username already exists!");
                return;
            }
            if (!password.equals(conformPassword)) {
                request.setAttribute("message", "Password and re-enter password are not the same!");
                return;
            }

            if (accountDAO.isEmailExistForEmail(email)) {
                request.setAttribute("message", "The email address is already in use by another account.");
                return;
            }

            boolean success = accountDAO.register(username, password, firstName, lastName, email, phoneNumber, birthDate, "customer");

            if (success) {
                request.setAttribute("message", "Registration successful! Please log in.");
            } else {
                request.setAttribute("message", "Username or email is already in use.");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
        dispatcher.forward(request, response);
=======
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");

        try {
            AccountDAO accountDAO = new AccountDAO();
            boolean isUsernameExist = accountDAO.getAccountByUsername(username) != null;
            if (isUsernameExist) {
                request.setAttribute("message", "Username already exists!");
                setRequestAttributes(request, username, firstName, lastName, email, phoneNumber, birthDate);
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else {
                if (!password.equals(confirmPassword)) {
                    request.setAttribute("message", "Passwords do not match!");
                    setRequestAttributes(request, username, firstName, lastName, email, phoneNumber, birthDate);
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                } else {
                    boolean success = accountDAO.register(username, password, firstName, lastName, null, phoneNumber, birthDate, "customer");
                    if (success) {
                        request.getSession().setAttribute("tempEmail", email);
                        request.getSession().setAttribute("tempUsername", username);
                        response.sendRedirect("emailAuthentication");
                    } else {
                        request.setAttribute("message", "Registration failed. Please try again.");
                        setRequestAttributes(request, username, firstName, lastName, email, phoneNumber, birthDate);
                        request.getRequestDispatcher("register.jsp").forward(request, response);
                    }

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Database error: " + e.getMessage());
            setRequestAttributes(request, username, firstName, lastName, email, phoneNumber, birthDate);
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    private void setRequestAttributes(HttpServletRequest request, String username, String firstName, String lastName, String email, String phoneNumber, String birthDate) {
        request.setAttribute("username", username);
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);
        request.setAttribute("email", email);
        request.setAttribute("phoneNumber", phoneNumber);
        request.setAttribute("birthDate", birthDate);
>>>>>>> origin/ThanhMoi
    }
}
