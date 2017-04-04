package twistedgiraffes.com.goldenapp;

/**
 * Created by michael on 3/20/2017.
 */

public class Coupon {
    private String mCoupon; // What the discount is
    private double mLat;
    private double mLog;

    public Coupon(String c, double lat, double log) {
        setmCoupon(c);
        setmLat(lat);
        setmLog(log);
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
}
