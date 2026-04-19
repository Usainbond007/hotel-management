package com.hotel.dao;

import com.hotel.model.Room;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class RoomDAO {

    public boolean addRoom(Room room) {
        String sql = "INSERT INTO rooms (room_number, room_type, price_per_day, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, room.getRoomNumber());
            stmt.setString(2, room.getRoomType());
            stmt.setDouble(3, room.getPricePerDay());
            stmt.setString(4, room.getStatus());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding room: " + e.getMessage());
            return false;
        }
    }
    public boolean deleteRoom(int roomNumber) {
    String sql = "DELETE FROM rooms WHERE room_number = ?";
    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
        stmt.setInt(1, roomNumber);
        stmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        System.err.println("Error deleting room: " + e.getMessage());
        return false;
    }
}

public boolean hasActiveBookings(int roomNumber) {
    String sql = "SELECT COUNT(*) FROM bookings WHERE room_number = ?";
    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
        stmt.setInt(1, roomNumber);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getInt(1) > 0;
    } catch (SQLException e) {
        System.err.println("Error checking bookings: " + e.getMessage());
    }
    return false;
}

public boolean hasActiveCustomers(int roomNumber) {
    String sql = "SELECT COUNT(*) FROM customers WHERE room_number = ?";
    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
        stmt.setInt(1, roomNumber);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getInt(1) > 0;
    } catch (SQLException e) {
        System.err.println("Error checking customers: " + e.getMessage());
    }
    return false;
}
  public List<Room> searchRooms(String keyword) {
    List<Room> rooms = new ArrayList<>();
    String sql = "SELECT * FROM rooms WHERE CAST(room_number AS CHAR) LIKE ? OR room_type LIKE ? OR status LIKE ?";
    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
        String k = "%" + keyword + "%";
        stmt.setString(1, k);
        stmt.setString(2, k);
        stmt.setString(3, k);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            rooms.add(new Room(
                rs.getInt("room_number"),
                rs.getString("room_type"),
                rs.getDouble("price_per_day"),
                rs.getString("status")
            ));
        }
    } catch (SQLException e) {
        System.err.println("Error searching rooms: " + e.getMessage());
    }
    return rooms;
}
public void autoReleaseExpiredRooms() {
    String sql = "UPDATE rooms SET status = 'Available' WHERE room_number IN " +
                 "(SELECT DISTINCT room_number FROM bookings WHERE check_out < ?)";
    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
        stmt.setDate(1, Date.valueOf(LocalDate.now()));
        stmt.executeUpdate();
    } catch (SQLException e) {
        System.err.println("Error auto-releasing rooms: " + e.getMessage());
    }
}
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(new Room(
                    rs.getInt("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price_per_day"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching rooms: " + e.getMessage());
        }
        return rooms;
    }

    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status = 'Available'";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rooms.add(new Room(
                    rs.getInt("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price_per_day"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching available rooms: " + e.getMessage());
        }
        return rooms;
    }

    public boolean updateRoomStatus(int roomNumber, String status) {
        String sql = "UPDATE rooms SET status = ? WHERE room_number = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, roomNumber);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating room status: " + e.getMessage());
            return false;
        }
    }

    public Room getRoomByNumber(int roomNumber) {
        String sql = "SELECT * FROM rooms WHERE room_number = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Room(
                    rs.getInt("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price_per_day"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching room: " + e.getMessage());
        }
        return null;
    }
}