package twistedgiraffes.com.goldenapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rybailey on 2/27/17.
 */
public class EventList {
    private static EventList sEventList;
    private List<Event> mEvent;

    public static EventList get(Context context){
        if (sEventList == null){
            sEventList = new EventList(context);
        }
        return sEventList;
    }

    private EventList(Context context){
        mEvent = new ArrayList<>();
        Event event = new Event();
        event.setTitle("Breaking Event: This is a Headline");
        event.setDescription("Blah Blah Blah. You now can see this. In theory. Hopefully");
        mEvent.add(event);
        for (int i = 0; i < 15; i++){
            event = new Event();
            event.setTitle("Headline " + i);
            event.setDescription("Example Story. Will be more here. Currently no database integration either. I will mostly likely will add a picture also because I can.");
            mEvent.add(event);
        }
    }

    public List<Event> getEventList() {
        return mEvent;
    }

    public Event getEvent(UUID id){
        for(Event event : mEvent){
            if (event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }

    public void delateEventItem(int position){
        mEvent.remove(position);
    }

    public int size(){
        return mEvent.size();
    }
}
