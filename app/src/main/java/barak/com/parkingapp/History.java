package barak.com.parkingapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.support.v7.app.ActionBar;

public class History extends AppCompatActivity {


    public static ListView listView;
    SharedPreferences myDBfile; // create a file or return a reference to an exist file
    SharedPreferences.Editor myEditor;
    String[] values;
    String[] valuesdebug;
    public static List<String> mylist;

    public int listSize = 0;
    double test = 0;
    private List<Place> list_places = new ArrayList<>();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private Place selectedPlace;
    private ProgressBar circular_progress;
    RecyclerView rvContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Add toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        circular_progress = (ProgressBar)findViewById(R.id.circular_progress);
        TextView tvTotal = (TextView) findViewById(R.id.textViewHistory);
        tvTotal.setVisibility(View.INVISIBLE);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvContacts.setLayoutManager(llm);
        PlaceAdapter pAdapter = new PlaceAdapter(list_places);
        rvContacts.setAdapter( pAdapter );
        //Firebase
        try {
            initFirebase();
            addEventFirebaseListener();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    private void addEventFirebaseListener() {
        //Progressing
        circular_progress.setVisibility(View.VISIBLE);

        mDatabaseReference.child("place").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    if (list_places.size() > 0)
                        list_places.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        Place place = new Place(postSnapshot.child("address").getValue().toString(), postSnapshot.child("createdDate").getValue().toString(), postSnapshot.child("uid").getValue().toString(),postSnapshot.child("longitude").getValue().toString(),postSnapshot.child("latitude").getValue().toString());
                        list_places.add(place);
                    }
                    TextView tvTotal = (TextView) findViewById(R.id.textViewHistory);
                    tvTotal.setText(String.valueOf(list_places.size()+" records in total"));

                    rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
                    PlaceAdapter pAdapter = new PlaceAdapter(list_places);

                    pAdapter.SetOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position, String id) {
                            System.out.println("onItemClick" + id);

                            Place place = list_places.get(position);
                            selectedPlace =place;
                        }

                        @Override
                        public void onLongItemClick(View v, int position, String id) {

                        }
                    });
                    pAdapter.SetOnLongItemClickListener(new PlaceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position, String id) {

                        }

                        @Override
                        public void onLongItemClick(View v, int position, String id) {
                              System.out.println("onItemLongClick" + id);

                              Place place = list_places.get(position);
                             selectedPlace =place;
                        }
                    });

                    LinearLayoutManager llm = new LinearLayoutManager(History.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    rvContacts.setLayoutManager(llm);
                    rvContacts.setAdapter(pAdapter);
                    // Set layout manager to position the items

                    circular_progress.setVisibility(View.INVISIBLE);
                    tvTotal.setVisibility(View.VISIBLE);


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "onDataChange() error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Budget", "onDataChange() error", e);

                }
            }




            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference  = mFirebaseDatabase.getReference();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_location, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Uri gmmIntentUri;
        Intent mapIntent;
        switch (item.getItemId()) {
            case R.id.homeActivity:
                Intent myIntent;
                myIntent = new Intent(this, HomeActivity.class);
                startActivity(myIntent);
                return true;


            case R.id.navigate:
                String alt, lon;
                lon = myDBfile.getString("longitude", "34.77539057");
                alt = myDBfile.getString("latitude", "32.07481721");
                gmmIntentUri = Uri.parse("google.navigation:q=" + Double.parseDouble(alt) + "," + Double.parseDouble(lon) + "&mode=w");
                mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);

                }
                return true;
            case R.id.showParkingLots:
                gmmIntentUri = Uri.parse("geo:0,0?q=parking+lots");
                mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);

                }
                return true;
            default:
        }

        switch (id) {
            case R.id.action_settings:
                Intent myIntentSettings;
                myIntentSettings = new Intent(this, SettingsActivity.class);
                startActivity(myIntentSettings);
                //here is changing in the float icon aka settings
                return true;
            case R.id.about_the_developer:
                Intent myIntentAbout;
                myIntentAbout = new Intent(this, ActivityAbout.class);
                startActivity(myIntentAbout);
                //here is changing in the float icon aka settings
                return true;
            default:
                //noinspection SimplifiableIfStatement
                if (id == R.id.action_settings) {
                    return true;
                }

                return super.onOptionsItemSelected(item);
        }
    }

}


