package twistedgiraffes.com.goldenapp;

/**
 * Created by Sergio on 3/18/2017.
 */

public class Event {
    private String Title;
    private String Description;
    private String Time;
    private String Date;
    private String Location;

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

    public Event(String title, String description, String time, String date, String location) {

        Title = title;
        Description = description;
        Time = time;
        Date = date;
        Location = location;
    }

    public Event() {

    }
}
