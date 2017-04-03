package twistedgiraffes.com.goldenapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by rybailey on 3/8/17.
 */

public class EventActivity extends AppCompatActivity {
        private static final String EXTRA_EVENT_ID = "twistedgiraffes.com.goldenapp.event_id";


        private ViewPager mViewPager;
        private DataBase mDataBase;
        private List<Event> mEvent;

    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext, EventActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_pager);

        UUID eventId = (UUID) getIntent().getSerializableExtra(EXTRA_EVENT_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_event_pager_view_pager);

        mEvent = DataBase.get(this).getEventList();
        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Event event = mEvent.get(position);
                return EventFragment.newInstance(event.getId());
            }

            @Override
            public int getCount() {
                return mEvent.size();
            }
        });

        for (int i = 0; i < mEvent.size(); i++){
            if (mEvent.get(i).getId().equals(eventId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
