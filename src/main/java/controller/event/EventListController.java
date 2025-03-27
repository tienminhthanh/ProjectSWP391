/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.event;

import dao.*;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Event;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "EventListServlet", urlPatterns = {"/eventList"})
public class EventListController extends HttpServlet {

    private final String EVENT_LIST_PAGE = "eventList.jsp";

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
        String url = EVENT_LIST_PAGE;
        int page = 1;
        int pageSize = 5;
        String pageStr = request.getParameter("page");
        String isActiveParam = request.getParameter("isActive");
        String searchKeyword = request.getParameter("search");
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (Exception e) {
                page = 1;
            }
        }
        try {
            EventDAO eDao = new EventDAO();
            EventProductDAO epDao = new EventProductDAO();
            List<Event> listEvent = eDao.getEventByPage(page, pageSize, isActiveParam, searchKeyword);
            int totalEvent = eDao.getTotalEvent(searchKeyword, isActiveParam);
            int totalPages = (int) Math.ceil((double) totalEvent / pageSize);

            // 🔥 Tạo Map để lưu số lượng sản phẩm của mỗi sự kiện
            Map<Integer, Integer> eventProductCountMap = new HashMap<>();

            // 🌟 Lặp qua danh sách sự kiện và lấy số lượng sản phẩm cho từng event
            for (Event event : listEvent) {
                int eventID = event.getEventID();
                int productCount = epDao.getTotalProductInAnEvent(eventID);
                eventProductCountMap.put(eventID, productCount);
            }

//            Boolean isActive = null;
//            if (isActiveParam != null && !isActiveParam.isEmpty()) {
//                isActive = Boolean.parseBoolean(isActiveParam);
//            }

            request.setAttribute("EVENT_PRODUCT_COUNT", eventProductCountMap);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPage", totalPages);
            request.setAttribute("LIST_EVENT", listEvent);
            request.setAttribute("searchKeyword", searchKeyword);
            request.setAttribute("isActiveParam", isActiveParam);

        } catch (Exception ex) {
            log("VoucherListServlet error:" + ex.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
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
