package fr.wildcodeschool.mesclubs;

public class Club {
    private String clubName;
    private String website;
    private String sport;
    private String address;
    private boolean handicapped;
    private double latitude;
    private double longitude;
    private int color;
    private int image;
    private int picture;
    private int imgHandicap;
    private int counter;
    private String id;
    private double distance;

    public Club() {
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

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
