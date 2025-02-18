package controller.account;

import dao.AccountDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AddAccountShipOrStaffSevlet", urlPatterns = {"/addAccount"})
public class AddAccountSevlet extends HttpServlet {

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

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");
        String role = request.getParameter("role");

        AccountDAO accountDAO = new AccountDAO();

        if (accountDAO.isEmailExistForEmail(email)) {
            request.setAttribute("message", "The email address is already in use.");
            request.getRequestDispatcher("accountAddNew.jsp").forward(request, response);
            return;
        }

        if (accountDAO.getAccountByUsername(username) != null) {
            request.setAttribute("message", "Username already exists!");
            request.getRequestDispatcher("accountAddNew.jsp").forward(request, response);
            return;
        }

        boolean success = accountDAO.addStaffOrShipper(username, password, firstName, lastName, email, phoneNumber, birthDate, role);

        if (success) {
            request.setAttribute("message", "Account successfully created!");
        } else {
            request.setAttribute("message", "Username or email is already in use.");
        }

        request.getRequestDispatcher("accountAddNew.jsp").forward(request, response);
    }
}
