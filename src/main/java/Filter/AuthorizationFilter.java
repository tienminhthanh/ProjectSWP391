package Filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import model.Account;

public class AuthorizationFilter implements Filter {

    private final Map<String, List<String>> roleAccessMap = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) {
        roleAccessMap.put("admin", Arrays.asList("/manageProductList", "/manageProductDetails", "/updateProduct", "/addProduct", "/changeProductStatus", "/importProduct",
                "/queueImport", "/search", "/category", "/genre", "/publisher", "/creator", "/series", "/brand", "/character",
                "/new", "/sale", "/ranking", "/productDetails", "/addAccount",
                "/OrderDetailForStaffController", "/OrderListForStaffController",
                "/chat", "/listnotification", "/notificationdetail", "/createnotification","/changePassword"));

        roleAccessMap.put("staff", Arrays.asList("/staff", "/manageOrders", "/search", "/category", "/genre", "/publisher",
                "/creator", "/series", "/brand", "/character", "/new", "/sale", "/ranking", "/productDetails",
                "/OrderDetailForStaffController", "/OrderListForStaffController",
                "/chat", "/listnotification", "/notificationdetail", "/createnotification","/changePassword"));

        roleAccessMap.put("customer", Arrays.asList("/changePassword", "/deleteAccount", "/emailAuthentication", "/emailForgot",
                "/emailUnlock", "/forgotPassword", "/loginGoogle", "/login", "/logout", "/processPassword", "/register", "/readAccount",
                "/removeEmailFromLockedAccount", "/unlockAccount", "/updateAccount", "/home", "/cart", "/notification", "/chat", "/OrderController",
                "/search", "/category", "/genre", "/publisher", "/creator", "/series", "/brand", "/character", "/new", "/sale", "/ranking", "/productDetails",
                "/DeleteOrderController", "/OrderController", "/OrderDetailController", "/OrderListController", "/UpdateOrderController",
                "/cart", "/notification", "/notificationDetail", "/chat"));

        roleAccessMap.put("shipper", Arrays.asList("/OrderDetailForShipperController", "/viewOrders", "/home", "/search", "/category", "/genre",
                "/publisher", "/creator", "/series", "/brand", "/character", "/new", "/sale", "/ranking", "/productDetails",
                "/OrderListForShipperController", "/OrderDetailForShipperController",
                "/notificationshipper", "/notificationdetail","/changePassword"));

        roleAccessMap.put("guest", Arrays.asList("/emailAuthentication", "/emailForgot", "/emailUnlock", "/forgotPassword",
                "/loginGoogle", "/login", "/logout", "/processPassword", "/register", "/readAccount", "/removeEmailFromLockedAccount",
                "/unlockAccount", "/updateAccount", "/home", "/cart.jsp", "/chatAI.jsp", "/search", "/category", "/genre", "/publisher",
                "/creator", "/series", "/brand", "/character", "/new", "/sale", "/ranking", "/productDetails"));

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        Account account = (session != null) ? (Account) session.getAttribute("account") : null;
        String role = (account != null && account.getRole() != null) ? account.getRole().toLowerCase() : "guest";
        String path = req.getServletPath();

        if (isAuthorized(role, path)) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "/home");
        }
    }

    @Override
    public void destroy() {
    }

    private boolean isAuthorized(String role, String path) {
        if ("admin".equals(role)) {
            return true;
        }

        List<String> accessiblePaths = roleAccessMap.get(role);
        if (accessiblePaths == null) {
            return false;
        }

        return accessiblePaths.stream().anyMatch(path::startsWith);
    }
}
