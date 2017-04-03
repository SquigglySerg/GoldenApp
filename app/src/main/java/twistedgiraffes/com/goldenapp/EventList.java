package twistedgiraffes.com.goldenapp;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rybailey on 2/27/17.
 */
public class EventList {
    private static EventList sEventList;
    private List<Event> mEvents;

    public static EventList get(Context context){
        if (sEventList == null){
            sEventList = new EventList(context);
        }
        return sEventList;
    }

    private EventList(Context context){
        mEvents = new ArrayList<>();
        /*
        Event event = new Event();
        event.setTitle("Breaking Event: This is a Headline");
        event.setDescription("Blah Blah Blah. You now can see this. In theory. Hopefully");
        mEvents.add(event);
        for (int i = 0; i < 15; i++){
            event = new Event();
            event.setTitle("Headline " + i);
            event.setDescription("Example Story. Will be more here. Currently no database integration either. I will mostly likely will add a picture also because I can.");
            mEvents.add(event);
        }
        */
    }

    public void addOrUpdate(Event e){
        Log.d("***Event:  ", e.getTitle() + " added.");
        for(int i = 0; i < mEvents.size(); i++){
            if (mEvents.get(i).getId().equals(e.getId())) {
                mEvents.set(i,e);
                return;
            }
        }
        mEvents.add(e);
    }

    public List<Event> getEventList() {
        return mEvents;
    }

    public Event getEvent(UUID id){
        for(Event event : mEvents){
            if (event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }

    public void delateEventItem(int position){
        mEvents.remove(position);
    }

    public int size(){
        return mEvents.size();
    }
}
