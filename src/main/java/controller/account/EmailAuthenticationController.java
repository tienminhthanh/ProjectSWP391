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
import java.util.Random;
import model.Account;

@WebServlet(name = "EmailAuthenticationServlet", urlPatterns = {"/emailAuthentication"})
public class EmailAuthenticationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountLib lib = new AccountLib();
        String email = (String) request.getSession().getAttribute("tempEmail");
        String otp = lib.generateOTP();
        request.getSession().setAttribute("otp", otp);

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
        String enteredOTP = request.getParameter("otp");
        String generatedOTP = (String) request.getSession().getAttribute("otp");
        String username = (String) request.getSession().getAttribute("tempUsername");
        String email = (String) request.getSession().getAttribute("tempEmail");
        if (generatedOTP == null || enteredOTP == null || !enteredOTP.equals(generatedOTP)) {
            request.setAttribute("message", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("verifyEmail.jsp").forward(request, response);
            return;
        }
        try {
            AccountDAO accountDAO = new AccountDAO();
            boolean updateSuccess = accountDAO.updateAccount(username, null, null, email, null, null, null);
            if (updateSuccess) {
                request.getSession().invalidate();
                request.setAttribute("message", "Email verified successfully! You can now log in.");
                request.getRequestDispatcher("completeAccount.jsp").forward(request, response);
                if (updateSuccess) {
                    // Get the newly registered account
                    Account newAccount = accountDAO.getAccountByUsername(username);

                    // Auto login
                    HttpSession session = request.getSession();
                    session.setAttribute("account", newAccount);
                    session.setMaxInactiveInterval(60 * 60); // 60 minutes session timeout

                }

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
