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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;

public class NewsFeed extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewsFeedFragment.Callbacks {
    private HashMap<String, Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        events = new HashMap<>();

        Firebase.setAndroidContext( this );
        Firebase firebaseReferance = new Firebase("https://banded-charmer-160001.firebaseio.com/");
        Firebase eventsRef = firebaseReferance.child("Events");

        //Initial Query of the DataBase
        //Query eventsQuery = eventsRef.orderByChild("date");
        //eventsQuery.addChildEventListener(new ChildEventListener()

        eventsRef.addChildEventListener( new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Event newEvent = dataSnapshot.getValue(Event.class);
                events.put(s, newEvent);
                if(newEvent != null) {
                    Log.d("***Added Event ", newEvent.getTitle());
                }

                //update whatever
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // Update Event
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Remove Event(s)
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            fragment = new NewsFeedFragment();
            fm.beginTransaction().add(R.id.news_feed,fragment).commit();
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

            Log.d("*****Num Events", Integer.toString(events.size()));
            /*for (Map.Entry<String, Event> entry : events.entrySet()) {
                String key = entry.getKey();
                Event event = entry.getValue();

                Log.d("**** Event: ", key + " = " + event.getTitle());
            }*/

        } else if (id == R.id.nav_golden_ticket) {
            Intent intent = new Intent(this, TicketActivity.class);
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
    public void onNewsSelect(News news) {
        Intent intent = NewsActivity.newIntent(this, news.getId());
        startActivity(intent);
    }
}
