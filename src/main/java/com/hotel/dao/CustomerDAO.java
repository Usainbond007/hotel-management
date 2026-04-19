package com.hotel.dao;

import com.hotel.model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (name, contact, room_number) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getContact());
            stmt.setInt(3, customer.getRoomNumber());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
            return false;
        }
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("contact"),
                    rs.getInt("room_number")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching customers: " + e.getMessage());
        }
        return customers;
    }

    public boolean removeCustomer(int id) {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error removing customer: " + e.getMessage());
            return false;
        }
    }
   public List<Customer> searchCustomers(String keyword) {
    List<Customer> customers = new ArrayList<>();
    String sql = "SELECT * FROM customers WHERE name LIKE ? OR contact LIKE ?";
    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
        String k = "%" + keyword + "%";
        stmt.setString(1, k);
        stmt.setString(2, k);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            customers.add(new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("contact"),
                rs.getInt("room_number")
            ));
        }
    } catch (SQLException e) {
        System.err.println("Error searching customers: " + e.getMessage());
    }
    return customers;
}
    public Customer getCustomerById(int id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("contact"),
                    rs.getInt("room_number")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching customer: " + e.getMessage());
        }
        return null;
    }
}