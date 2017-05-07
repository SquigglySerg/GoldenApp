package twistedgiraffes.com.goldenapp;

import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final float DEFAULT_ZOOM = 13;
    private static final LatLng NORTH_EASTERN_COLORADO_CORNER = new LatLng(41.0031, -102.0616);
    private static final LatLng SOUTH_WESTERN_COLORADO_CORNER = new LatLng(37.0100, -109.0425);
    private static final LatLng GOLDEN = new LatLng(39.7554, -105.2213);

    private GoogleMap mMap;
    private DataBase mDataBase = DataBase.get(getBaseContext());
    private SupportMapFragment mapFragment;

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

        // Set up map
        // Add a marker in Golden, CO, USA and move the camera to Golden
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(GOLDEN, DEFAULT_ZOOM);
        mMap.animateCamera(update);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String eMsg = marker.getTitle() + "\n" + marker.getSnippet();
                Snackbar.make( mapFragment.getView(), eMsg, eMsg.length()*100)
                        .setAction("Action", null)
                        .show();

                return true;
            }
        });

        /*mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                LatLngBounds viewBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                if(viewBounds.southwest.latitude < SOUTH_WESTERN_COLORADO_CORNER.latitude
                        && viewBounds.southwest.longitude < SOUTH_WESTERN_COLORADO_CORNER.longitude
                        && viewBounds.northeast.latitude > NORTH_EASTERN_COLORADO_CORNER.latitude
                        && viewBounds.northeast.longitude > NORTH_EASTERN_COLORADO_CORNER.longitude){

                    Log.d("****OUT OF BOUNDS","df");
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(GOLDEN, DEFAULT_ZOOM);
                    mMap.animateCamera(update);
                }
            }
        });*/

        for(Event e : mDataBase.getEventList()){
            LatLng eLocal = new LatLng(e.getLat(), e.getLng());
            String eTitle = e.getTitle();
            String eDescription = e.getDescription();

            mMap.addMarker(new MarkerOptions()
                    .position(eLocal)
                    .title(eTitle)
                    .snippet(eDescription)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_event)) );
        }
    }
}
