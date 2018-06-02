package barak.com.parkingapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

                        Place place = new Place(postSnapshot.child("address").getValue().toString(), postSnapshot.child("createdDate").getValue().toString(), postSnapshot.child("uid").getValue().toString());
                        list_places.add(place);
                    }
                    TextView tvTotal = (TextView) findViewById(R.id.textViewHistory);
                    tvTotal.setText(String.valueOf(list_places.size()+" records in total"));

                    rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
                    PlaceAdapter pAdapter = new PlaceAdapter(list_places);

                    pAdapter.SetOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position, String id) {
                            System.out.println("onItemClick MainActivity" + id);

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
}


