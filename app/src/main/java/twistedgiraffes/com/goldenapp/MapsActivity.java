package twistedgiraffes.com.goldenapp;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.UUID;

/**
 * The Activity showing the map with the events as markers
 *
 * Connects to Google Maps and displays the map. Then uses the events in the database and displays
 * them as markers of the map.
 *
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final float MIN_ZOOM = 7;
    private static final float DEFAULT_ZOOM = 13;
    private static final float EVENT_ZOOM = 15;
    private static final LatLng NORTH_EASTERN_COLORADO_CORNER = new LatLng(41.0031, -102.0616);
    private static final LatLng SOUTH_WESTERN_COLORADO_CORNER = new LatLng(37.0100, -109.0425);
    private static final LatLng GOLDEN = new LatLng(39.7554, -105.2213);

    private GoogleMap mMap;
    private DataBase mDataBase = DataBase.get(getBaseContext());
    private SupportMapFragment mapFragment;

    /**
     * Sets the view for the map and obtains the SupportMapFragment and get notified when the map
     * is ready to be used.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     *
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /* ************Set up map********** */
        // Limited the area the user can traverse and see
        mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(SOUTH_WESTERN_COLORADO_CORNER,NORTH_EASTERN_COLORADO_CORNER));
        mMap.setMinZoomPreference(MIN_ZOOM);

        // Move the camera to Golden
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(GOLDEN, DEFAULT_ZOOM));

        // Set up the marker click listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                //Display a snack bar with the respective event's description
                final Event e = mDataBase.getEvent(UUID.fromString(marker.getSnippet()));

                String eMsg = e.getTitle() + "\n" + e.getTime() + " - " + e.getEndTime()
                        + " on " + e.getDate() + " at " + e.getLocation();
                Snackbar.make( mapFragment.getView(), eMsg, eMsg.length()*100)
                        .setAction("Go Here", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri gmmIntentUri = Uri.parse("google.navigation:q="+
                                        Double.toString(marker.getPosition().latitude)
                                        + "," + Double.toString(marker.getPosition().longitude) );
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }
                        })
                        .show();

                //Move camera to event
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), EVENT_ZOOM));

                String oldSnippet = marker.getSnippet();
                marker.setSnippet(eMsg);
                marker.showInfoWindow();
                marker.setSnippet(oldSnippet);

                return true;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                final Event e = mDataBase.getEvent(UUID.fromString(marker.getSnippet()));
                Intent intent = EventDetailActivity.newIntent(getBaseContext(), e.getId());
                startActivity(intent);
            }
        });

        // Add events from database as markers
        for(Event e : mDataBase.getEventList()){
            LatLng eLocal = new LatLng(e.getLat(), e.getLng());
            String eTitle = e.getTitle();
            String eId = e.getId().toString();

            mMap.addMarker(new MarkerOptions()
                    .position(eLocal)
                    .title(eTitle)
                    .snippet(eId)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_event)) );
        }
    }
}
