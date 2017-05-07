package twistedgiraffes.com.goldenapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Sergio on 5/7/2017.
 */

public class EventDetailActivity extends SingleFragmentActivity {
    private static final String ARG_EVENT_ID = "event_id";

    public static Intent newIntent(Context packageContext, UUID eventId){
        Intent intent = new Intent(packageContext, EventDetailActivity.class);
        intent.putExtra(ARG_EVENT_ID, eventId.toString());

        return intent;
    }

    /**
     * Returns an EventFragment Instance, which starts the detailed view of an event.
     *
     * Calls the EventFragment newInstance method which returns an Event Fragment which will
     * display the events deatails such as title, time, date, location, and a detailed description.
     *
     * @return EventFragment which initiates the detail view of an event
     */
    @Override
    protected Fragment createFragment() {
        String eventIdStr = getIntent().getStringExtra(ARG_EVENT_ID);
        UUID eventId = UUID.fromString(eventIdStr);

        return EventFragment.newInstance(eventId);
    }
}
