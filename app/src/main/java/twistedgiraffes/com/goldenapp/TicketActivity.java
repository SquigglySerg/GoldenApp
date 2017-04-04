package twistedgiraffes.com.goldenapp;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by michael on 3/3/2017.
 */

/*
* I will make this a fragment later
*
*
* Will have a set number of tickets that the user can get
* They must be in a specific location to get each ticket
*
* Need a class that will contain the string value of that ticket and its location
* Need fragment similar to the fragment in crim init
* */

/*
*
* I'm just hard coding in the different check boxes for
* time constraits
*
* */
public class TicketActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String KEY_CLICKED = "clicked";
    private static final String TAG = "GoldenApp";

    // Will need to convert these to mabye on but for now we will use four
    private CheckBox mCheckBox1;
    private CheckBox mCheckBox2;
    private CheckBox mCheckBox3;
    private CheckBox mCheckBox4;

    private GoogleApiClient mClient;
    private CouponList mList;
    private Location mLocation;

    private boolean mChecked1 = false;
    private boolean mChecked2 = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ticket);

        mClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mClient.connect();

        // Get the list of coupouns.
        mList = CouponList.get(this);

        // This is our general location
        try {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
        } catch (SecurityException se) {

        }

        // Will need to be removed when we get the database for them
        // Fill in the list of counpons with their new locations.
        for (Coupon x : mList.mCoupons) {
            if (mLocation != null) {
                x.setmLat(mLocation.getLatitude());
                x.setmLog(mLocation.getLongitude());
            }
            else {
                x.setmLat(0);
                x.setmLog(0);
            }
        }
        // Delete the above when done

        // Define the four different checkboxes
        mCheckBox1 = (CheckBox) findViewById( R.id.ticket_checkbox1 );
        mCheckBox2 = (CheckBox) findViewById( R.id.ticket_checkbox2 );
        mCheckBox3 = (CheckBox) findViewById( R.id.ticket_checkbox3 );
        mCheckBox4 = (CheckBox) findViewById( R.id.ticket_checkbox4 );

        /*
        *
        * NOTE: In the final version we will be adding a delta for how
        *       far they will be allowed to get the ticket
        *
        * */
        mCheckBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "The lattitude: " + mList.mCoupons.get(0).getmLat());
                if (mCheckBox1.isChecked() && !mChecked1)/* && mLocation != null
                        && mList.mCoupons.get(0).getmLat() == mLocation.getLatitude()
                        && mList.mCoupons.get(0).getmLog() == mLocation.getLongitude())*/ {
                    Toast.makeText(TicketActivity.this, mList.mCoupons.get(0).getmCoupon(), Toast.LENGTH_LONG).show();
                    mChecked1 = true;
                    //Toast.makeText(getParentActivityIntent(), mList.mCoupons.get(0).getmCoupon(), Toast.LENGTH_SHORT ).show();
                }
                else if (mChecked1) {
                    Toast.makeText(TicketActivity.this, "You have already claimed this ticket", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(TicketActivity.this, "There are no events near you.", Toast.LENGTH_LONG).show();
                }
                mCheckBox1.setChecked(mChecked1);
            }
        });
        mCheckBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCheckBox2.setChecked(false);
                Toast.makeText(TicketActivity.this, "There are no events near you.\n"
                        + " NOTE: This one is designed to fail for the purposes\n"
                        + " of showing you how it would work in the final version.", Toast.LENGTH_LONG).show();

            }
        });
        mCheckBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mCheckBox3.isChecked())/* && mLocation != null
                        && mList.mCoupons.get(2).getmLat() == mLocation.getLatitude()
                        && mList.mCoupons.get(2).getmLog() == mLocation.getLongitude())*/ {
                    Toast.makeText(TicketActivity.this, mList.mCoupons.get(2).getmCoupon(), Toast.LENGTH_LONG).show();
                    mChecked2 = true;
                    //Toast.makeText(getParentActivityIntent(), mList.mCoupons.get(0).getmCoupon(), Toast.LENGTH_SHORT ).show();
                }
                else if (mChecked2) {
                    Toast.makeText(TicketActivity.this, "You have already claimed this ticket", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(TicketActivity.this, "There are no events near you.", Toast.LENGTH_LONG).show();
                }
                mCheckBox3.setChecked(mChecked2);
            }
        });
        mCheckBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCheckBox4.setChecked(false);
                Toast.makeText(TicketActivity.this, "There are no events near you.\n"
                        + " NOTE: This one is designed to fail for the purposes\n"
                        + " of showing you how it would work in the final version.", Toast.LENGTH_LONG).show();
            }
        });


    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
