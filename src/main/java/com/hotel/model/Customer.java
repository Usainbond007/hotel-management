package com.hotel.model;

public class Customer {
    private int id;
    private String name;
    private String contact;
    private int roomNumber;

    public Customer(int id, String name, String contact, int roomNumber) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.roomNumber = roomNumber;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public int getRoomNumber() { return roomNumber; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setContact(String contact) { this.contact = contact; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }
}