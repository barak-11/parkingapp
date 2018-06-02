package barak.com.parkingapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class SaveLocationActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    public static boolean saved = false;
    final static int MAX_HISTORY_SIZE = 30;
    SharedPreferences myDBfile; // create a file or return a reference to an exist file
    SharedPreferences.Editor myEditor;
    SharedPreferences myDBfileNew; // create a file or return a reference to an exist file
    SharedPreferences.Editor myEditorNew;

    LocationManager _manager;

    boolean exists=false;

    double longitude;
    double latitude;

    String [] history = new String[MAX_HISTORY_SIZE];
    Boolean [] historyBool = new Boolean[MAX_HISTORY_SIZE];

    String a;
    String b;

    int k=-1;
    int index;
    int keeper=0;

    TextView myTextViewLati;
    TextView myTextViewLong;

    TextView myTextViewLatiNotationN;
    TextView myTextViewAltiNotationE;

    private List<Place> list_places = new ArrayList<>();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private Place selectedPlace;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private String address="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_location);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Firebase
        try {
            initFirebase();
            addEventFirebaseListener();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        try {
            runLocationSettings();
            getLastLocation();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }




        for (int i=0;i<historyBool.length;i++)
        {
            historyBool[i]=false;
        }

        myDBfile = getSharedPreferences("file1", MODE_PRIVATE);
        myEditor = myDBfile.edit();
        myDBfileNew = getSharedPreferences("file2", MODE_PRIVATE);
        myEditorNew = myDBfileNew.edit();

        index = myDBfile.getInt("Index", 0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        try{
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            _manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
            _manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3 * 60 * 1000, 1, this);

            if (_manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)==null) {
                Toast.makeText(this, "No GPS signal", Toast.LENGTH_SHORT).show();
                Intent myIntent;
                myIntent = new Intent(this, HomeActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
                finish();

            }
            Location l = _manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = l.getLongitude();
            latitude = l.getLatitude();
        }catch (Exception e){
            Toast.makeText(this, "SaveLocationActivity OnCreate:: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }




        a = String.valueOf(longitude);
        b = String.valueOf(latitude);


        DecimalFormat df = new DecimalFormat("#.########");

        df.setRoundingMode(RoundingMode.FLOOR);

        double resultLO = new Double(df.format(longitude));
        double resultLA = new Double(df.format(latitude));

        myTextViewLati = (TextView) findViewById(R.id.latitude);
        myTextViewLati.setText(Double.toString(resultLA));

        myTextViewLong = (TextView) findViewById(R.id.longtitude);
        myTextViewLong.setText(Double.toString(resultLO));

        ///----
        myTextViewLatiNotationN = (TextView) findViewById(R.id.textViewNotationAltitude);
        myTextViewLatiNotationN.setText(" °N");

        myTextViewAltiNotationE = (TextView) findViewById(R.id.textViewNotationLongtitude);
        myTextViewAltiNotationE.setText(" °E");


    }

    private void addEventFirebaseListener() {
        //Progressing
        //circular_progress.setVisibility(View.VISIBLE);

        mDatabaseReference.child("place").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    if (list_places.size() > 0)
                        list_places.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String date_str = postSnapshot.child("createdDate").getValue().toString();

                        Place place = new Place(postSnapshot.child("address").getValue().toString(), postSnapshot.child("createdDate").getValue().toString(), postSnapshot.child("uid").getValue().toString(),postSnapshot.child("longitude").getValue().toString(),postSnapshot.child("latitude").getValue().toString());
                        list_places.add(place);
                        //currentSpendings+=Integer.valueOf(postSnapshot.child("price").getValue().toString());
                    }
                    //final TextView budget_tv = (TextView) findViewById(R.id.budget);
                    // remainedBudget = budget - currentSpendings;
                    //currentSpendings=0;
                    //budget_tv.setText(remainedBudget.toString());

                    //rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
                    PlaceAdapter pAdapter = new PlaceAdapter(list_places);
                    // Attach the adapter to the recyclerview to populate items

                    pAdapter.SetOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position, String id) {
                            //System.out.println("onItemClick MainActivity" + id);

                            //Place place = list_places.get(position);
                            //selectedPlace =place;

                        }

                        @Override
                        public void onLongItemClick(View view, int position, String id) {

                        }
                    });
                    LinearLayoutManager llm = new LinearLayoutManager(SaveLocationActivity.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    // Set layout manager to position the items

                    //circular_progress.setVisibility(View.INVISIBLE);


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


    public void save(View view) throws IOException {
        try{
            History history = new History();
            Geocoder geocoder = new Geocoder();

            exists=false;
            saved = true;
            String longtitudeFirebase=((TextView) findViewById(R.id.longtitude)).getText().toString();
            String latitudeFirebase=((TextView) findViewById(R.id.latitude)).getText().toString();
            myEditor.putString("longitude", ((TextView) findViewById(R.id.longtitude)).getText().toString());
            myEditor.putString("latitude", ((TextView) findViewById(R.id.latitude)).getText().toString());
            myEditor.putString("saved", Boolean.toString(saved));

            if (isFirstTime()) {
                myEditorNew.putString("k","0");
            }
            LatLng myLocation = new LatLng(latitude, longitude);
            if (history.mylist==null) {
                history.mylist = new ArrayList<String>();
            }
            history.mylist.add(myLocation.toString());


            if (geocoder.addresses==null) {
                geocoder.addresses = new ArrayList<Address>();
            }

            String test=myDBfileNew.getString("k","0");
            Log.d("k1", test);
            k=Integer.parseInt(test);
            if (k>200)
            {
                k=0;
            }
            myEditorNew.putString("test["+k+"]",myLocation.toString());
            k++;

            myEditorNew.putString("k",Integer.toString(k));

            myEditorNew.commit();
            myEditor.commit(); //"commit" saves the file


            Place place = new Place(address, Calendar.getInstance().getTime().toString(), UUID.randomUUID().toString(),longtitudeFirebase,latitudeFirebase);
            mDatabaseReference.child("place").child(place.getUid()).setValue(place);



            Intent myIntent;
            myIntent = new Intent(this, HomeActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);
            finish();
        }catch (Exception e){
            Toast.makeText(this, "Save: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onLocationChanged(Location location) {
        try{
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            _manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
            _manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this);


            Location l = _manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = l.getLongitude();
            latitude = l.getLatitude();
        }catch (Exception e){
            Toast.makeText(this, "onLocationChanged: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }



        myTextViewLati = (TextView) findViewById(R.id.latitude);
        myTextViewLati.setText(Double.toString(latitude));

        myTextViewLong = (TextView) findViewById(R.id.longtitude);
        myTextViewLong.setText(Double.toString(longitude));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void returnToMainActivity(View view) {

        Intent myIntentNew;
        myIntentNew = new Intent(this, HomeActivity.class);
        myIntentNew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntentNew);


    }


    public void updateCurrentLocation(View view) {
        try{
            Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            _manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
            _manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this);


            Location l = _manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = l.getLongitude();
            latitude = l.getLatitude();
        }catch (Exception e){
            Toast.makeText(this, "updateCurrentLocation: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }




        myTextViewLati = (TextView) findViewById(R.id.latitude);
        myTextViewLati.setText(Double.toString(latitude));

        myTextViewLong = (TextView) findViewById(R.id.longtitude);
        myTextViewLong.setText(Double.toString(longitude));

    }

    /*
        @Override
        protected void onStop() {
            super.onStop(); // in case the app crashes - onStop will save the data

            myEditor.putString("longitude", ((TextView) findViewById(R.id.longitude)).getText().toString());
            myEditor.putString("latitude", ((TextView) findViewById(R.id.latitude)).getText().toString());
            myEditor.commit();

        }
    */
    @Override
    public void onMapReady(GoogleMap map) {

        try{
            LatLng myLocation = new LatLng(latitude, longitude);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));
            Marker marker = null;

            marker = map.addMarker(new MarkerOptions()
                    .title("This is your Car")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                    .snippet("Know where your car is located at anytime")
                    .position(myLocation));

            dropPinEffect(marker);
        }catch (Exception e){
            Toast.makeText(this, "onMapReady: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window
                    marker.showInfoWindow();
                }
            }
        });
    }

    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
            // Send the SMS

        }
        return ranBefore;

    }
    private void getLastLocation() throws IOException{

        //final TextView latlngNew = (TextView)findViewById(R.id.latLng);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {



                            android.location.Geocoder geocoder;
                            List<Address> addresses;
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            geocoder = new android.location.Geocoder(SaveLocationActivity.this, Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                                Log.d("debug",address+","+city+","+state+","+country+","+postalCode+","+knownName);
                                //latlngNew.setText(address);
                            }
                            catch (Exception e){
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }


                        }
                        else {

                            Toast.makeText(getApplicationContext(),"location is null", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private  void runLocationSettings(){
        //Asking permission to access device's location
        ActivityCompat.requestPermissions(SaveLocationActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                1);
        mFusedLocationClient = getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
    }

}

