package com.bikeshare;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class BikeShareService {
    private final Map<Station, Deque<Bike>> stationBikes = new EnumMap<>(Station.class);
    private final Map<String, Rental> activeRentals = new HashMap<>();
    private final RideRepository rideRepository;

    public BikeShareService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
        initializeStations();
    }

    private void initializeStations() {
        int bikeCounter = 1;
        for (Station station : Station.values()) {
            Deque<Bike> queue = new ArrayDeque<>();
            queue.add(new Bike("B" + (bikeCounter++)));
            queue.add(new Bike("B" + (bikeCounter++)));
            stationBikes.put(station, queue);
        }
    }

    public synchronized Bike rentBike(String memberId, MemberType memberType, Station station) {
        if (activeRentals.containsKey(memberId)) {
            throw new IllegalStateException("Member " + memberId + " already has an active rental.");
        }
        Deque<Bike> bikesAtStation = stationBikes.get(station);
        if (bikesAtStation == null || bikesAtStation.isEmpty()) {
            throw new IllegalStateException("No bikes available at " + station + ".");
        }

        Bike bike = bikesAtStation.pollFirst();
        activeRentals.put(memberId, new Rental(bike, memberId, memberType, station));
        return bike;
    }

    public synchronized int returnBike(String memberId, Station returnStation, int minutes) throws IOException {
        if (minutes < 0) {
            throw new IllegalArgumentException("Ride duration cannot be negative.");
        }
        Rental rental = activeRentals.remove(memberId);
        if (rental == null) {
            throw new IllegalStateException("No active rental found for member " + memberId + ".");
        }

        stationBikes.get(returnStation).addLast(rental.getBike());
        int fee = rental.getMemberType().calculateFee(minutes);

        rideRepository.saveRide(
                rental.getBike().getId(),
                rental.getMemberId(),
                rental.getMemberType().name(),
                minutes,
                fee,
                rental.getOriginStation().name(),
                returnStation.name()
        );

        return fee;
    }

    public synchronized Rental getActiveRental(String memberId) {
        return activeRentals.get(memberId);
    }
}