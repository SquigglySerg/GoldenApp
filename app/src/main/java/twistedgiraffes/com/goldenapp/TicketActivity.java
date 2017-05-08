package twistedgiraffes.com.goldenapp;

import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

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
        GoogleApiClient.OnConnectionFailedListener  {
    private static final String KEY_COUPONLIST = "coupon_list";
    private static final String TAG = "GoldenApp";

    private GoogleApiClient mClient;
    private CouponList mList;
    private Location mCurrentLocation;

    private RecyclerView mCouponRecyclerView;
    private TicketAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ticket_list);

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
        mList = CouponList.get(this);
        mCouponRecyclerView = (RecyclerView) findViewById(R.id.ticket_recycle_view);
        mCouponRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        CouponList couponList = CouponList.get(getApplicationContext());
        List<Coupon> crimes = couponList.getCoupons();

        Log.d(TAG, "The size of the list: " + crimes.size());
        if (mAdapter == null) {
            Log.d(TAG, "Its in the if statement");
            mAdapter = new TicketAdapter(crimes);
            mCouponRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCoupons(crimes);
            mAdapter.notifyDataSetChanged();
        }
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

    @Override
    public void onResume() {
        super.onResume();
        //updateUI();
    }

    // Find out current location, it will take some time each time
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

    // This is our holder for the tickets
    private class TicketHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private Coupon mCoupon;

        public TicketHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.ticket_prize);
        }

        public void bindTicket(Coupon coupon) {
            mCoupon = coupon;
            if (mCoupon.getClicked()) {
                mTitleTextView.setText(mCoupon.getmCoupon());
                mTitleTextView.setBackgroundColor(Color.WHITE);
                //mTitleTextView.setBackgroundResource(R.drawable.golden_scratch);
                //RelativeLayout relative = (RelativeLayout) findViewById(R.id.ticket);
                //relative.setBackgroundResource(R.drawable.golden_scratch);
            } else {
                mTitleTextView.setText(""); // This will be blank and will only be displayed after they click
            }

        }

        private double distance(double cLat, double cLog, double tLat, double tLog) {
            return Math.sqrt(Math.pow(cLat - tLat, 2) + Math.pow(cLog - tLog, 2));
        }
        // This is where our checking locations will work
        @Override
        public void onClick(View v) {
            findLocation(); // this will need to be called each time so that way it is updated every time
            if (!mCoupon.getClicked()
                    && mCurrentLocation != null
                    && distance(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(),
                    mCoupon.getmLat(),mCoupon.getmLog()) <= 0.002) {
                mTitleTextView.setText(mCoupon.getmCoupon());
                // We have to go in and manually set the values to true in a loop
                for (Coupon x : mList.getCoupons()) {
                    if (x.getId() == mCoupon.getId()) {
                        x.setClicked(true);
                    }
                }

                //v.setBackgroundColor(Color.WHITE);
                v.setBackgroundResource(R.drawable.golden_scratch);

                // We set the background to be blank so they know they can't click it
                //RelativeLayout relative = (RelativeLayout) findViewById(R.id.ticket);
                //relative.setBackgroundResource(R.drawable.golden_scratch);

            } else if (mCoupon.getClicked()) {
                v.setBackgroundColor(Color.WHITE);
                Toast.makeText(getApplicationContext(), "You already received this ticket.", Toast.LENGTH_SHORT).show();
            } else if (mCurrentLocation == null) {
                Toast.makeText(getApplicationContext(), "Please wait a moment we are getting your location.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Sorry there are no events near by.\nTry looking around.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private class TicketAdapter extends RecyclerView.Adapter<TicketHolder> {
        private List<Coupon> mCoupons;

        public TicketAdapter(List<Coupon> coupons) {
            Log.i(TAG, "Its in teh adapter class");
            mCoupons = coupons;
            Log.d(TAG, "The size of the list: " + mCoupons.size());
        }

        @Override
        public TicketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.fragment_ticket, parent, false);
            return new TicketHolder(view);
        }

        @Override
        public void onBindViewHolder(TicketHolder holder, int position) {
            Coupon coupon = mCoupons.get(position);
            holder.bindTicket(coupon);
        }

        @Override
        public int getItemCount() {
            return mCoupons.size();
        }
        public void setCoupons(List<Coupon> coupons) {
            mCoupons = coupons;
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
