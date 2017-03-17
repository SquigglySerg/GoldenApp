package twistedgiraffes.com.goldenapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Sergio on 3/17/2017.
 */

public class Event {

    private String title;
    private String description;
    private String date;    //Maybe change to java.util.calendar instead of String
    private String location;
    private LatLng coordinates;

    public Event(String title, String description, String date, String location, LatLng coordinates) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.coordinates = coordinates;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }
}
