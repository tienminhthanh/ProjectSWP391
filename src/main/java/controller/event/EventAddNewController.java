/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.event;

import dao.EventDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import model.Account;
import model.Event;

/**
 *
 * @author ADMIN
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB: Nếu file lớn hơn 2MB, nó sẽ lưu vào disk
        maxFileSize = 1024 * 1024 * 10, // 10MB: Giới hạn kích thước file
        maxRequestSize = 1024 * 1024 * 50 // 50MB: Giới hạn tổng dung lượng request
)
@WebServlet(name = "EventAddNewController", urlPatterns = {"/eventAddNew"})
public class EventAddNewController extends HttpServlet {

    private final String EVENT_ADDNEW_PAGE = "eventAddNew.jsp";
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
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EventAddNewController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EventAddNewController at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher(EVENT_ADDNEW_PAGE).forward(request, response);
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
        HttpSession session = request.getSession();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        EventDAO eDao = new EventDAO();

        try {
            boolean isMultipart = request.getContentType() != null && request.getContentType().startsWith("multipart/");

            String name = "";
            String banner = "";
            String dateStarted_raw = "";
            LocalDate dateStarted = null;
            LocalDate dateCreated = null;
            String description = "";
            int duration = 0;

            if (isMultipart) {
                Collection<Part> parts = request.getParts();
                for (Part part : parts) {
                    String partName = part.getName();
                    String value = new String(part.getInputStream().readAllBytes());

                    switch (partName) {
                        case "eventName":
                            name = value;
                            break;
                        case "dateStarted":
                            dateStarted_raw = value;
                            dateStarted = LocalDate.parse(dateStarted_raw, formatter);
                            break;
                        case "description":
                            description = value;
                            break;
                        case "duration":
                            duration = Integer.parseInt(value);
                            break;
                    }
                }
            } else {
                name = request.getParameter("eventName");
                banner = request.getParameter("bannerFile");
                dateStarted_raw = request.getParameter("dateStarted");
                dateStarted_raw = request.getParameter("dateStarted");
                dateStarted = LocalDate.parse(dateStarted_raw, formatter);
                duration = Integer.parseInt(request.getParameter("duration"));
            }

            dateCreated = LocalDate.now();

            Account account = (Account) session.getAttribute("account");
            if (account == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            int adminID = account.getAccountID();

            Part photo = request.getPart("bannerFile");
            if (photo != null && photo.getSize() > 0) {
                String uploadPath = getServletContext().getRealPath("/img/banner_event/");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String originalFileName = Paths.get(photo.getSubmittedFileName()).getFileName().toString();
                File existingFile = new File(uploadDir, originalFileName);
                if (existingFile.exists()) {
                    request.setAttribute("error", "Ảnh đã tồn tại, vui lòng đổi tên file!");
                    request.getRequestDispatcher("eventUpdate.jsp").forward(request, response);
                    return;
                }
                File newFile = new File(uploadDir, originalFileName);
                photo.write(newFile.getAbsolutePath());
                banner = "/img/banner_event/" + originalFileName;
            }

            Event event = new Event(name, dateCreated.toString(), duration, banner, description, adminID, true, dateStarted.toString(), true);
            boolean add = eDao.addEvent(event);
            if (add) {
                session.setAttribute("message", "Event created successfully!");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Failed to create event.");
                session.setAttribute("messageType", "error");
            }
        } catch (Exception e) {
            session.setAttribute("message", "Error: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }
        response.sendRedirect(EVENT_LIST_PAGE);

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
