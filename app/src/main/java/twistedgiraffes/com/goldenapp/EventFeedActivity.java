package twistedgiraffes.com.goldenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class EventFeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EventFeedFragment.Callbacks {

    DataBase mDataBase;

    // Mvillafu: I'm adding a list of coupons inside the main function instead of a database
    private CouponList mCouponList;
    private static final String KEY_COUPONLIST = "coupon_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DataBase.get(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.event_feed);

        if (fragment == null){
            fragment = new EventFeedFragment();
            fm.beginTransaction().add(R.id.event_feed,fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calendar) {
            // Handle the calender action
            //Log.d("***Num Events:  ", Integer.toString(mDataBase.size()) ); //Using this to get info on the db
            // +++++++++++++++Log.d("***Num Events:  ", Integer.toString(mDataBase.size()) ); //Using this to get info on the db
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_golden_ticket) {
            // Mvillafu: I'm adding a list of coupons inside the main function instead of a database
            if (mCouponList == null) { // for the start of the app
                mCouponList = CouponList.get(this);
            }
            Intent intent = new Intent(this, TicketActivity.class);
            //intent.putExtra(KEY_COUPONLIST, mCouponList);
            startActivity(intent);
        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onEventSelect(Event event) {
        Intent intent = EventActivity.newIntent(this, event.getId());
        startActivity(intent);
    }

}
