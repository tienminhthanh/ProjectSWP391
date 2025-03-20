/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.oracle.wls.shaded.org.apache.xpath.axes.LocPathIterator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class EventDAO {

    private utils.DBContext context;

    public EventDAO() {
        context = new utils.DBContext();
    }

//    public List<Event>
    public List<Event> getEventByPage(int page, int pageSize, String filtered, String searchKeyword) {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT * FROM [dbo].[Event] WHERE 1=1"; // Mặc định có điều kiện true

        List<Object> paramsList = new ArrayList<>();

        // Lọc theo trạng thái (isActive)
        if (filtered != null && !filtered.isEmpty()) {
            sql += " AND eventIsActive = ?";
            paramsList.add(Boolean.parseBoolean(filtered));
        }

        // Tìm kiếm theo tất cả các cột
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql += " AND (eventName LIKE ? OR description LIKE ? OR CAST(eventID AS VARCHAR) LIKE ?)";
            String keywordPattern = "%" + searchKeyword.trim() + "%";
            paramsList.add(keywordPattern);
            paramsList.add(keywordPattern);
            paramsList.add(keywordPattern);
        }

        // Thêm phân trang
        sql += " ORDER BY eventID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        paramsList.add((page - 1) * pageSize);
        paramsList.add(pageSize);

        try {
            Object[] params = paramsList.toArray();
            ResultSet rs = context.exeQuery(sql, params);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String dateCreated = rs.getString(3);
                int duration = rs.getInt(4);
                String banner = rs.getString(5);
                String description = rs.getString(6);
                int adminID = rs.getInt(7);
                boolean isActive = rs.getBoolean(8);
                String dateStarted_raw = rs.getString(9);

                LocalDate today = LocalDate.now();
                LocalDate dateStarted = LocalDate.parse(dateStarted_raw, formatter);
                LocalDate expiryDate = dateStarted.plusDays(duration);

                Event event = new Event(id, name, dateCreated, duration, banner, description, adminID, isActive, dateStarted_raw, !today.isAfter(expiryDate));
                list.add(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalEvent(String searchKeyword, String filtered) {
        String sql = "SELECT COUNT(*) FROM [dbo].[Event] WHERE 1=1";
        List<Object> paramsList = new ArrayList<>();

        // Lọc theo trạng thái (isActive)
        if (filtered != null && !filtered.isEmpty()) {
            sql += " AND eventIsActive = ?";
            paramsList.add(Boolean.parseBoolean(filtered));
        }

        // Tìm kiếm theo từ khóa
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql += " AND (eventName LIKE ? OR description LIKE ? OR CAST(eventID AS VARCHAR) LIKE ?)";
            String keywordPattern = "%" + searchKeyword.trim() + "%";
            paramsList.add(keywordPattern);
            paramsList.add(keywordPattern);
            paramsList.add(keywordPattern);
        }

        try {
            Object[] params = paramsList.toArray();
            ResultSet rs = context.exeQuery(sql, params);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Event> getListEvent() {
        List<Event> listEvent = new ArrayList<>();
        String sql = "SELECT * FROM [dbo].[Event]";
        try {

            ResultSet rs = context.exeQuery(sql, null);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String dateCreated = rs.getString(3);
                int duration = rs.getInt(4);
                String banner = rs.getString(5);
                String description = rs.getString(6);
                int adminID = rs.getInt(7);
                boolean isActive = rs.getBoolean(8);
                String dateStarted = rs.getString(9);
                LocalDate createDate = LocalDate.parse(dateCreated, formatter);
                LocalDate expiryDate = createDate.plusDays(duration);
                Event event = new Event(id, name, dateCreated, duration, banner, description, adminID, isActive, dateStarted, !LocalDate.now().isAfter(expiryDate));
                listEvent.add(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listEvent;
    }

    public List<Event> getListActiveEvents() {
        List<Event> listEvent = new ArrayList<>();
        String sql = "SELECT * FROM [dbo].[Event] where eventIsActive = 1";
        try {

            ResultSet rs = context.exeQuery(sql, null);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String dateCreated = rs.getString(3);
                int duration = rs.getInt(4);
                String banner = rs.getString(5);
                String description = rs.getString(6);
                int adminID = rs.getInt(7);
                boolean isActive = rs.getBoolean(8);
                String dateStarted = rs.getString(9);
                //Handling null
                LocalDate expiryDate = dateStarted != null ? LocalDate.parse(dateStarted, formatter).plusDays(duration) : LocalDate.EPOCH;
                Event event = new Event(id, name, dateCreated, duration, banner, description, adminID, isActive, dateStarted, !LocalDate.now().isAfter(expiryDate));
                //Only add non-expired events
                if (event.isExpiry()) {
                    listEvent.add(event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listEvent;
    }

    public Event getEventByID(int eventID) {
        try {
            String sql = "SELECT * FROM [dbo].[Event] WHERE [eventID] = ?";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Object[] params = {eventID};
            ResultSet rs = context.exeQuery(sql, params);
            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String dateCreated = rs.getString(3);
                int duration = rs.getInt(4);
                String banner = rs.getString(5);
                String description = rs.getString(6);
                int adminID = rs.getInt(7);
                boolean isActive = rs.getBoolean(8);
                String dateStarted_raw = rs.getString(9);

                LocalDate today = LocalDate.now();
                LocalDate dateStarted = LocalDate.parse(dateStarted_raw, formatter);
                LocalDate expiryDate = dateStarted.plusDays(duration);
                return new Event(id, name, dateCreated, duration, banner, description, adminID, isActive, dateStarted_raw, !today.isAfter(expiryDate));
            }
        } catch (Exception e) {
        }
        return null;
    }

    public List<String> getBannerEvent() {
        String sql = "SELECT [banner]\n"
                + "  FROM [dbo].[Event]"
                + "  WHERE [eventIsActive] = 1"
                + "  ORDER BY [eventDateStarted]";
        List<String> listBanner = new ArrayList<>();
        try {
            ResultSet rs = context.exeQuery(sql, null);
            while (rs.next()) {
                String banner = rs.getString(1);
//                if (!banner.startsWith("img/")) {
//                    banner = "img/banner_event/" + banner;
//                }
                listBanner.add(banner);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listBanner;
    }

    public Event getEventByBanner(String currentBanner) {
        try {
            String sql = "SELECT * FROM [dbo].[Event] WHERE [banner] = ?";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Object[] params = {currentBanner};
            ResultSet rs = context.exeQuery(sql, params);
            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String dateCreated = rs.getString(3);
                int duration = rs.getInt(4);
                String banner = rs.getString(5);
                String description = rs.getString(6);
                int adminID = rs.getInt(7);
                boolean isActive = rs.getBoolean(8);
                String dateStarted = rs.getString(9);

                LocalDate createDate = LocalDate.parse(dateCreated, formatter);
                LocalDate expiryDate = createDate.plusDays(duration);
                return new Event(id, name, dateCreated, duration, banner, description, adminID, isActive, dateStarted, !LocalDate.now().isAfter(expiryDate));
            }
        } catch (Exception e) {
        }
        return null;
    }

    public boolean deleteEvent(int id) {
        try {
            Event event = getEventByID(id);
            LocalDate today = LocalDate.now();

            String sql = "UPDATE [dbo].[Event]\n"
                    + "   SET [eventIsActive] = ?\n"
                    + " WHERE [eventID] = ?";
            Object[] params = {!event.isIsActive(), id};
            int rowsAffected = context.exeNonQuery(sql, params);
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean unlockEvent(int id) {
        try {
            Event event = getEventByID(id);
            LocalDate today = LocalDate.now();

            if ((!event.isExpiry() && !event.isIsActive())) {
                return false;
            } else if (today.isBefore(LocalDate.parse(event.getDateStarted()))) {
                return false;
            }

            String sql = "UPDATE [dbo].[Event]\n"
                    + "   SET [eventIsActive] = ?\n"
                    + " WHERE [eventID] = ?";
            Object[] params = {!event.isIsActive(), id};
            int rowsAffected = context.exeNonQuery(sql, params);
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean addEvent(Event event) {
        try {
            String sql = "INSERT INTO [dbo].[Event]\n"
                    + "           ([eventName]\n"
                    + "           ,[eventDateCreated]\n"
                    + "           ,[eventDuration]\n"
                    + "           ,[banner]\n"
                    + "           ,[description]\n"
                    + "           ,[adminID]\n"
                    + "           ,[eventIsActive]\n"
                    + "           ,[eventDateStarted])\n"
                    + "     VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//            String banner = event.getBanner();
//            if (!banner.startsWith("img/")) {
//                banner = "img/banner_event/" + banner;
//            }
            Object[] params = {event.getEventName(),
                event.getDateCreated(),
                event.getDuration(),
                event.getBanner(),
                event.getDescription(),
                event.getAdminID(),
                event.isIsActive(),
                event.getDateStarted()
            };
            int rowsAffected = context.exeNonQuery(sql, params);
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public boolean updateEvent(Event event) {
        String sql = "UPDATE [dbo].[Event]\n"
                + "   SET [eventName] = ?\n"
                + "      ,[eventDuration] = ?\n"
                + "      ,[banner] = ?\n"
                + "      ,[description] = ?\n"
                + "      ,[eventDateStarted] = ?\n"
                + "      ,[eventIsActive] = ?\n"
                + " WHERE [eventID] = ?";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate today = LocalDate.now();
            LocalDate createDate = LocalDate.parse(event.getDateStarted(), formatter);
            LocalDate expiryDate = createDate.plusDays(event.getDuration());
            String banner = event.getBanner();
            if (!banner.startsWith("img/")) {
                banner = "img/banner_event/" + banner;
            }

            boolean isActive = false;
            if (!(today.isAfter(expiryDate)) && LocalDate.parse(event.getDateStarted()).isEqual(today)) {
                isActive = true;
            }

            Object params[] = {event.getEventName(),
                event.getDuration(),
                banner,
                event.getDescription(),
                event.getDateStarted(),
                isActive,
                event.getEventID()};

            int rowsAffected = context.exeNonQuery(sql, params);
            return rowsAffected > 0;

        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static void main(String[] args) {
        EventDAO eDao = new EventDAO();
        System.out.println(eDao.deleteEvent(6));
    }
}