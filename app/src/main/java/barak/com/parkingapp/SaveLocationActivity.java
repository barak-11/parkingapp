package barak.com.parkingapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class SaveLocationActivity extends ActionBarActivity implements LocationListener, OnMapReadyCallback {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_location);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);




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


        _manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        _manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3 * 60 * 1000, 1, this);

        Location l = _manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = l.getLongitude();
        latitude = l.getLatitude();



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

        History history = new History();
        Geocoder geocoder = new Geocoder();

        exists=false;
        saved = true;
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
        if (k>100)
        {
            k=0;
        }
        myEditorNew.putString("test["+k+"]",myLocation.toString());
        k++;

        myEditorNew.putString("k",Integer.toString(k));

        myEditorNew.commit();
        myEditor.commit(); //"commit" saves the file



        Intent myIntent;
        myIntent = new Intent(this, HomeActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        finish();
    }


    @Override
    public void onLocationChanged(Location location) {

        _manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        _manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this);


        Location l = _manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = l.getLongitude();
        latitude = l.getLatitude();


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

        Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT).show();
        _manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        _manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this);


        Location l = _manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = l.getLongitude();
        latitude = l.getLatitude();



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


        LatLng myLocation = new LatLng(latitude, longitude);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));
        Marker marker = null;

        marker = map.addMarker(new MarkerOptions()
                .title("This is your Car")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .snippet("Know where your car is located at anytime")
                .position(myLocation));

        dropPinEffect(marker);

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

}

