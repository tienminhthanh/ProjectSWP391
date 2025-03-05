package controller.account;

import dao.AccountDAO;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "EmailForgotServlet", urlPatterns = {"/emailForgot"})
public class EmailForgotServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get email from session that was temporarily stored
        String email = (String) request.getSession().getAttribute("tempEmail");
        AccountLib lib = new AccountLib();
        
        // Generate OTP for password reset
        String otp = lib.generateOTP();
        request.getSession().setAttribute("otp", otp);

        try {
            // Send OTP to the user's email
            lib.sendEmail(email, "Password Reset Request", 
                "We received a request to reset your password.\n\n" + 
                "Your OTP code is: " + otp + "\n\n" +
                "If you did not request this, please ignore this email.");

            // Inform the user that the OTP has been sent
            request.setAttribute("message", "A verification code has been sent to your email.");
        } catch (MessagingException e) {
            e.printStackTrace();
            request.setAttribute("message", "Failed to send email. Please try again.");
        }
        // Forward to the page where user will enter OTP to reset the password
        request.getRequestDispatcher("verifyForgot.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String enteredOTP = request.getParameter("otp");
        String generatedOTP = (String) request.getSession().getAttribute("otp");
        String email = (String) request.getSession().getAttribute("tempEmail");

        if (generatedOTP == null || enteredOTP == null || !enteredOTP.equals(generatedOTP)) {
            request.setAttribute("message", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("verifyForgot.jsp").forward(request, response);
            return;
        }

        // After successful OTP verification, forward user to reset password page
        request.setAttribute("email", email); // Store email in the request for next step (reset password)
        request.getRequestDispatcher("processPassword.jsp").forward(request, response);
    }
}
