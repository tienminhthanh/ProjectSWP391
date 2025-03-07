package controller.account;

import dao.AccountDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "AddAccountServlet", urlPatterns = {"/addAccount"})
public class AddAccountController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("account") != null) {
            model.Account account = (model.Account) request.getSession().getAttribute("account");
            if ("admin".equals(account.getRole())) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("addAccount.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect("home.jsp");
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountLib lib = new AccountLib();

        String username = request.getParameter("username");
        String password = lib.hashMD5(request.getParameter("password"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");
        String role = request.getParameter("role");
        AccountDAO accountDAO = new AccountDAO();
        try {
            if (accountDAO.isEmailExist(email, null)) {
                request.setAttribute("erorrMessage", "The email address is already in use.");
                request.getRequestDispatcher("accountAddNew.jsp").forward(request, response);
                return;
            }
            if (accountDAO.getAccountByUsername(username) != null) {
                request.setAttribute("erorrMessage", "Username already exists!");
                request.getRequestDispatcher("accountAddNew.jsp").forward(request, response);
                return;
            }
            boolean success = accountDAO.createAccount(username, password, firstName, lastName, email, phoneNumber, birthDate, role);
            if (success) {
                request.setAttribute("message", "Account successfully created!");
            } else {
                request.setAttribute("message", "There was an issue creating the account. Please try again.");
            }
            request.getRequestDispatcher("accountAddNew.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erorrMessage", "An error occurred while processing your request. Please try again later.");
            request.getRequestDispatcher("accountAddNew.jsp").forward(request, response);
        }
    }

}
