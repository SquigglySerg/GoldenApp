package twistedgiraffes.com.goldenapp;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by rybailey on 2/27/17.
 */
public class News {

    private static final AtomicLong counter = new AtomicLong();


    private String mHeadline;
    private String mFullStory;
    private UUID mId;
    private long mSessionId;
    private Date mDate;
    private boolean mToogle;

    public News(){
        mId = UUID.randomUUID();
        mDate = new Date();
        mSessionId = counter.getAndIncrement();
        mToogle = false;
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

    public long getSessionId() {
        return mSessionId;
    }

    public boolean getToogle() {
        return mToogle;
    }
    public void setToogle(boolean toogle) {
        mToogle = toogle;
    }
}
