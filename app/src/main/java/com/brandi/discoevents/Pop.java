package com.brandi.discoevents;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.brandi.discoevents.R.id.listPopWindow;

/**
 * Created by Owner on 11/25/2017.
 */

public class Pop extends Activity {

    globals g = globals.getInstance();
    private final String date = g.getMonth() + "-" + g.getDay() + "-" + g.getYear();
    private static final String TAG = "Pop Activity Data";

    // This will hold our collection of com.brandi.discoevents.EventData Objects that will be printed to the screen
    final ArrayList<EventData> events = new ArrayList<EventData>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.8));

       // Toast.makeText(Pop.this, " " + date, Toast.LENGTH_LONG).show();

        // New instance of text view to change the date on the pop up to display the date the user clicked
        TextView textDate = (TextView) findViewById(R.id.textHeader);
        textDate.setText("Events on " + date + ":");

        // Get a reference to our Events
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        // Attach a listener to read the data at our posts reference
        ref.child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // get all of the children at this level.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                // Iterate over the collection called children
                // Each time you get to a child put it in the collection variable
                for (DataSnapshot child : children) {
                    // Returned as a java object
                    EventData value = child.getValue(EventData.class);

                    // Checking for the date that was selected and if the date the same as the values then add it to the event list
                    if(date.equals(value.getEventDate())) {
                        events.add(value);
                    }
                }
                showListNow();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Display a toast Error message
                Log.d(TAG, " Error ");
                Toast.makeText(Pop.this, "The read failed: " + databaseError.getCode(), Toast.LENGTH_LONG).show();
            }

        });

        // Make array adapter to show results
        ListView listview = (ListView) findViewById(listPopWindow);
        ListAdapter eventAdapter = new CustomAdapter(this, events);
        listview.setAdapter(eventAdapter);
    }

    private void showListNow() {
        // Make array adapter to show results
        ListView listview = (ListView) findViewById(listPopWindow);
        ListAdapter eventAdapter = new CustomAdapter(this, events);
        listview.setAdapter(eventAdapter);
    }
}
