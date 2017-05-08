package twistedgiraffes.com.goldenapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;


/**
 * The Activity which host the EventFragment.
 *
 * The Activity which host the EventFragment. Requires a UUID to be passed in -- the eventID. Which
 * is utilized as the an identifier for the event which needs to be displayed from the database in
 * the EventFragment.
 */
public class EventDetailActivity extends SingleFragmentActivity {
    private static final String ARG_EVENT_ID = "event_id";

    /**
     * The function returning an intent with the passed in parameters as Extras. Uses the eventId
     * parameter to deliver it to the Event Fragment which uses it to identify which event to
     * display.
     *
     * @param packageContext
     * @param eventId used to identify which event to display.
     * @return intent which holds the needed parameters for the EventFragment.
     */
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
