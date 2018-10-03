package fr.wildcodeschool.mesclubs;

import android.support.v7.app.AppCompatActivity;

public class ClubModel extends AppCompatActivity {

    private String  clubName;
    private String  assMail;
    private String  sport;
    private String  handicapped;
    private int     color;

    public ClubModel(String clubName,String assMail, String sport, String handicapped) {
        this.clubName = clubName;
        this.assMail = assMail;
        this.sport = sport;
        this.handicapped = handicapped;
    }

    public ClubModel(int color,String clubName, String sport) {
        this.color = color;
        this.clubName = clubName;
        this.sport = sport;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getAssMail() {
        return assMail;
    }

    public void setAssMail(String assMail) {
        this.assMail = assMail;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getHandicapped() {
        return handicapped;
    }

    public void setHandicapped(String handicapped) {
        this.handicapped = handicapped;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
