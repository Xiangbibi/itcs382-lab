package com.bikeshare;

import java.io.IOException;

public interface RideRepository {
    void saveRide(String bikeId, String memberId, String memberType, int minutes, int fee, String startStation, String endStation) throws IOException;
}