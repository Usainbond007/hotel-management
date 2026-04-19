package com.hotel.dao;

import com.hotel.model.Booking;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public boolean addBooking(Booking booking) {
        String sql = "INSERT INTO bookings (customer_id, room_number, check_in, check_out, total_cost) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, booking.getCustomerId());
            stmt.setInt(2, booking.getRoomNumber());
            stmt.setDate(3, Date.valueOf(booking.getCheckIn()));
            stmt.setDate(4, Date.valueOf(booking.getCheckOut()));
            stmt.setDouble(5, booking.getTotalCost());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding booking: " + e.getMessage());
            return false;
        }
    }
    public Booking getBookingByRoomAndCustomer(int roomNumber, int customerId) {
    String sql = "SELECT * FROM bookings WHERE room_number = ? AND customer_id = ? ORDER BY id DESC LIMIT 1";
    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
        stmt.setInt(1, roomNumber);
        stmt.setInt(2, customerId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Booking(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                rs.getInt("room_number"),
                rs.getDate("check_in").toLocalDate(),
                rs.getDate("check_out").toLocalDate(),
                rs.getDouble("total_cost")
            );
        }
    } catch (SQLException e) {
        System.err.println("Error fetching booking: " + e.getMessage());
    }
    return null;
}
public List<Booking> filterBookingsByDate(LocalDate from, LocalDate to) {
    List<Booking> bookings = new ArrayList<>();
    String sql = "SELECT * FROM bookings WHERE check_in >= ? AND check_out <= ?";
    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
        stmt.setDate(1, Date.valueOf(from));
        stmt.setDate(2, Date.valueOf(to));
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            bookings.add(new Booking(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                rs.getInt("room_number"),
                rs.getDate("check_in").toLocalDate(),
                rs.getDate("check_out").toLocalDate(),
                rs.getDouble("total_cost")
            ));
        }
    } catch (SQLException e) {
        System.err.println("Error filtering bookings: " + e.getMessage());
    }
    return bookings;
}
    public boolean hasOverlappingBooking(int roomNumber, LocalDate checkIn, LocalDate checkOut) {
    String sql = "SELECT COUNT(*) FROM bookings WHERE room_number = ? AND check_in < ? AND check_out > ?";
    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
        stmt.setInt(1, roomNumber);
        stmt.setDate(2, Date.valueOf(checkOut));
        stmt.setDate(3, Date.valueOf(checkIn));
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        System.err.println("Error checking overlapping bookings: " + e.getMessage());
    }
    return false;
}
    public boolean deleteByCustomerId(int customerId) {
    String sql = "DELETE FROM bookings WHERE customer_id = ?";
    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
        stmt.setInt(1, customerId);
        stmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        System.err.println("Error deleting booking: " + e.getMessage());
        return false;
    }
}

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bookings.add(new Booking(
                    rs.getInt("id"),
                    rs.getInt("customer_id"),
                    rs.getInt("room_number"),
                    rs.getDate("check_in").toLocalDate(),
                    rs.getDate("check_out").toLocalDate(),
                    rs.getDouble("total_cost")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings: " + e.getMessage());
        }
        return bookings;
    }

    public Booking getBookingByRoomNumber(int roomNumber) {
        String sql = "SELECT * FROM bookings WHERE room_number = ? ORDER BY id DESC LIMIT 1";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Booking(
                    rs.getInt("id"),
                    rs.getInt("customer_id"),
                    rs.getInt("room_number"),
                    rs.getDate("check_in").toLocalDate(),
                    rs.getDate("check_out").toLocalDate(),
                    rs.getDouble("total_cost")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching booking: " + e.getMessage());
        }
        return null;
    }
}