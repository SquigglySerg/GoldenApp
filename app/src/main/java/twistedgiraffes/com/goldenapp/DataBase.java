package twistedgiraffes.com.goldenapp;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by rybailey on 4/3/17.
 */

public class DataBase {
    private static final String FIREBASE_URL = "https://banded-charmer-160001.firebaseio.com/";
    private HashMap<String, Event> events;
    private static DataBase sDataBase;
    private Context mContext;

    public List<Event> mLocalList;
    private List<DataBaseChanged> mListeners;

    public void clearListeners() {
        mListeners.clear();
    }

    interface DataBaseChanged{
        void itemPosChange(int pos);
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
                        callListeners(addOrUpdate(e));
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

    private void tempAddToDB(){
        Firebase.setAndroidContext(mContext);
        Firebase fb = new Firebase(FIREBASE_URL);

        Event e = new Event("TEST1 TITLE", "DESC", "7:00 pm", "March 3, 2017", "Lions Park", 39.7554, -105.2213);
        Firebase eventRef = fb.child(e.getTitle());
        eventRef.setValue(e);

        Event e2 = new Event("TEST2 TITLE", "DESC 2", "2:00 pm", "March 2, 2017", "Lions Park 2", 39.7554, -105.2213);
        eventRef = fb.child(e2.getTitle());
        eventRef.setValue(e2);
    }

    public int addOrUpdate(Event e){
        Log.d("***Event:  ", e.getTitle() + " added.");
        for(int i = 0; i < mLocalList.size(); i++){
            if (mLocalList.get(i).getId().equals(e.getId())) {
                mLocalList.set(i,e);
                return i;
            }
        }
        mLocalList.add(e);
        return mLocalList.size() - 1;
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

    private void callListeners(int pos){
        for (DataBaseChanged listener : mListeners){
            listener.itemPosChange(pos);
        }
    }
}
