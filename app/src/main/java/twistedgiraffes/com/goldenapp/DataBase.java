package twistedgiraffes.com.goldenapp;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by rybailey on 4/3/17.
 */

/**
 * The class used to hold a list of events which is retrieved from the FireBase database.
 *
 * The class used to hold a list of events which is retrieved from the FireBase database. Also
 * allows the removal, addition, or retrieval of those events.
 */
public class DataBase {
    private static DataBase sDataBase;
    private static final String FIREBASE_URL = "https://banded-charmer-160001.firebaseio.com/";

    private Context mContext;
    public List<Event> mLocalList;
    private List<DataBaseChanged> mListeners;

    /**
     * The DataBase constructor.
     *
     * Takes in a context which is needed to initialize the FireBase database, and initializes the
     * mLocalList (a List<Event> holding the events) and the mListeners (a List<DataBaseChanged>).
     * Later initializing the FireBase database.
     *
     * @param context
     */
    private DataBase(Context context){
        mContext = context;
        mLocalList = new ArrayList<>();
        mListeners = new ArrayList<>();
        initializeFireBase();
    }

    /**
     * Returns the static Database varible used throughout the app.
     *
     * @param context used to define the DataBase, if not done so already.
     * @return sDataBase which contains all the events in the FireBase database.
     */
    public static DataBase get(Context context){
        if (sDataBase == null){
            sDataBase = new DataBase(context);
        }
        return sDataBase;
    }

    public void clearListeners() {
        mListeners.clear();
    }

    public long getNewestEventTime() {
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date newestDate = null;

        if (newestDate == null){
            newestDate = new Date();
        }
        return newestDate.getTime();
    }

    interface DataBaseChanged{
        void itemAtPosChanged(int pos);
        void itemAddedAt(int pos);
    }

    /**
     * Initializes the FireBase database.
     *
     * Connects to the Golden App FireBase database using the FIREBASE_URL and the context passed in
     * when the Database was created. Once connected it will populate the mLocalList.
     *
     */
    private void initializeFireBase(){
        Firebase.setAndroidContext(mContext);
        Firebase firebaseRef = new Firebase(FIREBASE_URL);

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Event e = child.getValue(Event.class);
                        addOrUpdate(e);
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

    /**
     * Temporary function used to add items to the database.
     *
     * A function used during development which add two test events to the database.
     */
    private void tempAddToDB(){
        Firebase.setAndroidContext(mContext);
        Firebase fb = new Firebase(FIREBASE_URL);

        Event e = new Event("Baseball Game", "Test_Event: Baseball game in Lions Park at 7:00 pm on March 3rd, 2017.", "7:00 pm", "March 3, 2017", "Lions Park", 39.7547, -105.2291, "9:00 pm");
        Firebase eventRef = fb.child(e.getTitle());
        eventRef.setValue(e);

        Event e2 = new Event("Half Price Froyo at Goozell's", "Test_Event: Half price froyo at Goozell's from 2:00-3:00 pm on March 2nd, 2017.", "2:00 pm", "March 2, 2017", "Goozell", 39.755245, -105.221391, "3:00 pm");
        eventRef = fb.child(e2.getTitle());
        eventRef.setValue(e2);
    }

    /**
     * Takes in an event and will either add the event or update the event if it already exist.
     *
     * @param e is the event which should be added or updates in the database.
     */
    public void addOrUpdate(Event e){
        Log.d("***Event:  ", e.getTitle() + " added.");
        for(int i = 0; i < mLocalList.size(); i++){
            if (mLocalList.get(i).getId().equals(e.getId())) {
                mLocalList.set(i,e);
                for (DataBaseChanged listener : mListeners){
                    listener.itemAtPosChanged(i);
                }
                return;
            }
        }
        mLocalList.add(0,e);
        for (DataBaseChanged listener : mListeners){
            listener.itemAddedAt(0);
        }
    }

    /**
     * Returns the mLocalList variable which is a list of events, holding all the events.
     *
     * @return
     */
    public List<Event> getEventList() {
        return mLocalList;
    }

    /**
     * Takes in a UUID and returns the matching event.
     *
     * @param id a UUID which identifies which event to return
     * @return the event in mLocalList with the passed in id.
     */
    public Event getEvent(UUID id){
        for(Event event : mLocalList){
            if (event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }

    /**
     * Takes in an int parameter and will remove the item from the mLocalList List<Event>. Will not
     * check if the position is out of bounds.
     *
     * @param position the position in the mLocalList list<Event> which should be removed.
     */
    public void delateEventItem(int position){
        mLocalList.remove(position);
    }

    /**
     * Returns the size of the mLocalList list<Event>
     *
     * @return the int size of the mLocalList list<Event>
     */
    public int size(){
        return mLocalList.size();
    }

    public void addListener(DataBaseChanged listener){
        Log.d("***Event:  ", "Listener added.");
        mListeners.add(listener);
    }

}
