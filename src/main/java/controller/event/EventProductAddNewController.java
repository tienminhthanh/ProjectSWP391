/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.*;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "EventProductAddNewController", urlPatterns = {"/eventProductAddNew"})
public class EventProductAddNewController extends HttpServlet {

    private final String EVENT_PRODUCT_ADDNEW_PAGE = "eventProductAddNew.jsp";

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
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EventProductAddNewController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EventProductAddNewController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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

//        ProductDAO pDao = new ProductDAO();
        EventProductDAO epDao = new EventProductDAO();
        EventDAO eDao = new EventDAO();

        try {
            int id = Integer.parseInt(request.getParameter("eventId"));
            String category = request.getParameter("category");
            String searchKeyword = request.getParameter("search");
            String eventName = eDao.getEventByID(id).getEventName();
            request.setAttribute("eventName", eventName);

            List<Product> listProductForEvent = epDao.getListProductToAddForEvent(id, searchKeyword, category);
            request.setAttribute("productList", listProductForEvent);
            request.getRequestDispatcher(EVENT_PRODUCT_ADDNEW_PAGE).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int eventId;
        try {
            eventId = Integer.parseInt(request.getParameter("eventId"));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid event ID.");
            request.getRequestDispatcher("eventProductAddNew.jsp").forward(request, response);
            return;
        }
//discount data
        HttpSession session = request.getSession();

        // Lấy danh sách sản phẩm đã chọn (dùng getParameterValues để lấy nhiều giá trị)
        String[] selectedProductsArr = request.getParameterValues("selectedProducts");
        String discountDataStr = request.getParameter("discountData");
        String currentURL = request.getParameter("currentURL");

        // Kiểm tra danh sách sản phẩm có hợp lệ không
        if (selectedProductsArr == null || selectedProductsArr.length == 0) {
            request.setAttribute("errorMessage", "No products selected.");
            request.getRequestDispatcher("eventProductAddNew.jsp").forward(request, response);
            return;
        }

        // Chuyển danh sách sản phẩm thành Set để loại bỏ trùng lặp
        Set<Integer> productSet = new HashSet<>();
        try {
            for (String id : selectedProductsArr) {
                productSet.add(Integer.parseInt(id.trim()));
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid product ID format.");
            request.getRequestDispatcher("eventProductAddNew.jsp").forward(request, response);
            return;
        }

        // Kiểm tra nếu không có sản phẩm hợp lệ
        if (productSet.isEmpty()) {
            request.setAttribute("errorMessage", "No valid products selected.");
            request.getRequestDispatcher("eventProductAddNew.jsp").forward(request, response);
            return;
        }

        // Chuyển chuỗi JSON discountData thành Map<Integer, Integer>
        Map<Integer, Integer> discountMap = new HashMap<>();
        if (discountDataStr != null && !discountDataStr.trim().isEmpty()) {
            try {
                discountMap = parseDiscountData(discountDataStr);
                // Chỉ giữ lại các discount liên quan đến sản phẩm đã chọn
                discountMap.keySet().removeIf(key -> !productSet.contains(key));
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Invalid discount data format: " + e.getMessage());
                request.getRequestDispatcher("eventProductAddNew.jsp").forward(request, response);
                return;
            }
        }

        // Gọi DAO để thêm sản phẩm vào sự kiện
        EventProductDAO epDao = new EventProductDAO();
        List<Integer> failedProducts = new ArrayList<>();

        for (int productId : productSet) {
            int discountPercent = discountMap.getOrDefault(productId, 0);
            if (discountPercent < 0 || discountPercent > 99) {
                failedProducts.add(productId);
                continue; // Không return sớm, tiếp tục xử lý sản phẩm khác
            }

            boolean isAdded = epDao.addProductToEvent(eventId, productId, discountPercent);
            if (!isAdded) {
                failedProducts.add(productId);
            }

            System.out.println("Added Product ID: " + productId + " | Discount: " + discountPercent + "%");
        }

        // Nếu có sản phẩm thất bại, báo lỗi nhưng vẫn tiếp tục với những sản phẩm thành công
        if (!failedProducts.isEmpty()) {
            request.setAttribute("errorMessage", "Failed to add some products: " + failedProducts);
            request.getRequestDispatcher("eventProductAddNew.jsp").forward(request, response);
            return;
        }

        session.setAttribute("message", "Product added successfully!");
        session.setAttribute("messageType", "success");
        if (currentURL != null && !currentURL.trim().isEmpty()) {
            response.sendRedirect(currentURL);
        } else {
            response.sendRedirect("eventDetails?eventId=" + eventId);
        }
    }

    private Map<Integer, Integer> parseDiscountData(String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Integer, Integer> discountMap = new HashMap<>();

        try {
            // Parse JSON thành Map<String, String>
            Map<String, String> tempMap = objectMapper.readValue(jsonString, new TypeReference<Map<String, String>>() {
            });

            // Chuyển đổi key và value thành Integer, đồng thời kiểm tra giá trị hợp lệ
            for (Map.Entry<String, String> entry : tempMap.entrySet()) {
                try {
                    int productId = Integer.parseInt(entry.getKey());
                    int discount = Integer.parseInt(entry.getValue());
                    if (discount < 0 || discount > 99) {
                        throw new IllegalArgumentException("Discount for product ID " + productId + " must be between 0 and 99.");
                    }
                    discountMap.put(productId, discount);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number format in discount data for key: " + entry.getKey() + " or value: " + entry.getValue());
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to parse discount data: " + jsonString + " - Error: " + e.getMessage());
            throw e; // Ném lại ngoại lệ để doPost xử lý
        }

        return discountMap;
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
