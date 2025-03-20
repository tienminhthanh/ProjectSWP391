import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import model.Account;

public class AuthorizationFilter implements Filter {

    // Define accessible pages for each role
    private final Map<String, List<String>> roleAccessMap = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) {
        roleAccessMap.put("admin", Arrays.asList("/home","/login","/listAccount"));
        roleAccessMap.put("staff", Arrays.asList("/staff", "/manageOrders", "/home"));
        roleAccessMap.put("customer", Arrays.asList("/customer", "/viewOrders", "/cart", "/home"));
        roleAccessMap.put("shipper", Arrays.asList("/OrderDetailForShipperController", "/viewOrders", "/cart", "/home"));
        roleAccessMap.put("guest", Arrays.asList("/login", "/register", "/home"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // Ensure session is not null and check for account attribute
        Account account = null;
        if (session != null) {
            account = (Account) session.getAttribute("account");
        }

        // Handle case when account is null
        String role = (account != null && account.getRole() != null) ? account.getRole() : "guest";

        // Get requested URI
        String path = req.getServletPath();

        // Check if the role has access to the page
        if (isAuthorized(role, path)) {
            chain.doFilter(request, response);
        } else {
            // Redirect to home if not authorized
            res.sendRedirect(req.getContextPath() + "/home");
        }
    }

    @Override
    public void destroy() {
    }

    private boolean isAuthorized(String role, String path) {
        // Check if the role is authorized to access the requested path
        List<String> accessiblePaths = roleAccessMap.get(role);
        return accessiblePaths != null && accessiblePaths.contains(path);
    }
}
