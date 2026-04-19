package com.hotel;

import com.hotel.model.Booking;
import com.hotel.model.Customer;
import com.hotel.model.Room;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import com.itextpdf.text.pdf.draw.LineSeparator;
public class InvoiceGenerator {

    public static String generateInvoice(Customer customer, Room room, Booking booking) throws Exception {

    // ─── File path ───────────────────────────────────────────
String userHome = System.getProperty("user.home");

// Try OneDrive Desktop first, fall back to regular Desktop
String desktopPath;
java.io.File onedriveDesktop = new java.io.File(userHome + "/OneDrive/Desktop/");
if (onedriveDesktop.exists()) {
    desktopPath = userHome + "/OneDrive/Desktop/";
} else {
    desktopPath = userHome + "/Desktop/";
}

String fileName = "Invoice_" + customer.getName().replace(" ", "_") + "_Room" + room.getRoomNumber() + ".pdf";
String filePath = desktopPath + fileName;
        // ─── Colors ───────────────────────────────────────────────
        BaseColor navyBlue = new BaseColor(26, 42, 74);
        BaseColor iceBlue = new BaseColor(74, 144, 217);
        BaseColor lightGray = new BaseColor(240, 244, 248);
        BaseColor darkGray = new BaseColor(100, 100, 100);

        // ─── Fonts ────────────────────────────────────────────────
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 28, Font.BOLD, BaseColor.WHITE);
        Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.WHITE);
        Font sectionFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, navyBlue);
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, darkGray);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
        Font totalFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, darkGray);
        Font invoiceNoFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.WHITE);

        // ─── Document setup ───────────────────────────────────────
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // ─── Header ───────────────────────────────────────────────
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);

        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(navyBlue);
        headerCell.setPadding(30);
        headerCell.setBorder(Rectangle.NO_BORDER);

        Paragraph hotelName = new Paragraph("Alpine Chalet", titleFont);
        hotelName.setAlignment(Element.ALIGN_CENTER);

        Paragraph hotelTagline = new Paragraph("MOUNTAIN RESORT & SPA", subtitleFont);
        hotelTagline.setAlignment(Element.ALIGN_CENTER);

        Paragraph invoiceNo = new Paragraph("INVOICE #INV-" + booking.getId() + "-" + LocalDate.now().getYear(), invoiceNoFont);
        invoiceNo.setAlignment(Element.ALIGN_CENTER);
        invoiceNo.setSpacingBefore(8);

        headerCell.addElement(hotelName);
        headerCell.addElement(hotelTagline);
        headerCell.addElement(invoiceNo);
        header.addCell(headerCell);
        document.add(header);

        // ─── Spacing ─────────────────────────────────────────────
        document.add(new Paragraph(" "));

        // ─── Invoice date and status ──────────────────────────────
        PdfPTable dateTable = new PdfPTable(2);
        dateTable.setWidthPercentage(100);
        dateTable.setSpacingBefore(10);

        PdfPCell dateCell = new PdfPCell();
        dateCell.setBorder(Rectangle.NO_BORDER);
        Paragraph dateLabel = new Paragraph("Invoice Date", labelFont);
        Paragraph dateValue = new Paragraph(LocalDate.now().toString(), valueFont);
        dateCell.addElement(dateLabel);
        dateCell.addElement(dateValue);

        PdfPCell statusCell = new PdfPCell();
        statusCell.setBorder(Rectangle.NO_BORDER);
        statusCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph statusLabel = new Paragraph("Status", labelFont);
        Paragraph statusValue = new Paragraph("PAID", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(39, 174, 96)));
        statusCell.addElement(statusLabel);
        statusCell.addElement(statusValue);

        dateTable.addCell(dateCell);
        dateTable.addCell(statusCell);
        document.add(dateTable);

        // ─── Divider ─────────────────────────────────────────────
        LineSeparator line = new LineSeparator();
        line.setLineColor(iceBlue);
        document.add(new Chunk(line));
        document.add(new Paragraph(" "));

        // ─── Customer and Room info ───────────────────────────────
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(5);
        infoTable.setSpacingAfter(15);

        // Customer info cell
        PdfPCell customerCell = new PdfPCell();
        customerCell.setBackgroundColor(lightGray);
        customerCell.setPadding(15);
        customerCell.setBorder(Rectangle.NO_BORDER);

        Paragraph customerTitle = new Paragraph("GUEST DETAILS", sectionFont);
        customerTitle.setSpacingAfter(8);
        customerCell.addElement(customerTitle);

        customerCell.addElement(new Paragraph("Name", labelFont));
        customerCell.addElement(new Paragraph(customer.getName(), valueFont));
        customerCell.addElement(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 4)));
        customerCell.addElement(new Paragraph("Contact", labelFont));
        customerCell.addElement(new Paragraph(customer.getContact(), valueFont));
        customerCell.addElement(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 4)));
        customerCell.addElement(new Paragraph("Customer ID", labelFont));
        customerCell.addElement(new Paragraph(String.valueOf(customer.getId()), valueFont));

        // Room info cell
        PdfPCell roomCell = new PdfPCell();
        roomCell.setBackgroundColor(lightGray);
        roomCell.setPadding(15);
        roomCell.setBorder(Rectangle.NO_BORDER);
        roomCell.setPaddingLeft(20);

        Paragraph roomTitle = new Paragraph("ROOM DETAILS", sectionFont);
        roomTitle.setSpacingAfter(8);
        roomCell.addElement(roomTitle);

        roomCell.addElement(new Paragraph("Room Number", labelFont));
        roomCell.addElement(new Paragraph(String.valueOf(room.getRoomNumber()), valueFont));
        roomCell.addElement(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 4)));
        roomCell.addElement(new Paragraph("Room Type", labelFont));
        roomCell.addElement(new Paragraph(room.getRoomType(), valueFont));
        roomCell.addElement(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 4)));
        roomCell.addElement(new Paragraph("Rate per Night", labelFont));
        roomCell.addElement(new Paragraph("Rs. " + String.format("%.2f", room.getPricePerDay()), valueFont));

        infoTable.addCell(customerCell);
        infoTable.addCell(roomCell);
        document.add(infoTable);

        // ─── Stay details table ───────────────────────────────────
        Paragraph stayTitle = new Paragraph("STAY DETAILS", sectionFont);
        stayTitle.setSpacingAfter(10);
        document.add(stayTitle);

        PdfPTable stayTable = new PdfPTable(4);
        stayTable.setWidthPercentage(100);
        stayTable.setWidths(new float[]{2f, 2f, 1.5f, 2f});

        // Table headers
        String[] headers = {"Check-In Date", "Check-Out Date", "Nights", "Rate per Night"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE)));
            cell.setBackgroundColor(iceBlue);
            cell.setPadding(10);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            stayTable.addCell(cell);
        }

        // Table values
        long nights = ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());
        String[] values = {
            booking.getCheckIn().toString(),
            booking.getCheckOut().toString(),
            String.valueOf(nights),
            "Rs. " + String.format("%.2f", room.getPricePerDay())
        };

        boolean alternate = false;
        for (String v : values) {
            PdfPCell cell = new PdfPCell(new Phrase(v, valueFont));
            cell.setBackgroundColor(alternate ? lightGray : BaseColor.WHITE);
            cell.setPadding(10);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            stayTable.addCell(cell);
            alternate = !alternate;
        }

        document.add(stayTable);
        document.add(new Paragraph(" "));

        // ─── Total amount ─────────────────────────────────────────
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(100);
        totalTable.setWidths(new float[]{3f, 1.5f});
        totalTable.setSpacingBefore(10);

        PdfPCell emptyCell = new PdfPCell(new Phrase(""));
        emptyCell.setBorder(Rectangle.NO_BORDER);
        totalTable.addCell(emptyCell);

        PdfPCell totalCell = new PdfPCell();
        totalCell.setBackgroundColor(navyBlue);
        totalCell.setPadding(15);
        totalCell.setBorder(Rectangle.NO_BORDER);

        Paragraph totalLabel = new Paragraph("TOTAL AMOUNT", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE));
        totalLabel.setAlignment(Element.ALIGN_CENTER);

        Paragraph totalAmount = new Paragraph("Rs. " + String.format("%.2f", booking.getTotalCost()), totalFont);
        totalAmount.setAlignment(Element.ALIGN_CENTER);

        totalCell.addElement(totalLabel);
        totalCell.addElement(totalAmount);
        totalTable.addCell(totalCell);

        document.add(totalTable);

        // ─── Divider ─────────────────────────────────────────────
        document.add(new Paragraph(" "));
        document.add(new Chunk(new LineSeparator()));
        document.add(new Paragraph(" "));

        // ─── Footer ───────────────────────────────────────────────
        Paragraph footer1 = new Paragraph("Thank you for staying at Alpine Chalet — Mountain Resort & SPA.", footerFont);
        footer1.setAlignment(Element.ALIGN_CENTER);

        Paragraph footer2 = new Paragraph("We hope to welcome you back soon.", footerFont);
        footer2.setAlignment(Element.ALIGN_CENTER);

        Paragraph footer3 = new Paragraph("For enquiries contact us at info@alpinechalet.com | +91 98765 43210", footerFont);
        footer3.setAlignment(Element.ALIGN_CENTER);
        footer3.setSpacingBefore(4);

        document.add(footer1);
        document.add(footer2);
        document.add(footer3);

        document.close();
        return filePath;
    }
}