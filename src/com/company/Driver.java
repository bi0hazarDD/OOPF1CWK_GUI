package com.company;

public abstract class Driver {
    private String fName;
    private String lName;
    private String country;

    public String getName() {
        return fName + " " + lName;
    }

    public Driver(String fName, String lName, String country) {
        this.fName = fName;
        this.lName = lName;
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public String getFName() {
        return fName;
    }

    public String getLName() {
        return lName;
    }
}
