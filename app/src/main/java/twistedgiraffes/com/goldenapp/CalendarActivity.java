package twistedgiraffes.com.goldenapp;

import java.util.Calendar;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        long startMillis = DataBase.get(getApplicationContext()).getNewestEventTime();
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        startActivity(intent);


        // One way to use the calendar widget is putting it in the xml file is shown in main.xml
        // setContentView(R.layout.main);

        /*
         Other way is to add is using the java code as follows.
		*/

        //Old calender Code

        //MonthView mv = new MonthView(this);
        //setContentView(mv);



        //Calendar cal = Calendar.getInstance();
        //cal.set(2012, Calendar.DECEMBER,12);
        //mv.GoToDate(cal.getTime());
    }
}
