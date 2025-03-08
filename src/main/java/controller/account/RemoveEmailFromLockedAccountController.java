/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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

@WebServlet(name = "RemoveEmailFromLockedAccountServlet", urlPatterns = {"/removeEmailFromLockedAccount"})
public class RemoveEmailFromLockedAccountController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = (String) request.getSession().getAttribute("tempEmail");
        AccountDAO accountDAO = new AccountDAO();
        try {
            Account account = accountDAO.getAccountByEmail(email);
            if (account == null) {
                request.setAttribute("message", "Account not found!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Generate OTP
            AccountLib lib = new AccountLib();
            String otp = lib.generateOTP();
            request.getSession().setAttribute("otp", otp);
            request.getSession().setAttribute("tempEmail", email);

            // Compose email content
            String subject = "Email Verification - Remove Email From Account";
            String content = "Dear " + account.getFirstName() + " " + account.getLastName() + ",\n\n"
                    + "We received a request to remove your email from the locked account.\n\n"
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
            request.getRequestDispatcher("verifyRemoveEmail.jsp").forward(request, response);  // Forward dữ liệu sau khi gửi email.
        } catch (MessagingException e) {
            e.printStackTrace();
            request.setAttribute("message", "Failed to send email.");
            request.getRequestDispatcher("verifyRemoveEmail.jsp").forward(request, response);  // Đảm bảo là không forward sau khi gửi dữ liệu phản hồi.
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Database error occurred.");
            request.getRequestDispatcher("verifyRemoveEmail.jsp").forward(request, response);  // Đảm bảo là không forward sau khi gửi dữ liệu phản hồi.
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String enteredOTP = request.getParameter("otp");
        String generatedOTP = (String) request.getSession().getAttribute("otp");
        String email = (String) request.getSession().getAttribute("tempEmail");

        if (generatedOTP == null || enteredOTP == null || !enteredOTP.equals(generatedOTP)) {
            // Đảm bảo thông báo lỗi được xử lý và forward trước khi phản hồi được cam kết.
            request.setAttribute("message", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("verifyRemoveEmail.jsp").forward(request, response);
            return;
        }

        try {
            AccountDAO accountDAO = new AccountDAO();
            Account account = accountDAO.getAccountByEmail(email);

            if (account != null && !account.getIsActive()) {
                // Remove email from the account using the new method
                boolean success = accountDAO.removeEmailFromAccount(account.getUsername());
                if (success) {
                    request.setAttribute("message", "Email has been removed from the locked account.\n"
                            + " You can use that email account for another account.");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "Failed to remove email from the account.");
                    request.getRequestDispatcher("verifyRemoveEmail.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("message", "Account not found or already active.");
                request.getRequestDispatcher("verifyRemoveEmail.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Database error occurred.");
            request.getRequestDispatcher("verifyRemoveEmail.jsp").forward(request, response);
        }
    }

}
