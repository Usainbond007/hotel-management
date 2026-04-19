# Alpine Chalet — Hotel Management System

A JavaFX desktop application for hotel management built with Java 21, JavaFX 21, MySQL 8 and Maven.

## Setup Instructions

### Prerequisites
- Java 21 (Temurin)
- Maven
- MySQL 8

### Database Setup
Open MySQL and run the following:

```sql
CREATE DATABASE IF NOT EXISTS hotel_db;

USE hotel_db;

CREATE TABLE rooms (
    room_number INT PRIMARY KEY,
    room_type ENUM('Single', 'Double', 'Deluxe', 'Suite') NOT NULL,
    price_per_day DOUBLE NOT NULL,
    status ENUM('Available', 'Occupied') DEFAULT 'Available'
);

CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(20) NOT NULL,
    room_number INT,
    FOREIGN KEY (room_number) REFERENCES rooms(room_number)
);

CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    room_number INT NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    total_cost DOUBLE,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (room_number) REFERENCES rooms(room_number)
);
```

### Configuration
1. Copy `src/main/resources/db.properties.example` to `src/main/resources/db.properties`
2. Fill in your MySQL credentials:

```properties
db.url=jdbc:mysql://localhost:3306/hotel_db
db.user=your_username
db.password=your_password
```

### Running the App
mvn clean javafx:run
### Login Credentials
- Username: `admin`
- Password: `admin123`

## Features
- Login system with credential validation
- Dashboard with live statistics
- Room management with search and delete
- Customer management with validation
- Booking system with date validation
- Automatic room release on checkout date
- Billing and PDF invoice generation
- Search and filter across all modules
- Logout functionality

## Tech Stack
- Java 21
- JavaFX 21
- MySQL 8
- JDBC
- Maven
- iText 5 (PDF generation)
