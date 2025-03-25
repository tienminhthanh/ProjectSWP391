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
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "EventDeleteServlet", urlPatterns = {"/eventDelete"})
public class EventDeleteController extends HttpServlet {

    private final String EVENT_LIST_PAGE = "eventList";

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
        HttpSession session = request.getSession();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String action = request.getParameter("action");
            EventDAO eDao = new EventDAO();
            EventProductDAO epDao = new EventProductDAO();
            if (action.equals("delete")) {
                if (epDao.deleteListProductInEvent(id) && eDao.deleteEvent(id)) {
                    session.setAttribute("message", "Event and Products deleted successfully!");
                    session.setAttribute("messageType", "success");
                } else if (!(epDao.deleteListProductInEvent(id)) && eDao.deleteEvent(id)) {
                    session.setAttribute("message", "Event deleted successfully!");
                    session.setAttribute("messageType", "error");
                } else {
                    session.setAttribute("message", "Failed to delete Event.");
                    session.setAttribute("messageType", "error");
                }
            } else if (action.equals("unlock")) {
                if (eDao.unlockEvent(id)) {
                    session.setAttribute("message", "Event unlock successfully!");
                    session.setAttribute("messageType", "success");
                } else {
                    session.setAttribute("message", "Failed to unlock Event.");
                    session.setAttribute("messageType", "error");
                }
            }

        } catch (Exception e) {
            session.setAttribute("message", "Error: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }

        response.sendRedirect(url);
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
