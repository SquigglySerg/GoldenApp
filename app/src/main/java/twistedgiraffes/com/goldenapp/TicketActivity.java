package twistedgiraffes.com.goldenapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

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
    private static final String KEY_COUPONLIST = "coupon_list";
    private static final String TAG = "GoldenApp";

    // Will need to convert these to mabye on but for now we will use four
    private CheckBox mCheckBox1;
    private CheckBox mCheckBox2;
    private CheckBox mCheckBox3;
    private CheckBox mCheckBox4;

    private GoogleApiClient mClient;
    private GoogleMap mMap;
    private CouponList mList;
    private Location mCurrentLocation;

    private boolean mChecked1 = false;
    private boolean mChecked2 = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ticket);

        mClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d(TAG, "GoogleApiClient onConnected()");
                        findLocation();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(TAG, "GoogleApiClient onConnectionSuspended()");
                    }
                }).build();

        // Get our coupon list
        mList = (CouponList) getIntent().getSerializableExtra(KEY_COUPONLIST);


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

    }

    @Override
    public void onStart() {
        super.onStart();

        mClient.connect();
    }
    @Override
    public void onStop() {
        super.onStop();

        mClient.disconnect();
    }

    private void findLocation() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        try {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mClient, request, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Log.i(TAG, "Got a fix: " + location);
                            new SearchTask().execute(location);
                        }
                    });
        } catch (SecurityException se) {
            Log.e(TAG, "Problem with FusedLocationApi", se);
        }
    }

    // This is copied from the LoctarFragment
    private class SearchTask extends AsyncTask<Location,Void,Void> {

        private Location mLocation;

        @Override
        protected Void doInBackground(Location... params) {
            mLocation = params[0];
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mCurrentLocation = mLocation;
        }
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

    /*
    private void updateUI() {
        if (mMap == null || mMapImage == null) {
            return;
        }

        LatLng itemPoint = new LatLng(mMapItem.getmLat(), mMapItem.getmLon());
        //LatLng itemPoint = new LatLng(mMapItem.getmLon(), mMapItem.getmLat());
        LatLng myPoint = new LatLng(
                mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        BitmapDescriptor itemBitmap = BitmapDescriptorFactory.fromBitmap(mMapImage);
        MarkerOptions itemMarker = new MarkerOptions()
                .position(itemPoint)
                .icon(itemBitmap);
        MarkerOptions myMarker = new MarkerOptions()
                .position(myPoint);

        mMap.clear();
        mMap.addMarker(itemMarker);
        mMap.addMarker(myMarker);

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(itemPoint)
                .include(myPoint)
                .build();

        int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        mMap.animateCamera(update);

    }
    */

}
