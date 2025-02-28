package controller.account;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.mail.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

@WebServlet(name = "EmailAuthenticationServlet", urlPatterns = {"/emailAuthentication"})
public class EmailAuthenticationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MyLib lib = new MyLib();
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
