package controller.account;

import dao.AccountDAO;
import model.Account;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Customer;
import model.DeliveryAddress;

@WebServlet(name = "ReadAccountServlet", urlPatterns = {"/readAccount"})
public class ReadAccountController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        Account account = (Account) session.getAttribute("account");

        if (account != null) {
            // Retrieve additional information based on the role
            AccountDAO accountDAO = new AccountDAO();
            try {

                account = accountDAO.getAdditionalInfo(account);
            } catch (SQLException ex) {
                Logger.getLogger(ReadAccountController.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Update the account with full information in the session
            session.setAttribute("account", account);
            request.setAttribute("account", account);
            RequestDispatcher dispatcher = request.getRequestDispatcher("accountRead.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    public static void main(String[] args) {
        AccountDAO accountDAO = new AccountDAO();

        try {
            // Retrieve the account by username
            String username = "anhk_cus2"; // Replace with your username
            Account account = accountDAO.getAccountByUsername(username);

            if (account == null) {
                System.out.println("⚠ No account found with username: " + username);
                return;
            }

            // Retrieve additional information based on the role
            account = accountDAO.getAdditionalInfo(account);

            // Check if the account is a customer
            if (account instanceof Customer) {
                Customer customer = (Customer) account;
                System.out.println("✅ Retrieved customer account information:");
                System.out.println("Username: " + customer.getUsername());
                System.out.println("Total Purchase Points: " + customer.getTotalPurchasePoints());
                System.out.println("Default Delivery Address: " + customer.getDefaultDeliveryAddress());
            } else {
                System.out.println("⚠ The account is not a customer.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ An error occurred while querying data.");
        }
    }
}
