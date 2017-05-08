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

public class DataBase {
    private static DataBase sDataBase;
    private static final String FIREBASE_URL = "https://banded-charmer-160001.firebaseio.com/";


    private HashMap<String, Event> events;
    private Context mContext;

    public List<Event> mLocalList;
    private List<DataBaseChanged> mListeners;

    public void clearListeners() {
        mListeners.clear();
    }

    public long getNewestEventTime() {
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date newestDate = null;
       /* Fancy code to get the newset event. Not needed under the new design.
       for (Event event : mLocalList){
            try {
                Date date = format.parse(event.getDate());
                if (newestDate == null){
                    newestDate = date;
                } else {
                    if (date.before(newestDate)){
                        newestDate=date;
                    }
                }
            } catch (ParseException e) {
                Log.e("Calender", "Failed to pare date form event: " + event.getTitle());
            }
        }
        */
        if (newestDate == null){
            newestDate = new Date();
        }
        return newestDate.getTime();
    }

    interface DataBaseChanged{
        void itemAtPosChanged(int pos);
        void itemAddedAt(int pos);
    }

    public static DataBase get(Context context){
        if (sDataBase == null){
            sDataBase = new DataBase(context);
        }
        return sDataBase;
    }


    private DataBase(Context context){
        mContext = context;
        mLocalList = new ArrayList<>();
        mListeners = new ArrayList<>();
        events = new HashMap<>();
        initializeFireBase();
    }
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

    public List<Event> getEventList() {
        return mLocalList;
    }

    public Event getEvent(UUID id){
        for(Event event : mLocalList){
            if (event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }

    public void delateEventItem(int position){
        mLocalList.remove(position);

    }

    public int size(){
        return mLocalList.size();
    }

    public void addListener(DataBaseChanged listener){
        Log.d("***Event:  ", "Listener added.");
        mListeners.add(listener);
    }

}
