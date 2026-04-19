package com.hotel.model;

import java.time.LocalDate;

public class Booking {
    private int id;
    private int customerId;
    private int roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalCost;

    public Booking(int id, int customerId, int roomNumber, LocalDate checkIn, LocalDate checkOut, double totalCost) {
        this.id = id;
        this.customerId = customerId;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalCost = totalCost;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public int getRoomNumber() { return roomNumber; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public double getTotalCost() { return totalCost; }

    public void setId(int id) { this.id = id; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
}