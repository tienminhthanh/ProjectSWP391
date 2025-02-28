package controller.account;

import dao.AccountDAO;
import jakarta.mail.MessagingException;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import model.Account;

@WebServlet(name = "EmailUnlockServlet", urlPatterns = {"/emailUnlock"})
public class EmailUnlockServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountLib lib = new AccountLib();
        String email = (String) request.getSession().getAttribute("tempEmail");

        try {
            AccountDAO accountDAO = new AccountDAO();
            Account account = accountDAO.getAccountByEmail(email);

            if (account == null) {
                request.setAttribute("message", "Account not found!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Generate OTP
            String otp = lib.generateOTP();
            request.getSession().setAttribute("otp", otp);
            request.getSession().setAttribute("tempUsername", account.getUsername());

            // Compose email content
            String subject = "Email Verification - Unlock Your Account";
            String content = "Dear " + account.getFirstName() + " " + account.getLastName() + ",\n\n"
                    + "We received a request to unlock your account.\n\n"
                    + "Your account details:\n"
                    + "Username: " + account.getUsername() + "\n"
                    + "First Name: " + account.getFirstName() + "\n"
                    + "Last Name: " + account.getLastName() + "\n"
                    + "Registered Email: " + account.getEmail() + "\n\n"
                    + "To verify your request, please enter the following OTP code:\n"
                    + "**" + otp + "**\n\n"
                    + "If you did not request this, please ignore this email.\n\n"
                    + "Best regards,\n"
                    + "WIBOOKS Team";

            // Send email
            lib.sendEmail(email, subject, content);

            request.setAttribute("message", "A verification code has been sent to your email.");
        } catch (MessagingException e) {
            e.printStackTrace();
            request.setAttribute("message", "Failed to send email.");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Database error occurred.");
        }

        request.getRequestDispatcher("verifyUnlock.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String enteredOTP = request.getParameter("otp");
        String generatedOTP = (String) request.getSession().getAttribute("otp");
        String email = (String) request.getSession().getAttribute("tempEmail");

        if (generatedOTP == null || enteredOTP == null || !enteredOTP.equals(generatedOTP)) {
            request.setAttribute("message", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("verifyUnlock.jsp").forward(request, response);
            return;
        }

        try {
            AccountDAO accountDAO = new AccountDAO();
            Account account = accountDAO.getAccountByEmail(email);

            if (account != null && !account.getIsActive()) {
                // Unlock account after successful verification
                boolean unlockSuccess = accountDAO.updateAccountStatus(account.getUsername(), true);
                if (unlockSuccess) {
                    request.getSession().invalidate();
                    request.setAttribute("message", "Your account has been unlocked successfully! You can now log in.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);//sua thanh change pass
                } else {
                    request.setAttribute("message", "Failed to unlock your account.");
                    request.getRequestDispatcher("verifyUnlock.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("message", "Account not found or already active.");
                request.getRequestDispatcher("verifyUnlock.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Database error occurred.");
            request.getRequestDispatcher("verifyUnlock.jsp").forward(request, response);
        }
    }
}
