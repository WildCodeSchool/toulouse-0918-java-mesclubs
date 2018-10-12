package fr.wildcodeschool.mesclubs;

public class MarkerInfos {

    private String title;
    private String description;
    private int image;
    private boolean handicap;
    private int picture;
    private int imgHandicap;
    private  String sport;
    private  String web;

    public MarkerInfos(String title, String description, int image, boolean handicap, int picture, int imgHandicap, String sport, String web) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.handicap = handicap;
        this.picture = picture;
        this.imgHandicap = imgHandicap;
        this.sport = sport;
        this.web = web;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {return image;}

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isHandicap() {
        return handicap;
    }

    public void setHandicap(boolean handicap) {
        this.handicap = handicap;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public int getImgHandicap() {return imgHandicap;}

    public void setImgHandicap(int imgHandicap) {this.imgHandicap = imgHandicap;}

    public String getSport() {return sport;}

    public void setSport(String sport) {this.sport = sport;}

    public String getWeb() {return web;}

    public void setWeb(String web) {this.web = web;}


}