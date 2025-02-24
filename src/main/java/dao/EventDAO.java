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

    private static utils.DBContext context;

    public EventDAO() {
        context = new utils.DBContext();
    }

//    public static void getListEvent() {
//        List<Event> listEvent = new ArrayList<>();
//        String sql = "SELECT * FROM [dbo].[Event]";
//        try {
//
//            ResultSet rs = context.exeQuery(sql, null);
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            } catch (Exception e) {
//            System.out.println("hello");
//        }
//            while (rs.next()) {
//                String name = rs.getString(1);
//                System.out.println(name);
////                String dateCreated = rs.getString(2);
////                System.out.println(dateCreated);
////                int duration = rs.getInt(3);
////                System.out.println(duration);
////                String banner = rs.getString(4);
////                System.out.println(banner);
////                String description = rs.getString(5);
////                System.out.println(description);
////                int adminID = rs.getInt(6);
////                System.out.println(adminID);
//
////                LocalDate createDate = LocalDate.parse(dateCreated, formatter);
////                LocalDate expiryDate = createDate.plusDays(duration);
////                Event event = new Event(name, dateCreated, duration, banner, description, adminID, !LocalDate.now().isAfter(expiryDate));
////                listEvent.add(event);
//            }
//        
//    }
//
//    public static void main(String[] args) {
//        getListEvent();
//    }
}
