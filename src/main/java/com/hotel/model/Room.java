package com.hotel.model;

public class Room {
    private int roomNumber;
    private String roomType;
    private double pricePerDay;
    private String status;

    public Room(int roomNumber, String roomType, double pricePerDay, String status) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerDay = pricePerDay;
        this.status = status;
    }

    public int getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public double getPricePerDay() { return pricePerDay; }
    public String getStatus() { return status; }

    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }
    public void setStatus(String status) { this.status = status; }
}