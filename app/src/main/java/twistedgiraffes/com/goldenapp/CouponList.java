package twistedgiraffes.com.goldenapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 3/22/2017.
 */

public class CouponList {
    private static CouponList sCouponList;

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

    private CouponList(Context context) {
        mCoupons = new ArrayList<>();

    }

    public List<Coupon> getCoupons() {
        return mCoupons;
    }

}
