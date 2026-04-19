package com.hotel.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DashboardDAO {

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        Connection conn = DBConnection.getConnection();

        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM rooms");
            if (rs.next()) stats.put("totalRooms", rs.getInt(1));

            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM rooms WHERE status = 'Available'");
            if (rs.next()) stats.put("availableRooms", rs.getInt(1));

            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM rooms WHERE status = 'Occupied'");
            if (rs.next()) stats.put("occupiedRooms", rs.getInt(1));

            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM customers");
            if (rs.next()) stats.put("totalCustomers", rs.getInt(1));

            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM bookings WHERE check_in = ?");
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            rs = ps.executeQuery();
            if (rs.next()) stats.put("todayCheckIns", rs.getInt(1));

            ps = conn.prepareStatement("SELECT COUNT(*) FROM bookings WHERE check_out = ?");
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            rs = ps.executeQuery();
            if (rs.next()) stats.put("todayCheckOuts", rs.getInt(1));

            rs = conn.createStatement().executeQuery("SELECT COALESCE(SUM(total_cost), 0) FROM bookings");
            if (rs.next()) stats.put("totalRevenue", rs.getDouble(1));

        } catch (SQLException e) {
            System.err.println("Error fetching dashboard stats: " + e.getMessage());
        }

        return stats;
    }
}