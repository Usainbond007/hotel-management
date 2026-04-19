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

### Login System
The application opens with a secure login screen styled with a dark alpine gradient background and a frosted glass card. Staff must enter valid credentials before accessing any functionality. Invalid login attempts show an error message and clear the password field. On successful login the system transitions to the main dashboard. A logout button in the sidebar returns the user to the login screen at any time.

### Dashboard
The dashboard is the home screen after login and displays seven live statistics pulled directly from the database in real time. These include total rooms, available rooms, occupied rooms, total registered guests, today's check-ins, today's check-outs, and total revenue generated across all bookings. A Refresh button allows staff to manually update the stats at any time. The dashboard also automatically triggers the room release mechanism on every load to ensure occupancy data is always accurate.

### Room Management
Staff can add new rooms by specifying a room number, room type and price per day. The system enforces strict validation — room numbers must be between 1 and 9999, prices must be greater than zero, duplicate room numbers are rejected, and a room type must be selected from the dropdown. Rooms can be viewed in full or filtered to show only available rooms. A search bar allows staff to search across room number, type and status simultaneously using partial keyword matching. Rooms can be deleted by selecting a row in the table and clicking Delete Selected — the system prevents deletion if the room has active customers or existing bookings, and shows a confirmation dialog before proceeding.

### Customer Management
Staff can register new customers by entering their full name, contact number and assigned room number. The system validates that names contain only letters and spaces between 2 and 50 characters, contact numbers are exactly 10 digits, the assigned room exists in the database, and the room is not currently occupied. Customers can be searched by name or contact number using partial keyword matching. Removing a customer automatically releases their assigned room back to Available and deletes all associated booking records to maintain database integrity.

### Booking Management
The booking system allows staff to book a room for a registered customer with check-in and check-out dates. Six layers of validation are enforced before a booking is saved. Check-in and check-out dates must both be selected. Check-out must be after check-in. Check-in cannot be in the past. Check-in cannot be more than one year in the future to prevent unrealistic advance bookings. The room must exist in the database. The room must not have any overlapping bookings for the selected dates. If the check-in date is today or earlier the room status is automatically set to Occupied. Future bookings are recorded but the room remains Available until the check-in date arrives. The booking table can be filtered by date range to view bookings within a specific period.

### Automatic Room Release
Every time the application starts, the dashboard is opened, or a new booking is made, the system automatically scans all bookings and sets any room whose checkout date has passed back to Available. This means staff never need to manually free up rooms — the system handles it automatically based on the dates recorded at booking time.

### Checkout
Staff can check out a guest by entering the room number in the Bookings tab and clicking Checkout. The system verifies the room exists and is currently Occupied, then sets its status back to Available. All tables and the dashboard are refreshed immediately after checkout to reflect the updated state.

### Billing
The billing module generates a formatted receipt for any guest by entering their room number and customer ID. The system fetches the most recent booking for that combination from the database, calculates the duration of stay in nights using the check-in and check-out dates, and displays a formatted receipt showing the customer name, contact number, room number and type, check-in and check-out dates, duration, nightly rate and total cost. Both the room number and customer ID are required to generate a bill, preventing accidental billing of the wrong guest.

### PDF Invoice Generation
After generating a bill on screen, staff can click Download Invoice to generate a professional PDF invoice saved directly to the Desktop. The PDF is styled with the Alpine Chalet branding — navy blue header with the hotel name and tagline, a unique invoice number, guest details and room details in side-by-side cards, an itemised stay details table showing check-in date, check-out date, number of nights and nightly rate, a highlighted total amount section, and a footer with contact information. The system automatically detects whether the Desktop is in a standard location or inside an OneDrive folder and saves to the correct path. The PDF filename includes the customer name and room number for easy identification.

### Search and Filter
Every module has its own search or filter capability. The Rooms tab has a search bar that matches against room number, type and status simultaneously. The Customers tab has a search bar that matches against customer name and contact number. The Bookings tab has two date pickers that filter bookings by check-in and check-out date range. All searches use SQL LIKE queries with wildcard matching so partial keywords work correctly. A Clear button on each search resets the view back to all records.

### Sidebar Navigation
The main application uses a dark navy sidebar for navigation between five sections — Dashboard, Rooms, Customers, Bookings and Billing. Clicking a sidebar button highlights it as active and switches the content area to the corresponding page without loading a new screen. This is achieved using a StackPane where all pages are loaded at startup and only one is visible at a time. The sidebar also contains a Logout button at the bottom that returns the user to the login screen.

## Architecture
The project follows the MVC (Model View Controller) design pattern with a dedicated DAO (Data Access Object) layer for database communication.

- **Model** — Plain Java classes representing Room, Customer and Booking entities
- **View** — FXML files defining the UI layout with a separate CSS stylesheet for styling
- **Controller** — LoginController and MainController handling all user interactions and business logic
- **DAO** — RoomDAO, CustomerDAO, BookingDAO and DashboardDAO handling all SQL queries using PreparedStatements
- **DBConnection** — Singleton class managing the MySQL database connection loaded from db.properties

## Tech Stack
- Java 21
- JavaFX 21
- MySQL 8
- JDBC
- Maven
- iText 5 (PDF generation)
