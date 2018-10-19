package fr.wildcodeschool.mesclubs;

public class Club {
    private String  clubName;
    private String website;
    private String  sport;
    private String address;
    private boolean  handicapped;
    private double latitude;
    private double longitude;
    private int color;
    private int image;
    private int picture;
    private int imgHandicap;
    private int counter;

    public Club(String clubName, String website, String sport, boolean handicapped, double latitude, double longitude, int color, int image, int picture, int imgHandicap, int counter) {
        this.clubName = clubName;
        this.website = website;
        this.sport = sport;
        this.handicapped = handicapped;
        this.latitude = latitude;
        this.longitude = longitude;
        this.color = color;
        this.image = image;
        this.picture = picture;
        this.imgHandicap = imgHandicap;
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Club(int color, String clubName, String sport) {
        this.color = color;
        this.clubName = clubName;
        this.sport = sport;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public int getImgHandicap() {
        return imgHandicap;
    }

    public void setImgHandicap(int imgHandicap) {
        this.imgHandicap = imgHandicap;
    }

    public int getColor() { return color; }

    public void setColor(int color) {
        this.color = color;
    }

    public Club() {}

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public boolean isHandicapped() {
        return handicapped;
    }

    public void setHandicapped(boolean handicapped) {
        this.handicapped = handicapped;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
