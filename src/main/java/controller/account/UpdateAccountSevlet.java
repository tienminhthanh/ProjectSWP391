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

@WebServlet(name = "UpdateAccountSevlet", urlPatterns = {"/updateAccount"})
public class UpdateAccountSevlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        AccountDAO accountDAO = new AccountDAO();

        try {
            Account account = accountDAO.getAccountByUsername(username);

            if (account != null) {
                request.setAttribute("account", account);
                RequestDispatcher dispatcher = request.getRequestDispatcher("accountUpdate.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect("login.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");

        AccountDAO accountDAO = new AccountDAO();

        try {
            // Kiểm tra xem email đã tồn tại cho người dùng khác chưa
            if (accountDAO.isEmailExistEmailOfUser(username, email)) {
                request.setAttribute("message", "The email address is already in use by another account.");

                request.setAttribute("username", username);
                request.setAttribute("firstName", firstName);
                request.setAttribute("lastName", lastName);
                request.setAttribute("email", email);
                request.setAttribute("phoneNumber", phoneNumber);
                request.setAttribute("birthDate", birthDate);

                RequestDispatcher dispatcher = request.getRequestDispatcher("accountUpdate.jsp");
                dispatcher.forward(request, response);
            } else {
                boolean success = accountDAO.updateAccount(username, firstName, lastName, email, phoneNumber, birthDate);

                if (success) {
                    HttpSession session = request.getSession();
                    Account updatedAccount = accountDAO.getAccountByUsername(username);
                    session.setAttribute("account", updatedAccount);

                  
                    Account loggedInAccount = (Account) session.getAttribute("account");
                    if (loggedInAccount != null && "admin".equals(loggedInAccount.getRole())) {
                        response.sendRedirect("listAccount");
                    } else {
                        response.sendRedirect("readAccount?username=" + username);
                    }
                } else {
                    request.setAttribute("message", "Account update failed! Please try again.");

                    request.setAttribute("username", username);
                    request.setAttribute("firstName", firstName);
                    request.setAttribute("lastName", lastName);
                    request.setAttribute("email", email);
                    request.setAttribute("phoneNumber", phoneNumber);
                    request.setAttribute("birthDate", birthDate);

                    RequestDispatcher dispatcher = request.getRequestDispatcher("accountUpdate.jsp");
                    dispatcher.forward(request, response);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
