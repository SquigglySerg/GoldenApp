package twistedgiraffes.com.goldenapp;

import android.content.Context;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 3/22/2017.
 */

public class CouponList {
    public static final String TAG = MapsActivity.class.getSimpleName();
    private static CouponList sCouponList;

    // This is my dummy set of strings
    private String[] mString = {
            "10% off your next meal at Woody's Wood-Fired Pizza",
            "15% off your next purchase of tires from Big O Tires",
            "You won $200 in cash",
            "5% Your next one night stay at The Dove Inn Bed and Breakfast"
    };


    // These will be our dummy lognitudes and lattidutes
    private double[] longnit = {
            -105.220927,
            -105.207729,
            -105.219577,
            -105.209163
    };
    private double[] latt = {
            39.738716,
            39.755644,
            39.739179,
            39.755778
    };


    // Remove above when database is implemented
    public List<Coupon> mCoupons;

    public static CouponList get(Context context) {
        if (sCouponList == null) {
            sCouponList = new CouponList(context);
        }
        return sCouponList;
    }

    /*
    * I can't get the location in this class cause its not
    * an activity, will set it in the actual activity
    * */
    private CouponList(Context context) {

        mCoupons = new ArrayList<>();
        /*
        for (String x : mString) {
            mCoupons.add(new Coupon(x,0,0));
        }
        */
        for (int i = 0; i < mString.length; ++i) {
            mCoupons.add(new Coupon(mString[i],latt[i],longnit[i]));
        }

    }

    public List<Coupon> getCoupons() {
        return mCoupons;
    }

}
