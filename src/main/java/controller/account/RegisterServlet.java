package controller.account;

import dao.AccountDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
            String message = null;
            if (accountDAO.getAccountByUsername(username) != null) {
                message = "Username already exists!";
            } else if (accountDAO.isEmailExist(email, null)) {
                message = "Email already exists!";
            } else if (!password.equals(confirmPassword)) {
                message = "Passwords do not match!";
            } else {
                password = hashMD5(confirmPassword);
                boolean success = accountDAO.register(username, password, firstName, lastName, email, phoneNumber, birthDate);
                if (success) {
                    request.getSession().setAttribute("tempEmail", email);
                    request.getSession().setAttribute("tempUsername", username);
                    response.sendRedirect("emailAuthentication");
                    return;
                } else {
                    message = "Registration failed. Please try again.";
                }
            }
            if (message != null) {
                request.setAttribute("message", message);
                forwardToRegisterPage(request, response, username, firstName, lastName, email, phoneNumber, birthDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Database error: " + e.getMessage());
            forwardToRegisterPage(request, response, username, firstName, lastName, email, phoneNumber, birthDate);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Chuyển hướng lại trang đăng ký và giữ nguyên dữ liệu nhập vào.
     */
    private void forwardToRegisterPage(HttpServletRequest request, HttpServletResponse response,
            String username, String firstName, String lastName,
            String email, String phoneNumber, String birthDate)
            throws ServletException, IOException {
        request.setAttribute("username", username);
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);
        request.setAttribute("email", email);
        request.setAttribute("phoneNumber", phoneNumber);
        request.setAttribute("birthDate", birthDate);
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    public String hashMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_16LE);
            byte[] hashBytes = md.digest(inputBytes);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02X", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi khi mã hóa MD5", e);
        }
    }
}
