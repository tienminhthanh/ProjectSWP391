package controller.account;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.mail.*;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import model.Account;

@WebServlet(name = "EmailAuthenticationServlet", urlPatterns = {"/emailAuthentication"})
public class EmailAuthenticationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountLib lib = new AccountLib();
        HttpSession session = request.getSession();

        String email = (String) session.getAttribute("tempEmail");
        String otp = lib.generateOTP();
        session.setAttribute("otp", otp);

        try {
            lib.sendEmail(email, "Email Verification", "Your verification code is: " + otp);
            request.setAttribute("message", "A verification code has been sent to your email.");
        } catch (MessagingException e) {
            e.printStackTrace();
            request.setAttribute("message", "Failed to send email.");
        }
        request.getRequestDispatcher("verifyEmail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("register.jsp?error=sessionNotFound");
            return;
        }

        String enteredOTP = request.getParameter("otp");
        String generatedOTP = (String) session.getAttribute("otp");

        if (generatedOTP == null || enteredOTP == null || !enteredOTP.equals(generatedOTP)) {
            request.setAttribute("errorMessage", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("verifyEmail.jsp").forward(request, response);
            return;
        }

        try {
            Account account = (Account) session.getAttribute("account");
            String address = (String) session.getAttribute("address");

            if (account == null) {
                response.sendRedirect("register.jsp?error=sessionExpired");
                return;
            }

            AccountDAO accountDAO = new AccountDAO();
            boolean success = accountDAO.register(
                    account.getUsername(),
                    account.getPassword(),
                    account.getFirstName(),
                    account.getLastName(),
                    account.getEmail(),
                    account.getPhoneNumber(),
                    account.getBirthDate(),
                    address
            );

            if (success) {
                Account newAccount = accountDAO.getAccountByUsername(account.getUsername());
                session.invalidate(); // Xóa session cũ

                // Tạo session mới cho auto-login
                session = request.getSession();
                session.setAttribute("account", newAccount);
                session.setMaxInactiveInterval(3600);

                request.setAttribute("message", "Email verified successfully! You are now logged in.");
                request.getRequestDispatcher("completeAccount.jsp").forward(request, response);
            } else {
                request.setAttribute("message", "Failed to verify email.");
                request.getRequestDispatcher("verifyEmail.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Database error occurred.");
            request.getRequestDispatcher("verifyEmail.jsp").forward(request, response);
        }
    }
}
