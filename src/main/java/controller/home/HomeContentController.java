/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.home;

import dao.CartItemDAO;
import dao.NotificationDAO;
import dao.ProductDAO;
import dao.VoucherDAO;
import dao.interfaces.IGeneralProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Account;
import model.CartItem;
import model.Notification;
import model.Voucher;
import model.Genre;
import model.NonEntityClassification;
import model.Product;
import model.Series;
import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
@WebServlet({"/home"})
public class HomeContentController extends HttpServlet {

    private final CartItemDAO cartDAO = CartItemDAO.getInstance();
    private final NotificationDAO notiDAO = NotificationDAO.getInstance();
    private final VoucherDAO vDao = VoucherDAO.getInstance();
    private final IGeneralProductDAO productDAO = ProductDAO.getInstance();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        Account account = session != null ? (Account) session.getAttribute("account") : null;
        String role = account != null ? account.getRole() : "guest";
        switch (role.toLowerCase()) {
            case "admin":
                response.sendRedirect("listAccount");
                return;
            case "staff":
                response.sendRedirect("OrderListForStaffController");
                return;
            case "shipper":
                response.sendRedirect("OrderListForShipperController");
                return;
            case "customer":
            case "guest":
            default:
                break;
        }
        handleHomepage(request, response);
    }

    private void handleHomepage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        try {
            if (account != null && account.getRole().equals("customer")) {
                List<CartItem> listCart = session.getAttribute("cartItems") != null
                        ? (List<CartItem>) session.getAttribute("cartItems")
                        : cartDAO.getCartItemsByCustomer(account.getAccountID());
                session.setAttribute("cartItems", listCart);

                List<Notification> listNoti = session.getAttribute("notifications") != null
                        ? (List<Notification>) session.getAttribute("notifications")
                        : notiDAO.getNotificationsByReceiverDESC(account.getAccountID());
                session.setAttribute("notifications", listNoti);
            }
            showProductsInHomepage(request);
            showVouchersInHomepage(request);
            showVouchersAvalaibleInHomepage(request);
            request.getRequestDispatcher("home.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

    }

    private void showProductsInHomepage(HttpServletRequest request) throws Exception {
        IProductClassification newBookClsf = new NonEntityClassification("book", "new");
        IProductClassification newMerchClsf = new NonEntityClassification("merch", "new");
        IProductClassification saleBookClsf = new NonEntityClassification("book", "sale");
        IProductClassification saleMerchClsf = new NonEntityClassification("merch", "sale");
        IProductClassification animeBookClsf = new Genre().setGenreID(18);
        IProductClassification holoMerchClsf = new Series().setSeriesID(1);

        List<Product> newBookHome = productDAO.getClassifiedProductList(newBookClsf, "releaseDate", null, 1, 7, true);
        List<Product> newMerchHome = productDAO.getClassifiedProductList(newMerchClsf, "releaseDate", null, 1, 7, true);
        List<Product> saleBookHome = productDAO.getClassifiedProductList(saleBookClsf, "hotDeal", null, 1, 7, true);
        List<Product> saleMerchHome = productDAO.getClassifiedProductList(saleMerchClsf, "hotDeal", null, 1, 7, true);
        List<Product> animeBookHome = productDAO.getClassifiedProductList(animeBookClsf, "releaseDate", null, 1, 7, true);
        List<Product> holoMerchHome = productDAO.getClassifiedProductList(holoMerchClsf, "releaseDate", null, 1, 7, true);

        request.setAttribute("newBookHome", newBookHome);
        request.setAttribute("newMerchHome", newMerchHome);
        request.setAttribute("saleBookHome", saleBookHome);
        request.setAttribute("saleMerchHome", saleMerchHome);
        request.setAttribute("animeBookHome", animeBookHome);
        request.setAttribute("holoMerchHome", holoMerchHome);

    }

//    private void showBannerInHomepage(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        List<String> bannerList = eDao.getBannerEvent();
//
//        if (bannerList.isEmpty()) {
//            throw new Exception("Found no products in the catalog!");
//        }
//        request.setAttribute("bannerList", bannerList);
//    }
    private void showVouchersInHomepage(HttpServletRequest request) throws Exception {

        List<Voucher> listVoucher = vDao.getListVoucherAvailableNow();
        request.setAttribute("listVoucher", listVoucher);

    }

    private void showVouchersAvalaibleInHomepage(HttpServletRequest request) throws Exception {

        List<Voucher> listVoucher = vDao.getListVoucherComeSoon();
        request.setAttribute("listVoucherComeSoon", listVoucher);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
