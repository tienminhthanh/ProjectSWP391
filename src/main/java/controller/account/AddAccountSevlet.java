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

@WebServlet(name = "AddAccountServlet", urlPatterns = {"/addAccount"})
public class AddAccountSevlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("account") != null) {
            model.Account account = (model.Account) request.getSession().getAttribute("account");
            if ("admin".equals(account.getRole())) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("addAccount.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect("home.jsp");
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = hashMD5(request.getParameter("password"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");
        String role = request.getParameter("role");
        AccountDAO accountDAO = new AccountDAO();
        try {
            if (accountDAO.isEmailExist(email, null)) {
                request.setAttribute("message", "The email address is already in use.");
                request.getRequestDispatcher("accountAddNew.jsp").forward(request, response);
                return;
            }
            if (accountDAO.getAccountByUsername(username) != null) {
                request.setAttribute("message", "Username already exists!");
                request.getRequestDispatcher("accountAddNew.jsp").forward(request, response);
                return;
            }
            boolean success = accountDAO.createAccount(username, password, firstName, lastName, email, phoneNumber, birthDate, role);
            if (success) {
                request.setAttribute("message", "Account successfully created!");
            } else {
                request.setAttribute("message", "There was an issue creating the account. Please try again.");
            }
            request.getRequestDispatcher("accountAddNew.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "An error occurred while processing your request. Please try again later.");
            request.getRequestDispatcher("accountAddNew.jsp").forward(request, response);
        }
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
