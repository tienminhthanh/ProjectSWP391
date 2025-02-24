/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Event;
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

                LocalDate createDate = LocalDate.parse(dateCreated, formatter);
                LocalDate expiryDate = createDate.plusDays(duration);
                Event event = new Event(id, name, dateCreated, duration, banner, description, adminID, !LocalDate.now().isAfter(expiryDate));
                listEvent.add(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listEvent;
    }

    public Event getEventByID(int eventID) {
        try {
            String sql = "SELECT * FROM [dbo].[Event] WHERE [voucherID] = ?";
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

                LocalDate createDate = LocalDate.parse(dateCreated, formatter);
                LocalDate expiryDate = createDate.plusDays(duration);

                return new Event(id, name, dateCreated, duration, banner, description, adminID, !LocalDate.now().isAfter(expiryDate));
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static void main(String[] args) {
        EventDAO e = new EventDAO();
        Event en = e.getEventByID(45);;
        if (e.getEventByID(45).getDescription().isEmpty()) {
            System.out.println("null");
        }
    }
}
