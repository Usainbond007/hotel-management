package com.hotel.controller;

import com.hotel.InvoiceGenerator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.hotel.dao.BookingDAO;
import com.hotel.dao.CustomerDAO;
import com.hotel.dao.DashboardDAO;
import com.hotel.dao.RoomDAO;
import com.hotel.model.Booking;
import com.hotel.model.Customer;
import com.hotel.model.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class MainController {

    // ─── Billing Fields ─────────────────────────────────────────
    @FXML private TextField billingCustomerIdField;
    @FXML private TextField billingRoomField;
    @FXML private TextArea billOutput;
    @FXML private Button downloadInvoiceBtn;

    // ─── Room Fields ─────────────────────────────────────────────
    @FXML private TextField roomNumberField;
    @FXML private ComboBox<String> roomTypeCombo;
    @FXML private TextField roomPriceField;
    @FXML private TableView<Room> roomTable;
    @FXML private TableColumn<Room, Integer> roomNumberCol;
    @FXML private TableColumn<Room, String> roomTypeCol;
    @FXML private TableColumn<Room, Double> roomPriceCol;
    @FXML private TableColumn<Room, String> roomStatusCol;
    @FXML private Label roomStatusLabel;
    @FXML private TextField roomSearchField;

    // ─── Customer Fields ─────────────────────────────────────────
    @FXML private TextField customerNameField;
    @FXML private TextField customerContactField;
    @FXML private TextField customerRoomField;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> customerIdCol;
    @FXML private TableColumn<Customer, String> customerNameCol;
    @FXML private TableColumn<Customer, String> customerContactCol;
    @FXML private TableColumn<Customer, Integer> customerRoomCol;
    @FXML private Label customerStatusLabel;
    @FXML private TextField customerSearchField;

    // ─── Booking Fields ──────────────────────────────────────────
    @FXML private TextField bookingCustomerIdField;
    @FXML private TextField bookingRoomField;
    @FXML private DatePicker checkInPicker;
    @FXML private DatePicker checkOutPicker;
    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, Integer> bookingIdCol;
    @FXML private TableColumn<Booking, Integer> bookingCustomerCol;
    @FXML private TableColumn<Booking, Integer> bookingRoomCol;
    @FXML private TableColumn<Booking, String> bookingCheckInCol;
    @FXML private TableColumn<Booking, String> bookingCheckOutCol;
    @FXML private TableColumn<Booking, Double> bookingCostCol;
    @FXML private Label bookingStatusLabel;
    @FXML private DatePicker searchCheckInPicker;
    @FXML private DatePicker searchCheckOutPicker;

    // ─── Pages ───────────────────────────────────────────────────
    @FXML private VBox dashboardPage;
    @FXML private VBox roomsPage;
    @FXML private VBox customersPage;
    @FXML private VBox bookingsPage;
    @FXML private VBox billingPage;

    // ─── Sidebar Buttons ─────────────────────────────────────────
    @FXML private Button btnDashboard;
    @FXML private Button btnRooms;
    @FXML private Button btnCustomers;
    @FXML private Button btnBookings;
    @FXML private Button btnBilling;

    // ─── Dashboard Labels ────────────────────────────────────────
    @FXML private Label statTotalRooms;
    @FXML private Label statAvailableRooms;
    @FXML private Label statOccupiedRooms;
    @FXML private Label statTotalCustomers;
    @FXML private Label statCheckIns;
    @FXML private Label statCheckOuts;
    @FXML private Label statRevenue;

    // ─── DAOs ────────────────────────────────────────────────────
    private final RoomDAO roomDAO = new RoomDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final DashboardDAO dashboardDAO = new DashboardDAO();

    // ─── Current bill data for invoice ───────────────────────────
    private Customer currentBillCustomer;
    private Room currentBillRoom;
    private Booking currentBillBooking;

    // ─── Initialize ──────────────────────────────────────────────
    @FXML
    public void initialize() {
        roomDAO.autoReleaseExpiredRooms();

        roomNumberCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        roomPriceCol.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));
        roomStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        customerRoomCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));

        bookingIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookingCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        bookingRoomCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        bookingCheckInCol.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        bookingCheckOutCol.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
        bookingCostCol.setCellValueFactory(new PropertyValueFactory<>("totalCost"));

        roomTypeCombo.setItems(FXCollections.observableArrayList("Single", "Double", "Deluxe", "Suite"));

        handleViewAllRooms();
        handleViewCustomers();
        handleViewBookings();

        showDashboard();
    }

    // ─── Navigation ──────────────────────────────────────────────

    @FXML
    private void showDashboard() {
        roomDAO.autoReleaseExpiredRooms();
        setActivePage(dashboardPage, btnDashboard);
        refreshDashboard();
    }

    @FXML
    private void showRooms() {
        setActivePage(roomsPage, btnRooms);
    }

    @FXML
    private void showCustomers() {
        setActivePage(customersPage, btnCustomers);
    }

    @FXML
    private void showBookings() {
        setActivePage(bookingsPage, btnBookings);
    }

    @FXML
    private void showBilling() {
        setActivePage(billingPage, btnBilling);
    }

    private void setActivePage(VBox page, Button activeBtn) {
        dashboardPage.setVisible(false);
        roomsPage.setVisible(false);
        customersPage.setVisible(false);
        bookingsPage.setVisible(false);
        billingPage.setVisible(false);

        btnDashboard.getStyleClass().remove("sidebar-button-active");
        btnRooms.getStyleClass().remove("sidebar-button-active");
        btnCustomers.getStyleClass().remove("sidebar-button-active");
        btnBookings.getStyleClass().remove("sidebar-button-active");
        btnBilling.getStyleClass().remove("sidebar-button-active");

        page.setVisible(true);
        activeBtn.getStyleClass().add("sidebar-button-active");
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            Stage stage = (Stage) btnRooms.getScene().getWindow();
            stage.setTitle("Alpine Chalet");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ─── Dashboard ───────────────────────────────────────────────

    @FXML
    private void handleRefreshDashboard() {
        refreshDashboard();
    }

    private void refreshDashboard() {
        Map<String, Object> stats = dashboardDAO.getStats();
        statTotalRooms.setText(String.valueOf(stats.get("totalRooms")));
        statAvailableRooms.setText(String.valueOf(stats.get("availableRooms")));
        statOccupiedRooms.setText(String.valueOf(stats.get("occupiedRooms")));
        statTotalCustomers.setText(String.valueOf(stats.get("totalCustomers")));
        statCheckIns.setText(String.valueOf(stats.get("todayCheckIns")));
        statCheckOuts.setText(String.valueOf(stats.get("todayCheckOuts")));
        statRevenue.setText(String.format("₹%.2f", stats.get("totalRevenue")));
    }

    // ─── Room Handlers ───────────────────────────────────────────

    @FXML
    private void handleAddRoom() {
        try {
            String roomNumberText = roomNumberField.getText().trim();
            String priceText = roomPriceField.getText().trim();
            String type = roomTypeCombo.getValue();

            if (roomNumberText.isEmpty() || priceText.isEmpty()) {
                showStatus(roomStatusLabel, "All fields are required.", false);
                return;
            }

            int number = Integer.parseInt(roomNumberText);
            double price = Double.parseDouble(priceText);

            if (number <= 0) {
                showStatus(roomStatusLabel, "Room number must be greater than 0.", false);
                return;
            }

            if (number > 9999) {
                showStatus(roomStatusLabel, "Room number must be between 1 and 9999.", false);
                return;
            }

            if (price <= 0) {
                showStatus(roomStatusLabel, "Price must be greater than 0.", false);
                return;
            }

            if (type == null) {
                showStatus(roomStatusLabel, "Please select a room type.", false);
                return;
            }

            Room existing = roomDAO.getRoomByNumber(number);
            if (existing != null) {
                showStatus(roomStatusLabel, "Room " + number + " already exists.", false);
                return;
            }

            Room room = new Room(number, type, price, "Available");
            if (roomDAO.addRoom(room)) {
                showStatus(roomStatusLabel, "Room added successfully!", true);
                clearRoomFields();
                handleViewAllRooms();
                handleViewCustomers();
                handleViewBookings();
            } else {
                showStatus(roomStatusLabel, "Failed to add room.", false);
            }
        } catch (NumberFormatException e) {
            showStatus(roomStatusLabel, "Room number and price must be valid numbers.", false);
        }
    }

    @FXML
    private void handleViewAllRooms() {
        ObservableList<Room> rooms = FXCollections.observableArrayList(roomDAO.getAllRooms());
        roomTable.setItems(rooms);
    }

    @FXML
    private void handleViewAvailable() {
        ObservableList<Room> rooms = FXCollections.observableArrayList(roomDAO.getAvailableRooms());
        roomTable.setItems(rooms);
        showStatus(roomStatusLabel, "Showing available rooms only.", true);
    }

    @FXML
    private void handleDeleteRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus(roomStatusLabel, "Please select a room to delete.", false);
            return;
        }

        if (roomDAO.hasActiveCustomers(selected.getRoomNumber())) {
            showStatus(roomStatusLabel, "Cannot delete — room has active customers.", false);
            return;
        }

        if (roomDAO.hasActiveBookings(selected.getRoomNumber())) {
            showStatus(roomStatusLabel, "Cannot delete — room has existing bookings.", false);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Room");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete Room " + selected.getRoomNumber() + "?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                if (roomDAO.deleteRoom(selected.getRoomNumber())) {
                    showStatus(roomStatusLabel, "Room deleted successfully!", true);
                    handleViewAllRooms();
                    handleViewCustomers();
                    handleViewBookings();
                    refreshDashboard();
                } else {
                    showStatus(roomStatusLabel, "Failed to delete room.", false);
                }
            }
        });
    }

    @FXML
    private void handleRoomSearch() {
        String keyword = roomSearchField.getText().trim();
        if (keyword.isEmpty()) {
            handleViewAllRooms();
            return;
        }
        ObservableList<Room> rooms = FXCollections.observableArrayList(roomDAO.searchRooms(keyword));
        roomTable.setItems(rooms);
        showStatus(roomStatusLabel, rooms.size() + " result(s) found.", true);
    }

    @FXML
    private void handleClearRoomSearch() {
        roomSearchField.clear();
        handleViewAllRooms();
        showStatus(roomStatusLabel, "", true);
    }

    // ─── Customer Handlers ───────────────────────────────────────

    @FXML
    private void handleAddCustomer() {
        try {
            String name = customerNameField.getText().trim();
            String contact = customerContactField.getText().trim();
            String roomText = customerRoomField.getText().trim();

            if (name.isEmpty()) {
                showStatus(customerStatusLabel, "Name cannot be empty.", false);
                return;
            }

            if (!name.matches("[a-zA-Z ]{2,50}")) {
                showStatus(customerStatusLabel, "Name must contain only letters and spaces (2-50 chars).", false);
                return;
            }

            if (!contact.matches("\\d{10}")) {
                showStatus(customerStatusLabel, "Contact must be exactly 10 digits.", false);
                return;
            }

            if (roomText.isEmpty()) {
                showStatus(customerStatusLabel, "Room number cannot be empty.", false);
                return;
            }

            int roomNumber = Integer.parseInt(roomText);

            Room room = roomDAO.getRoomByNumber(roomNumber);
            if (room == null) {
                showStatus(customerStatusLabel, "Room " + roomNumber + " does not exist.", false);
                return;
            }

            if (room.getStatus().equals("Occupied")) {
                showStatus(customerStatusLabel, "Room " + roomNumber + " is currently occupied. Please choose another room.", false);
                return;
            }

            Customer customer = new Customer(0, name, contact, roomNumber);
            if (customerDAO.addCustomer(customer)) {
                showStatus(customerStatusLabel, "Customer added successfully!", true);
                clearCustomerFields();
                handleViewCustomers();
                handleViewAllRooms();
                handleViewBookings();
            } else {
                showStatus(customerStatusLabel, "Failed to add customer.", false);
            }
        } catch (NumberFormatException e) {
            showStatus(customerStatusLabel, "Invalid room number.", false);
        }
    }

    @FXML
    private void handleViewCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList(customerDAO.getAllCustomers());
        customerTable.setItems(customers);
    }

    @FXML
    private void handleRemoveCustomer() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus(customerStatusLabel, "Please select a customer to remove.", false);
            return;
        }

        roomDAO.updateRoomStatus(selected.getRoomNumber(), "Available");
        bookingDAO.deleteByCustomerId(selected.getId());

        if (customerDAO.removeCustomer(selected.getId())) {
            showStatus(customerStatusLabel, "Customer removed successfully!", true);
            handleViewAllRooms();
            handleViewCustomers();
            handleViewBookings();
        } else {
            showStatus(customerStatusLabel, "Failed to remove customer.", false);
        }
    }

    @FXML
    private void handleCustomerSearch() {
        String keyword = customerSearchField.getText().trim();
        if (keyword.isEmpty()) {
            handleViewCustomers();
            return;
        }
        ObservableList<Customer> customers = FXCollections.observableArrayList(customerDAO.searchCustomers(keyword));
        customerTable.setItems(customers);
        showStatus(customerStatusLabel, customers.size() + " result(s) found.", true);
    }

    @FXML
    private void handleClearCustomerSearch() {
        customerSearchField.clear();
        handleViewCustomers();
        showStatus(customerStatusLabel, "", true);
    }

    // ─── Booking Handlers ────────────────────────────────────────

    @FXML
    private void handleBookRoom() {
        roomDAO.autoReleaseExpiredRooms();
        showStatus(bookingStatusLabel, "", true);

        try {
            int customerId = Integer.parseInt(bookingCustomerIdField.getText().trim());
            int roomNumber = Integer.parseInt(bookingRoomField.getText().trim());
            LocalDate checkIn = checkInPicker.getValue();
            LocalDate checkOut = checkOutPicker.getValue();

            if (checkIn == null || checkOut == null) {
                showStatus(bookingStatusLabel, "Please select check-in and check-out dates.", false);
                return;
            }

            if (!checkOut.isAfter(checkIn)) {
                showStatus(bookingStatusLabel, "Check-out must be after check-in.", false);
                return;
            }

            if (checkIn.isBefore(LocalDate.now())) {
                showStatus(bookingStatusLabel, "Check-in date cannot be in the past.", false);
                return;
            }

            if (checkIn.isAfter(LocalDate.now().plusYears(1))) {
                showStatus(bookingStatusLabel, "Bookings cannot be made more than 1 year in advance.", false);
                return;
            }

            Room room = roomDAO.getRoomByNumber(roomNumber);
            if (room == null) {
                showStatus(bookingStatusLabel, "Room not found.", false);
                return;
            }

            if (bookingDAO.hasOverlappingBooking(roomNumber, checkIn, checkOut)) {
                showAlert("Booking Conflict", "Room " + roomNumber + " is already booked for the selected dates. Please choose different dates.");
                return;
            }

            long days = ChronoUnit.DAYS.between(checkIn, checkOut);
            double totalCost = days * room.getPricePerDay();

            Booking booking = new Booking(0, customerId, roomNumber, checkIn, checkOut, totalCost);
            if (bookingDAO.addBooking(booking)) {
                if (!checkIn.isAfter(LocalDate.now())) {
                    roomDAO.updateRoomStatus(roomNumber, "Occupied");
                }
                showStatus(bookingStatusLabel, "Room booked successfully! Total: ₹" + totalCost, true);
                clearBookingFields();
                handleViewAllRooms();
                handleViewCustomers();
                handleViewBookings();
                refreshDashboard();
            } else {
                showStatus(bookingStatusLabel, "Failed to book room.", false);
            }
        } catch (NumberFormatException e) {
            showStatus(bookingStatusLabel, "Invalid input. Check Customer ID and Room Number.", false);
        }
    }

    @FXML
    private void handleCheckout() {
        try {
            int roomNumber = Integer.parseInt(bookingRoomField.getText().trim());
            Room room = roomDAO.getRoomByNumber(roomNumber);

            if (room == null) {
                showStatus(bookingStatusLabel, "Room not found.", false);
                return;
            }
            if (room.getStatus().equals("Available")) {
                showStatus(bookingStatusLabel, "Room is already available.", false);
                return;
            }

            roomDAO.updateRoomStatus(roomNumber, "Available");
            showStatus(bookingStatusLabel, "Checkout successful! Room " + roomNumber + " is now available.", true);
            clearBookingFields();
            handleViewAllRooms();
            handleViewCustomers();
            handleViewBookings();
        } catch (NumberFormatException e) {
            showStatus(bookingStatusLabel, "Invalid room number.", false);
        }
    }

    @FXML
    private void handleViewBookings() {
        ObservableList<Booking> bookings = FXCollections.observableArrayList(bookingDAO.getAllBookings());
        bookingTable.setItems(bookings);
    }

    @FXML
    private void handleBookingFilter() {
        LocalDate from = searchCheckInPicker.getValue();
        LocalDate to = searchCheckOutPicker.getValue();

        if (from == null || to == null) {
            showStatus(bookingStatusLabel, "Please select both from and to dates.", false);
            return;
        }

        if (to.isBefore(from)) {
            showStatus(bookingStatusLabel, "To date must be after from date.", false);
            return;
        }

        ObservableList<Booking> bookings = FXCollections.observableArrayList(bookingDAO.filterBookingsByDate(from, to));
        bookingTable.setItems(bookings);
        showStatus(bookingStatusLabel, bookings.size() + " booking(s) found.", true);
    }

    @FXML
    private void handleClearBookingFilter() {
        searchCheckInPicker.setValue(null);
        searchCheckOutPicker.setValue(null);
        handleViewBookings();
        showStatus(bookingStatusLabel, "", true);
    }

    // ─── Billing Handlers ────────────────────────────────────────

    @FXML
    private void handleGenerateBill() {
        try {
            String roomText = billingRoomField.getText().trim();
            String customerText = billingCustomerIdField.getText().trim();

            if (roomText.isEmpty() || customerText.isEmpty()) {
                billOutput.setText("Please enter both Room Number and Customer ID.");
                downloadInvoiceBtn.setDisable(true);
                return;
            }

            int roomNumber = Integer.parseInt(roomText);
            int customerId = Integer.parseInt(customerText);

            Booking booking = bookingDAO.getBookingByRoomAndCustomer(roomNumber, customerId);
            Room room = roomDAO.getRoomByNumber(roomNumber);
            Customer customer = customerDAO.getCustomerById(customerId);

            if (room == null) {
                billOutput.setText("Room " + roomNumber + " does not exist.");
                downloadInvoiceBtn.setDisable(true);
                return;
            }

            if (customer == null) {
                billOutput.setText("Customer ID " + customerId + " does not exist.");
                downloadInvoiceBtn.setDisable(true);
                return;
            }

            if (booking == null) {
                billOutput.setText("No booking found for Room " + roomNumber + " and Customer ID " + customerId + ".");
                downloadInvoiceBtn.setDisable(true);
                return;
            }

            // Store for invoice generation
            currentBillCustomer = customer;
            currentBillRoom = room;
            currentBillBooking = booking;

            long days = ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());

            String bill = """
                    ╔══════════════════════════════════╗
                    ║       HOTEL BILLING RECEIPT       ║
                    ╚══════════════════════════════════╝

                    Customer  : %s
                    Contact   : %s
                    Room No.  : %d (%s)
                    Check-In  : %s
                    Check-Out : %s
                    Duration  : %d night(s)
                    Rate      : ₹%.2f / night

                    ──────────────────────────────────
                    TOTAL     : ₹%.2f
                    ──────────────────────────────────
                    Thank you for staying with us!
                    """.formatted(
                    customer.getName(),
                    customer.getContact(),
                    room.getRoomNumber(),
                    room.getRoomType(),
                    booking.getCheckIn(),
                    booking.getCheckOut(),
                    days,
                    room.getPricePerDay(),
                    booking.getTotalCost()
            );

            billOutput.setText(bill);
            downloadInvoiceBtn.setDisable(false);

        } catch (NumberFormatException e) {
            billOutput.setText("Please enter valid Room Number and Customer ID.");
            downloadInvoiceBtn.setDisable(true);
        }
    }

    @FXML
    private void handleDownloadInvoice() {
        try {
            String filePath = InvoiceGenerator.generateInvoice(
                currentBillCustomer,
                currentBillRoom,
                currentBillBooking
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invoice Downloaded");
            alert.setHeaderText(null);
            alert.setContentText("Invoice saved successfully to:\n" + filePath);
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invoice Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to generate invoice: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    // ─── Helpers ─────────────────────────────────────────────────

    private void showStatus(Label label, String message, boolean success) {
        label.setText(message);
        label.setStyle(success
                ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
                : "-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearRoomFields() {
        roomNumberField.clear();
        roomTypeCombo.setValue(null);
        roomPriceField.clear();
    }

    private void clearCustomerFields() {
        customerNameField.clear();
        customerContactField.clear();
        customerRoomField.clear();
    }

    private void clearBookingFields() {
        bookingCustomerIdField.clear();
        bookingRoomField.clear();
        checkInPicker.setValue(null);
        checkOutPicker.setValue(null);
    }
}
