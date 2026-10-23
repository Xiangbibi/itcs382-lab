package com.bikeshare;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BikeShareApp {

    // Member types encoded as raw integers
    public static final int TYPE_STUDENT = 1;
    public static final int TYPE_STAFF = 2;

    // Station bike inventory stored directly in public static fields
    public static final Map<String, List<String>> STATIONS = new HashMap<>();

    // Active rentals tracking: bikeId -> int[]{memberType, memberIdAsInt}
    public static final Map<String, int[]> ACTIVE_RENTALS = new HashMap<>();

    public static void main(String[] args) {
        // Initialize inventory: 2 bikes per station
        STATIONS.put("Library", new ArrayList<>(List.of("BIKE-LIB-1", "BIKE-LIB-2")));
        STATIONS.put("Canteen", new ArrayList<>(List.of("BIKE-CAN-1", "BIKE-CAN-2")));
        STATIONS.put("Dorm", new ArrayList<>(List.of("BIKE-DOR-1", "BIKE-DOR-2")));

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                System.out.println("\n--- BikeShare Menu ---");
                System.out.println("1. Rent a bike");
                System.out.println("2. Return a bike");
                System.out.println("Type 'quit' to exit.");
                System.out.print("Choose option: ");

                String input = scanner.nextLine().trim();
                if ("quit".equalsIgnoreCase(input)) {
                    System.out.println("Exiting BikeShare. Goodbye!");
                    break;
                }

                if ("1".equals(input)) {
                    // Boolean flag true = rent operation
                    handleRideTransaction(scanner, true);
                } else if ("2".equals(input)) {
                    // Boolean flag false = return operation
                    handleRideTransaction(scanner, false);
                } else {
                    System.out.println("Invalid command. Please try again.");
                }
            }
        }
    }

    /**
     * Single monolithic method handling both rent and return workflows,
     * fee calculation, receipt printing, and file writing based on isRent.
     */
    public static void handleRideTransaction(Scanner scanner, boolean isRent) {
        if (isRent) {
            System.out.print("Enter member ID: ");
            String memberIdStr = scanner.nextLine().trim();
            int memberId;
            try {
                memberId = Integer.parseInt(memberIdStr);
            } catch (NumberFormatException e) {
                System.out.println("Error: Member ID must be numeric.");
                return;
            }

            System.out.print("Enter member type (1 for Student, 2 for Staff): ");
            int memberType;
            try {
                memberType = Integer.parseInt(scanner.nextLine().trim());
                if (memberType != TYPE_STUDENT && memberType != TYPE_STAFF) {
                    System.out.println("Error: Invalid type code. Use 1 or 2.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Member type must be a valid integer.");
                return;
            }

            System.out.print("Enter pickup station (Library, Canteen, Dorm): ");
            String station = scanner.nextLine().trim();
            List<String> bikes = STATIONS.get(station);
            if (bikes == null) {
                System.out.println("Error: Unknown station.");
                return;
            }
            if (bikes.isEmpty()) {
                System.out.println("Error: No bikes available at " + station + ".");
                return;
            }

            String rentedBike = bikes.remove(0);
            ACTIVE_RENTALS.put(rentedBike, new int[]{memberType, memberId});
            System.out.println("Success! Bike " + rentedBike + " rented to Member " + memberId + ".");

        } else {
            System.out.print("Enter bike ID being returned: ");
            String bikeId = scanner.nextLine().trim();

            if (!ACTIVE_RENTALS.containsKey(bikeId)) {
                System.out.println("Error: Bike is not currently rented out.");
                return;
            }

            System.out.print("Enter return station (Library, Canteen, Dorm): ");
            String returnStation = scanner.nextLine().trim();
            List<String> bikes = STATIONS.get(returnStation);
            if (bikes == null) {
                System.out.println("Error: Unknown station.");
                return;
            }

            System.out.print("Enter ride duration in minutes: ");
            int minutes;
            try {
                minutes = Integer.parseInt(scanner.nextLine().trim());
                if (minutes < 0) {
                    System.out.println("Error: Duration cannot be negative.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ride minutes must be a valid integer.");
                return;
            }

            int[] rentalData = ACTIVE_RENTALS.remove(bikeId);
            int memberType = rentalData[0];
            int memberId = rentalData[1];

            // Inlined fee calculation based on int code: 2 baht/min student, 3 baht/min staff
            int rate = (memberType == TYPE_STUDENT) ? 2 : 3;
            int totalFee = minutes * rate;

            bikes.add(bikeId);

            // Print receipt
            System.out.println("\n========== RECEIPT ==========");
            System.out.println("Bike ID   : " + bikeId);
            System.out.println("Member ID : " + memberId);
            System.out.println("Minutes   : " + minutes);
            System.out.println("Total Fee : " + totalFee + " baht");
            System.out.println("=============================");

            // Append record to rides.csv
            saveRideToFile(bikeId, memberId, memberType, minutes, totalFee, returnStation);
        }
    }

    public static void saveRideToFile(String bikeId, int memberId, int memberType, int minutes, int fee, String station) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rides.csv", StandardCharsets.UTF_8, true))) {
            writer.write(bikeId + "," + memberId + "," + memberType + "," + minutes + "," + fee + "," + station);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write ride to file: " + e.getMessage());
        }
    }
}