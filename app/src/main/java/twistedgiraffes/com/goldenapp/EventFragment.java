package twistedgiraffes.com.goldenapp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by rybailey on 3/8/17.
 */

public class EventFragment extends Fragment {
    private static final String ARG_EVENT_ID = "event_id";
    private static final String TAG = "EventFragment";

    private Event mEvent;
    private ImageView mImage;
    private TextView mTitle;
    private TextView mTime;
    private TextView mDate;
    private TextView mLocation;
    private TextView mDescription;
    private Button mAddToCalendar;

    public static EventFragment newInstance(UUID eventId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT_ID, eventId);

        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID eventId = (UUID) getArguments().getSerializable(ARG_EVENT_ID);
        mEvent = DataBase.get(getActivity()).getEvent(eventId);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.event_full_page,container,false);

        mImage = (ImageView) v.findViewById(R.id.event_photo);
        mTitle = (TextView) v.findViewById(R.id.event_title);
        mTime = (TextView) v.findViewById(R.id.event_time);
        mDate = (TextView) v.findViewById(R.id.event_date);
        mLocation = (TextView) v.findViewById(R.id.event_location);
        mDescription = (TextView) v.findViewById(R.id.event_description);
        mAddToCalendar = (Button) v.findViewById(R.id.event_add_to_calendar);
        mAddToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat format = new SimpleDateFormat("MMMM d, yyyy hh:mm a", Locale.ENGLISH);
                try {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(getContext(), Locale.getDefault());
                    addresses = geocoder.getFromLocation(mEvent.getLat(), mEvent.getLng(), 1);
                    Date date = format.parse(mEvent.getDate() + " " + mEvent.getTime());
                    Date end = format.parse(mEvent.getDate() + " " + mEvent.getEndTime());
                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date.getTime())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end.getTime())
                            .putExtra(CalendarContract.Events.TITLE, mEvent.getTitle())
                            .putExtra(CalendarContract.Events.DESCRIPTION, mEvent.getDescription())
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, addresses.get(0));
                    startActivity(intent);
                } catch (ParseException e) {
                    Log.e(TAG, "Failed to pare date for event: " + mEvent.getTitle());
                } catch (IOException e) {
                    Log.e(TAG, "Location to Address failed with code :" + e);
                }

            }
        });

        mImage.setImageResource(R.mipmap.ic_launcher);
        mTitle.setText(mEvent.getTitle());
        mTime.setText(mEvent.getTime());
        mDate.setText(mEvent.getDate());
        mLocation.setText(mEvent.getLocation());
        mDescription.setText(mEvent.getDescription());

        return v;
    }
}
