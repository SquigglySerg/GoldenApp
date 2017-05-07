package twistedgiraffes.com.goldenapp;

import java.util.UUID;

/**
 * Created by michael on 3/20/2017.
 */

public class Coupon {
    private UUID mId;
    private String mCoupon; // What the discount is
    private double mLat;
    private double mLog;
    private boolean mClicked;

    public Coupon(String c, double lat, double log) {
        mId = UUID.randomUUID();
        setmCoupon(c);
        setmLat(lat);
        setmLog(log);
        mClicked = false;
    }

    public UUID getId() {
        return mId;
    }
    public String getmCoupon() {
        return mCoupon;
    }

    public void setmCoupon(String mCoupon) {
        this.mCoupon = mCoupon;
    }

    public double getmLat() {
        return mLat;
    }

    public void setmLat(double mLat) {
        this.mLat = mLat;
    }

    public double getmLog() {
        return mLog;
    }

    public void setmLog(double mLog) {
        this.mLog = mLog;
    }

    public void setClicked(boolean clicked) {mClicked = clicked;}
    public boolean getClicked() {return mClicked;}
}
