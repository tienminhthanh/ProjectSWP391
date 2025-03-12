/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.event;

import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.*;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "EventDetailsServlet", urlPatterns = {"/eventDetails"})
public class EventDetailsController extends HttpServlet {

    private final String EVENT_DETAILS_ADMIN_PAGE = "eventDetailsAdmin.jsp";
    private final String EVENT_DETAILS_CUS_PAGE = "eventDetailsCus.jsp";

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
        /* TODO output your page here. You may use following sample code. */
        String url = EVENT_DETAILS_ADMIN_PAGE;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            String action = request.getParameter("action");
            if (action == null) {
                int id = Integer.parseInt(request.getParameter("eventId"));
                EventDAO eDao = new EventDAO();
                Event eventDetails = eDao.getEventByID(id);
                request.setAttribute("EVENT_DETAILS", eventDetails);

                String dateStarted = eventDetails.getDateStarted();
                LocalDate createDate = LocalDate.parse(dateStarted, formatter);
                LocalDate dateEnd = createDate.plusDays(eventDetails.getDuration());
                request.setAttribute("dateEnd", dateEnd);
            } else if (action.equals("home")) {
                ProductDAO productDAO = new ProductDAO();
                List<Product> productList = productDAO.getProductsByCondition(0, "hotDeal", null, "sale", "book", "");
                request.setAttribute("productList", productList);

                String banner = request.getParameter("banner");
                if (banner != null) {
                    banner = URLDecoder.decode(banner, StandardCharsets.UTF_8);
                }
                request.setAttribute("banner", banner);
                EventDAO eDao = new EventDAO();;
                Event event = eDao.getEventByBanner(banner);
                request.setAttribute("eventDetails", event);

                String dateStarted = event.getDateStarted();
                LocalDate createDate = LocalDate.parse(dateStarted, formatter);
                LocalDate dateEnd = createDate.plusDays(event.getDuration());
                request.setAttribute("dateEnd", dateEnd);
                url = EVENT_DETAILS_CUS_PAGE;
            }

        } catch (Exception ex) {
            log("VoucherDetailsServlet error:" + ex.getMessage());
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
