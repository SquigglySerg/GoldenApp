package twistedgiraffes.com.goldenapp;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rybailey on 2/27/17.
 */
public class News {
    private String mHeadline;
    private String mFullStory;
    private UUID mId;
    private Date mDate;

    public News(){
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Date getDate() {
        return mDate;
    }

    public String getFullStory() {
        return mFullStory;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public UUID getId() {
        return mId;
    }

    public void setFullStory(String fullStory) {
        this.mFullStory = fullStory;
    }

    public void setHeadline(String headline) {
        mHeadline = headline;
    }
}
