/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

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

    public List<Event> getEventByPage(int page, int pageSize) {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT * FROM [dbo].[Event] ORDER BY eventID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {

            ResultSet rs = context.exeQuery(sql, new Object[]{(page - 1) * pageSize, pageSize});
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
                list.add(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalEvent() {
        String sql = "SELECT COUNT(*) FROM Event";
        try {
            ResultSet rs = context.exeQuery(sql, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
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
        String sql = "SELECT * FROM [dbo].[Event] where isActive = 1";
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
                String dateStarted = rs.getString(9);

                LocalDate createDate = LocalDate.parse(dateCreated, formatter);
                LocalDate expiryDate = createDate.plusDays(duration);
                return new Event(id, name, dateCreated, duration, banner, description, adminID, isActive, dateStarted, !LocalDate.now().isAfter(expiryDate));
            }
        } catch (Exception e) {
        }
        return null;
    }

    public List<String> getBannerEvent() {
        String sql = "SELECT [banner]\n"
                + "  FROM [dbo].[Event]"
                + "  WHERE [isActive] = 1"
                + "  ORDER BY [dateStarted]";
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

            if (!event.isExpiry() && !event.isIsActive()) {
                return false;
            }

            String sql = "UPDATE [dbo].[Event]\n"
                    + "   SET [isActive] = ?\n"
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
                    + "           ,[dateCreated]\n"
                    + "           ,[duration]\n"
                    + "           ,[banner]\n"
                    + "           ,[description]\n"
                    + "           ,[adminID]\n"
                    + "           ,[isActive]\n"
                    + "           ,[dateStarted])\n"
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
                + "      ,[duration] = ?\n"
                + "      ,[banner] = ?\n"
                + "      ,[description] = ?\n"
                + "      ,[dateStarted] = ?\n"
                + "      ,[isActive] = ?\n"
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
            Object params[] = {event.getEventName(),
                event.getDuration(),
                banner,
                event.getDescription(),
                event.getDateStarted(),
                (LocalDate.now().isAfter(expiryDate) || LocalDate.parse(event.getDateStarted()).isAfter(today)) ? false : true,
                event.getEventID()};

            event.setExpiry(!LocalDate.now().isAfter(expiryDate));

            int rowsAffected = context.exeNonQuery(sql, params);
            return rowsAffected > 0;

        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static void main(String[] args) {
    }
}
