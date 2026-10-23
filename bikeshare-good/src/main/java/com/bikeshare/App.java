package com.bikeshare;

import java.nio.file.Path;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        RideRepository rideRepository = new CsvRideRepository(Path.of("rides.csv"));
        BikeShareService service = new BikeShareService(rideRepository);
        Scanner scanner = new Scanner(System.in);

        CliMenu menu = new CliMenu(service, scanner);
        menu.run();
    }
}