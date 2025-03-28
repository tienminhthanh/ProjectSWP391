/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.account;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Random;

public class AccountLib {

    public String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void sendEmail(String toEmail, String subject, String content) throws MessagingException {
        final String fromEmail;
        fromEmail = "systemwibooks@gmail.com";
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
// MD5 hashing function

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
            throw new RuntimeException("Error while hashing MD5", e);
        }
    }

    public boolean isValidPassword(String password) {

        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (!hasUpperCase && Character.isUpperCase(c)) {
                hasUpperCase = true;
                continue;
            }
            if (!hasLowerCase && Character.isLowerCase(c)) {
                hasLowerCase = true;
                continue;
            }
            if (!hasNumber && Character.isDigit(c)) {
                hasNumber = true;
                continue;
            }
            if (!hasSpecialChar) {
                hasSpecialChar = true;
            }

            if (hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar) {
                break;
            }
        }

        return hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar;
    }

    public boolean isValidPhoneNumber(String phoneNumber) {

        if (phoneNumber == null || phoneNumber.length() != 10 || !phoneNumber.startsWith("0")) {
            return false;
        }

        try {
            Long.parseLong(phoneNumber);
        } catch (NumberFormatException e) {
            return false;
        }

        String[] validPrefixes = {"09", "03", "08", "07", "05", "06"};

        for (String prefix : validPrefixes) {
            if (phoneNumber.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        AccountLib validator = new AccountLib();

        // Test cases for different passwords
        String[] testPasswords = {
            "Password1!", // Valid password
            "password", // Invalid (no uppercase, special character, and digit)
            "PASSWORD123", // Invalid (no lowercase, special character)
            "Pass123", // Invalid (no special character)
            "Pass@123", // Valid password
            "12345678", // Invalid (no letters or special character)
            "Abcd123@" // Valid password
        };

        // Test each password and print result
        for (String password : testPasswords) {
            boolean isValid = validator.isValidPassword(password);
            System.out.println("Password: " + password + " is valid: " + isValid);
        }
        String[] testPhoneNumbers = {
            "0987654321", // Valid phone number
            "0123456789", // Valid phone number
            "1234567890", // Invalid phone number (does not start with 0)
            "09876A4321", // Invalid phone number (contains non-numeric character)
            "0999999999", // Valid phone number
            "012345678" // Invalid phone number (too short)
        };

        // Test each phone number and print result
        for (String phoneNumber : testPhoneNumbers) {
            boolean isValid = validator.isValidPhoneNumber(phoneNumber);
            System.out.println("Phone Number: " + phoneNumber + " is valid: " + isValid);
        }
    }
}
