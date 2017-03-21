package twistedgiraffes.com.goldenapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

/**
 * Created by michael on 3/3/2017.
 */

/*
* I will make this a fragment later
*
*
* Will have a set number of tickets that the user can get
* They must be in a specific location to get each ticket
*
* Need a class that will contain the string value of that ticket and its location
* Need fragment similar to the fragment in crim init
* */
public class TicketActivity extends AppCompatActivity {
    private static final String KEY_CLICKED = "clicked";

    private CheckBox mCheckBox;

    private boolean mChecked = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ticket);

        mCheckBox = (CheckBox) findViewById( R.id.ticket_checkbox );
        if (mCheckBox.isChecked()) {
            mChecked = true;
        }
    }
}
