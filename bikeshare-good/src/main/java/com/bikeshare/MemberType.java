package com.bikeshare;

public enum MemberType {
    STUDENT(2),
    STAFF(3);

    private final int ratePerMinute;

    MemberType(int ratePerMinute) {
        this.ratePerMinute = ratePerMinute;
    }

    public int calculateFee(int minutes) {
        return this.ratePerMinute * minutes;
    }
}