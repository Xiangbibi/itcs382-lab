package com.bikeshare;

public class Rental {
    private final Bike bike;
    private final String memberId;
    private final MemberType memberType;
    private final Station originStation;

    public Rental(Bike bike, String memberId, MemberType memberType, Station originStation) {
        this.bike = bike;
        this.memberId = memberId;
        this.memberType = memberType;
        this.originStation = originStation;
    }

    public Bike getBike() {
        return bike;
    }

    public String getMemberId() {
        return memberId;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public Station getOriginStation() {
        return originStation;
    }
}