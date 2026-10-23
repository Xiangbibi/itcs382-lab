package com.bikeshare;

import java.io.IOException;
import java.util.Scanner;

public class CliMenu {
    private final BikeShareService service;
    private final Scanner scanner;

    public CliMenu(BikeShareService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void run() {
        while (true) {
            System.out.println("\n=== BikeShare Campus ===");
            System.out.println("1. Rent a bike");
            System.out.println("2. Return a bike");
            System.out.println("Type 'quit' to exit");
            System.out.print("Select an option: ");

            String input = scanner.nextLine().trim();
            if ("quit".equalsIgnoreCase(input)) {
                System.out.println("Goodbye!");
                break;
            }

            switch (input) {
                case "1" -> handleRent();
                case "2" -> handleReturn();
                default -> System.out.println("Invalid selection. Try again.");
            }
        }
    }

    private void handleRent() {
        try {
            System.out.print("Enter Member ID: ");
            String memberId = scanner.nextLine().trim();

            System.out.print("Enter Member Type (STUDENT / STAFF): ");
            MemberType memberType = MemberType.valueOf(scanner.nextLine().trim().toUpperCase());

            System.out.print("Enter Station (LIBRARY / CANTEEN / DORM): ");
            Station station = Station.valueOf(scanner.nextLine().trim().toUpperCase());

            Bike bike = service.rentBike(memberId, memberType, station);
            System.out.printf("Successfully rented Bike [%s] at %s!%n", bike.getId(), station);
        } catch (IllegalArgumentException e) {
            System.out.println("Input error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Rental failed: " + e.getMessage());
        }
    }

    private void handleReturn() {
        try {
            System.out.print("Enter Member ID: ");
            String memberId = scanner.nextLine().trim();

            Rental rental = service.getActiveRental(memberId);
            if (rental == null) {
                System.out.println("No active rental found for this member ID.");
                return;
            }

            System.out.print("Enter Return Station (LIBRARY / CANTEEN / DORM): ");
            Station returnStation = Station.valueOf(scanner.nextLine().trim().toUpperCase());

            System.out.print("Enter Ride Time (minutes): ");
            int minutes = Integer.parseInt(scanner.nextLine().trim());

            String bikeId = rental.getBike().getId();
            int fee = service.returnBike(memberId, returnStation, minutes);

            printReceipt(bikeId, minutes, fee);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for minutes.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Return failed: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Warning: Bike returned, but could not write to rides.csv: " + e.getMessage());
        }
    }

    private void printReceipt(String bikeId, int minutes, int fee) {
        System.out.println("\n----------- RECEIPT -----------");
        System.out.printf(" Bike:    %s%n", bikeId);
        System.out.printf(" Minutes: %d%n", minutes);
        System.out.printf(" Fee:     %d baht%n", fee);
        System.out.println("-------------------------------");
    }
}