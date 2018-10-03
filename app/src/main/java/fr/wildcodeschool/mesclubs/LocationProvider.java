package fr.wildcodeschool.mesclubs;

import java.util.ArrayList;
import java.util.Date;

public class LocationProvider {
    double longitude;
    double latitude;

    public LocationProvider (double longitude, double latitude){
        this.longitude=longitude;
        this.latitude=latitude;

    }


    public double getLongitude() {

        return this.longitude;
    }

    public double getLatitude() {

        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
