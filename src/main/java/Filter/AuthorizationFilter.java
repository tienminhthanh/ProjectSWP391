//package Filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import java.io.IOException;
//import java.util.*;
//import model.Account;
//
//public class AuthorizationFilter implements Filter {
//
//    private final Map<String, List<String>> roleAccessMap = new HashMap<>();
//
//    @Override
//    public void init(FilterConfig filterConfig) {
//        roleAccessMap.put("admin", Arrays.asList("/manageProductList", "/manageProductDetails", "/updateProduct", "/addProduct", 
//                "/changeProductStatus", "/importProduct", "/queueImport", "/search", "/category", "/genre", "/publisher", 
//                "/creator", "/series", "/brand", "/character", "/new", "/sale", "/ranking", "/productDetails", "/addAccount",
//                "/OrderDetailForStaffController", "/OrderListForStaffController", "/chat", "/listnotification", 
//                "/notificationdetail", "/createnotification", "/changePassword", "/eventAddNew", "/eventDelete", 
//                "/eventDetails", "/eventList", "/eventProductAddNew", "/eventProductDelete", "/eventUpdate",
//                "/voucherAddNew", "/voucherDelete", "/voucherDetails", "/voucherList", "/voucherUpdate"));
//
//        roleAccessMap.put("staff", Arrays.asList("/staff", "/manageOrders", "/search", "/category", "/genre", "/publisher",
//                "/creator", "/series", "/brand", "/character", "/new", "/sale", "/ranking", "/productDetails",
//                "/OrderDetailForStaffController", "/OrderListForStaffController", "/chat", "/listnotification", 
//                "/notificationdetail", "/createnotification", "/changePassword", "/logout", "/processPassword",
//                "/eventList", "/eventDetails", "/eventUpdate", "/eventProductAddNew", "/eventProductDelete",
//                "/voucherList", "/voucherDetails", "/voucherUpdate"));
//
//        roleAccessMap.put("customer", Arrays.asList("/changePassword", "/deleteAccount", "/emailAuthentication", "/emailForgot",
//                "/emailUnlock", "/forgotPassword", "/loginGoogle", "/login", "/logout", "/processPassword", "/register", 
//                "/readAccount", "/removeEmailFromLockedAccount", "/unlockAccount", "/updateAccount", "/home", "/cart", 
//                "/notification", "/chat", "/OrderController", "/search", "/category", "/genre", "/publisher", "/creator", 
//                "/series", "/brand", "/character", "/new", "/sale", "/ranking", "/productDetails", "/DeleteOrderController", 
//                "/OrderController", "/OrderDetailController", "/OrderListController", "/UpdateOrderController",
//                "/cart", "/notification", "/notificationDetail", "/chat", "/eventList", "/eventDetails",
//                "/voucherList", "/voucherDetails"));
//
//        roleAccessMap.put("shipper", Arrays.asList("/OrderDetailForShipperController", "/viewOrders", "/home", "/search", 
//                "/category", "/genre", "/publisher", "/creator", "/series", "/brand", "/character", "/new", "/sale", 
//                "/ranking", "/productDetails", "/OrderListForShipperController", "/OrderDetailForShipperController",
//                "/notificationshipper", "/notificationdetail", "/changePassword", "/eventDetails", "/eventList"));
//
//        roleAccessMap.put("guest", Arrays.asList("/emailAuthentication", "/emailForgot", "/emailUnlock", "/forgotPassword",
//                "/loginGoogle", "/login", "/logout", "/processPassword", "/register", "/readAccount", 
//                "/removeEmailFromLockedAccount", "/unlockAccount", "/updateAccount", "/home", "/cart.jsp", "/chatAI.jsp", 
//                "/search", "/category", "/genre", "/publisher", "/creator", "/series", "/brand", "/character", "/new", 
//                "/sale", "/ranking", "/productDetails", "/eventList", "/eventDetails", "/voucherList", "/voucherDetails"));
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//        HttpSession session = req.getSession(false);
//
//        // Lấy account từ session
//        Account account = (session != null) ? (Account) session.getAttribute("account") : null;
//        // Chuẩn hóa role: chuyển về chữ thường và loại bỏ khoảng trắng
//        String role = (account != null && account.getRole() != null) ? account.getRole().trim().toLowerCase() : "guest";
//        String path = req.getServletPath();
//
//        // In log để debug (có thể xóa sau khi kiểm tra xong)
//        System.out.println("Filter - Account: " + (account != null ? account.toString() : "null"));
//        System.out.println("Filter - Role: " + role);
//        System.out.println("Filter - Path: " + path);
//
//        if (isAuthorized(role, path)) {
//            chain.doFilter(request, response);
//        } else {
//            System.out.println("Filter - Access denied, redirecting to /home");
//            res.sendRedirect(req.getContextPath() + "/home");
//        }
//    }
//
//    @Override
//    public void destroy() {
//        // Không cần làm gì ở đây
//    }
//
//    private boolean isAuthorized(String role, String path) {
//        // Admin được phép truy cập mọi thứ
//        if ("admin".equals(role)) {
//            System.out.println("Filter - Admin detected, allowing access to: " + path);
//            return true;
//        }
//
//        // Lấy danh sách các đường dẫn được phép cho role
//        List<String> accessiblePaths = roleAccessMap.get(role);
//        if (accessiblePaths == null) {
//            System.out.println("Filter - No accessible paths defined for role: " + role);
//            return false;
//        }
//
//        // Kiểm tra xem path có bắt đầu bằng bất kỳ đường dẫn nào trong danh sách không
//        boolean authorized = accessiblePaths.stream().anyMatch(path::startsWith);
//        System.out.println("Filter - Role: " + role + ", Path: " + path + ", Authorized: " + authorized);
//        return authorized;
//    }
//}