package com.company;

public class Formula1Driver extends Driver {

    private String team;
    public int firstPosCount;
    private int secondPosCount;
    private int thirdPosCount;
    private int totalPoints;
    private int racesCount;


    public Formula1Driver(String fName, String lName, String country, String team, int totalPoints, int firstPosCount, int secondPosCount, int thirdPosCount, int racesCount) {
        super(fName, lName, country);
        this.team = team;
        this.totalPoints = totalPoints;
        this.firstPosCount = firstPosCount;
        this.secondPosCount = secondPosCount;
        this.thirdPosCount = thirdPosCount;
        this.racesCount = racesCount;
    }

    public String getTeam() {
        return team;
    }

    public int getFirstPosCount() {
        return firstPosCount;
    }

    public int getSecondPosCount() {
        return secondPosCount;
    }

    public int getThirdPosCount() {
        return thirdPosCount;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getRacesCount() {
        return racesCount;
    }

//    public void setTeam(String team) {
//        this.team = team;
//    }

    public void setFirstPosCount(int firstPosCount) {
        this.firstPosCount += firstPosCount;
    }

    public void setSecondPosCount(int secondPosCount) {
        this.secondPosCount += secondPosCount;
    }

    public void setThirdPosCount(int thirdPosCount) {
        this.thirdPosCount += thirdPosCount;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints += totalPoints;
    }

    public void setRacesCount(int racesCount) {
        this.racesCount += racesCount;
    }
}
