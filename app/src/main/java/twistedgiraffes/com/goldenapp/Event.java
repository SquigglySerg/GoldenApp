package twistedgiraffes.com.goldenapp;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Sergio on 3/18/2017.
 */

public class Event implements Serializable {
    //Database synced elements
    private String Title;
    private String Description;
    private String Time;
    private String Date;
    private String Location;
    private Double Lat;
    private Double Lng;
    private UUID mId;

    //Local variables that are not synced
    private boolean mToogle;

    public boolean getToogle() {
        return mToogle;
    }

    public void setToogle(boolean toogle) {
        mToogle = toogle;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLng() {
        return Lng;
    }

    public void setLng(Double lng) {
        Lng = lng;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public Event(String title, String description, String time, String date, String location, Double lat, Double lng) {
        Title = title;
        Description = description;
        Time = time;
        Date = date;
        Location = location;
        Lat = lat;
        Lng = lng;
        mId = UUID.randomUUID();
        mToogle = false;
    }

    public Event() {
        mId = UUID.randomUUID();
        mToogle = false;
        //Required by Firebase for some weird reason.
    }
}
