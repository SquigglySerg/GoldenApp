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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

public class NewsFeed extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewsFeedFragment.Callbacks {
    private static final String FIREBASE_URL = "https://banded-charmer-160001.firebaseio.com/";
    private static final String FIREBASE_CHILD_NAME = "Events";
    private HashMap<String, Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        events = new HashMap<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeFireBase();

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
            addEventToDB();
            Log.d("***Num Events:  ", Integer.toString(events.size()) );
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

    private void initializeFireBase(){
        Firebase.setAndroidContext(this);
        Firebase firebaseRef = new Firebase("https://banded-charmer-160001.firebaseio.com/");

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Event e = child.getValue(Event.class);

                        Log.d("***Event:  ", e.getTitle() + " added.");
                        events.put(child.getKey().toString(), e);
                    }
                }
                catch (com.firebase.client.FirebaseException e){
                    Log.d("****ERROR****", e.getMessage());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void addEventToDB(){
        Firebase.setAndroidContext(this);
        Firebase firebaseRef = new Firebase(FIREBASE_URL);

        Event e = new Event("Test1 Title", "Test Event", "3:00 pm", "23/3/17", "CSM");
        Firebase eventRef = firebaseRef.child(e.getTitle());
        eventRef.setValue(e);


        Event e2 = new Event("Test2 Title", "Test Event", "9:00 pm", "1/3/17", "Clear Creek");
        eventRef = firebaseRef.child(e2.getTitle());
        eventRef.setValue(e2);
    }
}
