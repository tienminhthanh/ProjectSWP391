package controller.account;

import dao.AccountDAO;
import model.Account;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ListAccountSevlet", urlPatterns = {"/listAccount"})
public class ListAccountSevlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountDAO accountDAO = new AccountDAO();

        try {
            List<Account> accounts = accountDAO.getAllAccounts();
            Collections.reverse(accounts);
            request.setAttribute("accounts", accounts);
            RequestDispatcher dispatcher = request.getRequestDispatcher("accountList.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {

            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while fetching the accounts.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
