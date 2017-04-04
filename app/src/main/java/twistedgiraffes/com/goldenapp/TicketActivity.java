package twistedgiraffes.com.goldenapp;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

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

    private CheckBox mCheckBox1;
    private GoogleApiClient mClient;

    private boolean mChecked = false;
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

        // This is our general location
        Location mlocation = LocationServices.FusedLocationApi.getLastLocation(mClient);


        mCheckBox1 = (CheckBox) findViewById( R.id.ticket_checkbox );
        if (mCheckBox1.isChecked()) {
            mChecked = true;
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
}
