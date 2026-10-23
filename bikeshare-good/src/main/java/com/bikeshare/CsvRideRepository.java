package com.bikeshare;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class CsvRideRepository implements RideRepository {
    private final Path filePath;

    public CsvRideRepository(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public synchronized void saveRide(String bikeId, String memberId, String memberType, int minutes, int fee, String startStation, String endStation) throws IOException {
        boolean fileExists = Files.exists(filePath);
        try (FileWriter fw = new FileWriter(filePath.toFile(), true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
            
            if (!fileExists) {
                pw.println("bikeId,memberId,memberType,minutes,fee,startStation,endStation");
            }
            pw.printf("%s,%s,%s,%d,%d,%s,%s%n", bikeId, memberId, memberType, minutes, fee, startStation, endStation);
        }
    }
}