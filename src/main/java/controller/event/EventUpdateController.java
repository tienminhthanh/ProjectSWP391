/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.event;

import dao.EventDAO;
import dao.EventProductDAO;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
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
@WebServlet(name = "EventUpdateServlet", urlPatterns = {"/eventUpdate"})
public class EventUpdateController extends HttpServlet {

    private final String EVENT_UPDATE_PAGE = "eventUpdate.jsp";
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
        String url = EVENT_UPDATE_PAGE;
        try {
            int id = Integer.parseInt(request.getParameter("eventID"));
            EventDAO eDao = new EventDAO();
            Event eventDetails = eDao.getEventByID(id);
            request.setAttribute("EVENT_DETAILS", eventDetails);
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
        String url = EVENT_LIST_PAGE;
        HttpSession session = request.getSession();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        EventDAO eDao = new EventDAO();
//        Event e = new Event();

        try {
            boolean isMultipart = request.getContentType() != null && request.getContentType().startsWith("multipart/");

            int id = 0;
            String name = "";
            String description = "";
            String dateCreated = "";
            int duration = 0;
            int adminID = 0;
            String dateStarted_raw = "";
            LocalDate dateStarted = null;
            String banner = "";
            String fileName = "";

            if (isMultipart) {
                Collection<Part> parts = request.getParts();
                for (Part part : parts) {
                    String partName = part.getName();
                    String value = new String(part.getInputStream().readAllBytes()).trim();

                    switch (partName) {
                        case "eventID":
                            id = Integer.parseInt(value);
                            break;
                        case "eventName":
                            name = value;
                            break;
                        case "description":
                            description = value;
                            break;
                        case "duration":
                            duration = Integer.parseInt(value);
                            break;
                        case "dateStarted":
                            dateStarted_raw = value;
                            dateStarted = LocalDate.parse(dateStarted_raw, formatter);
                            break;
                    }
                }
            } else {
                id = Integer.parseInt(request.getParameter("eventID"));
                name = request.getParameter("eventName");
                description = request.getParameter("description");
                duration = Integer.parseInt(request.getParameter("duration"));
                dateStarted_raw = request.getParameter("dateStarted");
                dateStarted = LocalDate.parse(dateStarted_raw, formatter);
            }

            // Lấy thông tin từ database nếu cần
            Event existingEvent = eDao.getEventByID(id);
            if (existingEvent != null) {
                dateCreated = existingEvent.getDateCreated();
                adminID = existingEvent.getAdminID();
                banner = existingEvent.getBanner();
            }

            Part part = request.getPart("bannerFile");
            String fileName_raw = Path.of(part.getSubmittedFileName()).getFileName().toString();
            if (fileName_raw != null && !fileName_raw.isEmpty()) {
                ServletContext context = getServletContext();
                // Lấy đường dẫn thư mục lưu ảnh trong dự án
                String realPath = request.getServletContext().getRealPath("/img/banner_event");

                if (realPath.contains("target")) {
                    realPath = context.getRealPath("").replace("\\", "/")
                            .replaceAll("target/.*", "") + "src/main/webapp/img/banner_event";
                }
                // Kiểm tra và tạo thư mục nếu chưa tồn tại
                File directory = new File(realPath);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                // Ghi file vào thư mục trong dự án
                File file = new File(directory, fileName_raw);
                part.write(file.getAbsolutePath());

                System.out.println("File saved at: " + file.getAbsolutePath());
                fileName = "img/banner_event/" + fileName_raw;
            } else {
                fileName = banner;
            }

            LocalDate today = LocalDate.now();
            LocalDate createDate = LocalDate.parse(dateStarted.toString(), formatter);
            LocalDate expiryDate = createDate.plusDays(duration);

            boolean isActive = false;
            //ngày hết hạn >= hôm nay               ngày bắt đầu <= hôm nay
            LocalDate startDate = LocalDate.parse(dateStarted.toString());
            if (!expiryDate.isBefore(today) && (startDate.isBefore(today) || startDate.isEqual(today))) {
                isActive = true;
            }

            // Tạo đối tượng Event và cập nhật
            Event event = new Event(id, name, dateCreated, duration, fileName, description, adminID,
                    isActive, dateStarted.toString(), existingEvent.isExpiry());

            EventProductDAO epDAO = new EventProductDAO();
            if (eDao.updateEvent(event)) {
                session.setAttribute("message", "Event updated successfully!");
                session.setAttribute("messageType", "success");
                if (!event.isExpiry()) {
                    epDAO.deleteListProductInEvent(event.getEventID());
                }
            } else {
                session.setAttribute("message", "Failed to update event.");
                session.setAttribute("messageType", "error");
            }
        } catch (Exception ex) {
            session.setAttribute("message", "Error: " + ex.getMessage());
            session.setAttribute("messageType", "error");
        }

        response.sendRedirect(url);
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
