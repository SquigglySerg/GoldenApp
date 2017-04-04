package twistedgiraffes.com.goldenapp;

import android.content.Context;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 3/22/2017.
 */

public class CouponList {
    public static final String TAG = MapsActivity.class.getSimpleName();
    private static CouponList sCouponList;
    private GoogleApiClient mClient;

    // This is my dummy set of strings
    private String[] mString = {
            "10% off your next meal at Woody's Wood-Fired Pizza",
            "15% off your next purchase of tires from Big O Tires",
            "You won $200 in cash",
            "5% Your next one night stay at The Dove Inn Bed and Breakfast"
    };

    // Remove above when database is implemented
    private List<Coupon> mCoupons;

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
        for (String x : mString) {
            mCoupons.add(new Coupon(x,0,0));
        }

    }

    public List<Coupon> getCoupons() {
        return mCoupons;
    }

}
