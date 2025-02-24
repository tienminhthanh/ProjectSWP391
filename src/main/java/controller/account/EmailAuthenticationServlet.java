package controller.account;

import dao.AccountDAO;
<<<<<<< HEAD
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

@WebServlet(name = "EmailAuthenticationServlet", urlPatterns = {"/emailAuthentication"})
public class EmailAuthenticationServlet extends HttpServlet {

    private void sendEmail(String toEmail, String subject, String content) throws MessagingException {
        final String fromEmail = "systemwibooks@gmail.com"; // Your email address
        final String password = "lxuh bqye fyce avzb"; // Your email password

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(content);

        Transport.send(message);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String enteredOTP = request.getParameter("otp");
        AccountDAO accountDAO = new AccountDAO();

        if (enteredOTP != null) {
            String generatedOTP = (String) request.getSession().getAttribute("otp");
            if (enteredOTP.equals(generatedOTP)) {
                request.setAttribute("message", "Email verified successfully!");
            } else {
                request.setAttribute("message", "Invalid OTP. Please try again.");
            }
            request.getRequestDispatcher("home.jsp").forward(request, response);
            return;
        }

        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            request.setAttribute("message", "Invalid email format!");
            request.getRequestDispatcher("home.jsp").forward(request, response);
            return;
        }

        try {
            if (accountDAO.isEmailExistForEmail(email)) {
                request.setAttribute("message", "Email is already registered!");
                request.getRequestDispatcher("home.jsp").forward(request, response);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Error occurred while checking email. Please try again later.");
            request.getRequestDispatcher("home.jsp").forward(request, response);
            return;
        }

        String otp = generateOTP();

        try {
            sendEmail(email, "Email Verification", "Your verification code is: " + otp);
            request.getSession().setAttribute("otp", otp); // Store OTP in session
            request.setAttribute("message", "Verification code has been sent to your email!");
            request.getRequestDispatcher("home.jsp").forward(request, response);
        } catch (MessagingException e) {
            e.printStackTrace();
            request.setAttribute("message", "Error in sending email. Please try again later.");
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }

    private String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public static void main(String[] args) {
        EmailAuthenticationServlet test = new EmailAuthenticationServlet();
        String email = "thanhtmce181941@fpt.edu.vn";
=======
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

@WebServlet(name = "EmailAuthenticationServlet", urlPatterns = {"/emailAuthentication"})
public class EmailAuthenticationServlet extends HttpServlet {

    private void sendEmail(String toEmail, String subject, String content) throws MessagingException {
        final String fromEmail = "systemwibooks@gmail.com";
        final String password = "lxuh bqye fyce avzb";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(content);

        Transport.send(message);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = (String) request.getSession().getAttribute("tempEmail");
        String otp = generateOTP();

        request.getSession().setAttribute("otp", otp);

        try {
            sendEmail(email, "Email Verification", "Your verification code is: " + otp);
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
            boolean updateSuccess = accountDAO.updateEmail(username, email);

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

    private String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public static void main(String[] args) {
        EmailAuthenticationServlet test = new EmailAuthenticationServlet();
        String email = "tienminthanh@gmail.com";
>>>>>>> origin/ThanhMoi

        String otp = test.generateOTP();
        try {
            test.sendEmail(email, "Email Verification", "Your verification code is: " + otp);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
