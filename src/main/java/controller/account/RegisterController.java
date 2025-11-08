package controller.account;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {
private final AccountDAO accountDAO = AccountDAO.getInstance();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // Lấy dữ liệu từ form
        String username = request.getParameter("username").toLowerCase();
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");
        String address = request.getParameter("address");

        String message = null;

        try {
            AccountLib lib = new AccountLib();

            // Kiểm tra email có bị trùng không
            Account accountByEmail = accountDAO.getAccountByEmail(email);
            if (accountByEmail != null && !"admin".equals(accountByEmail.getRole())) {
                if (accountByEmail.getAccountIsActive()) {
                    message = "This email is already in use!";
                } else {
                    request.getSession().setAttribute("tempEmail", email);
                    request.setAttribute("lockedAccount", accountByEmail);
                    request.setAttribute("message", "This email belongs to a locked account. Do you want to unlock it or remove the email?");
                    request.getRequestDispatcher("unlockOrRegister.jsp").forward(request, response);
                    return;
                }
            }

            // Kiểm tra username đã tồn tại chưa
            if (accountDAO.getAccountByUsername(username) != null) {
                message = "Username is already taken!";
            } else if (!password.equals(confirmPassword)) {
                message = "Passwords do not match!";
            } else if (!lib.isValidPassword(password)) {
                message = "Password must be at least 8 characters long and contain uppercase letters, lowercase letters, digits, and special characters.";
            } else if (!lib.isValidPhoneNumber(phoneNumber)) {
                message = "The phone number must has 10 number and phone number Viet Nam";
            } else {
                // Mã hóa mật khẩu
                String hashedPassword = lib.hashMD5(confirmPassword);

                String role = "customer"; // Giả sử mặc định là customer
                int id = (int) ((Math.random() * 100000) + 1);
                // Tạo đối tượng Account không cần accountID
                Account account = new Account(id, username, hashedPassword, role, firstName, lastName, email, phoneNumber, birthDate, true);

                // Lưu thông tin vào session
                HttpSession session = request.getSession();
                session.setAttribute("account", account);
                session.setAttribute("address", address);
                session.setAttribute("tempEmail", email);
                session.setAttribute("tempUsername", username);

                // Chuyển hướng đến trang xác thực email
                response.sendRedirect("emailAuthentication");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message = "Database error: " + e.getMessage();
        }

        // Nếu có lỗi, giữ lại thông tin và chuyển hướng lại trang đăng ký
        if (message != null) {
            request.setAttribute("message", message);
            forwardToRegisterPage(request, response, username, firstName, lastName, email, phoneNumber, birthDate, password, address);
        }
    }

    private void forwardToRegisterPage(HttpServletRequest request, HttpServletResponse response,
            String username, String firstName, String lastName,
            String email, String phoneNumber, String birthDate,
            String password, String address) // Thêm các tham số mật khẩu và địa chỉ
            throws ServletException, IOException {

        // Thiết lập các thuộc tính cho request để gửi dữ liệu vào JSP
        request.setAttribute("username", username);
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);
        request.setAttribute("email", email);
        request.setAttribute("phoneNumber", phoneNumber);
        request.setAttribute("birthDate", birthDate);
        request.setAttribute("password", password);  // Thêm mật khẩu vào request
        request.setAttribute("address", address);  // Thêm địa chỉ vào request

        // Chuyển tiếp tới trang đăng ký
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

}
