package barak.com.parkingapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.vstechlab.easyfonts.EasyFonts;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, SwipeRefreshLayout.OnRefreshListener {




    String testString;

    SharedPreferences myDBfile;
    SharedPreferences.Editor myEditor;

    String longitude;
    String altitude;

    TextView tvWelcomeBack;
    TextView tvCarNumber;
    TextView tvCarType;
    TextView tvDriverNAme;
    TextView tvDriverCarNumber;
    TextView tvDriverCarType;
    TextView tvCarLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private String address="";

    SwipeRefreshLayout mSwipeRefreshLayout;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();


        int ori = getResources().getConfiguration().orientation;
        if (ori == 1) {
            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
            mSwipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_red_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);

            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myDBfile = getSharedPreferences("file1", MODE_PRIVATE);
        myEditor = myDBfile.edit();


        tvWelcomeBack = (TextView) findViewById(R.id.welcomeBackID);
        tvCarNumber = (TextView) findViewById(R.id.textView2);
        tvCarType = (TextView) findViewById(R.id.textView);
        tvDriverNAme = (TextView) findViewById(R.id.driverNameID);
        tvDriverCarNumber = (TextView) findViewById(R.id.CarNumberID);
        tvDriverCarType = (TextView) findViewById(R.id.CarTypeID);
        tvCarLocation = (TextView) findViewById(R.id.textView4);

        tvWelcomeBack.setTypeface(EasyFonts.robotoThin(this));
        tvCarNumber.setTypeface(EasyFonts.robotoThin(this));
        tvCarType.setTypeface(EasyFonts.robotoThin(this));
        tvDriverNAme.setTypeface(EasyFonts.robotoThin(this));
        tvDriverCarNumber.setTypeface(EasyFonts.robotoThin(this));
        tvDriverCarType.setTypeface(EasyFonts.robotoThin(this));
        tvCarLocation.setTypeface(EasyFonts.robotoThin(this));

        String x = myDBfile.getString("Name", "Yossi Cohen"); // asking for KEY names x (was created in save method) or a default file (in case x doesn't exist) named "haha"
        String y = myDBfile.getString("CarType", "Honda");
        String z = myDBfile.getString("CarNumber", "11-222-33");

        longitude = myDBfile.getString("longitude", "34.77539057");
        altitude = myDBfile.getString("latitude", "32.07481721");

        ((TextView) findViewById(R.id.driverNameID)).setText(x);
        ((TextView) findViewById(R.id.CarNumberID)).setText(z);
        ((TextView) findViewById(R.id.CarTypeID)).setText(y);

        myEditor.apply(); //"commit" saves the file


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

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
        Intent street;
        Intent history;
        switch (item.getItemId()) {
            case R.id.lastparkingplace:
                Intent myIntent;
                myIntent = new Intent(this, SaveLocationActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
                return true;

            case R.id.showParkingLots:
                gmmIntentUri = Uri.parse("geo:0,0?q=parking+lots");
                mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);

                }

                return true;
            case R.id.streetv:
                street = new Intent(this, StreetActivity.class);
                startActivity(street);

                return true;
            case R.id.historynew:
                try{
                    history = new Intent(this, Geocoder.class);
                    startActivity(history);
                }catch (Exception e){
                    Toast.makeText(this, "History: "+e.getMessage(), Toast.LENGTH_LONG).show();
                }


                return true;

            case R.id.clearCache:

                Toast.makeText(this, "Tip - Next Time Swipe Down the Screen to Refresh", Toast.LENGTH_LONG).show();
                myEditor.putString("longitude", "34.77539057");
                myEditor.putString("latitude", "32.07481721");
                myEditor.putString("saved", "false");
                myEditor.commit(); //"commit" saves the file

                Intent clearIntent;
                clearIntent = new Intent(this, HomeActivity.class);
                startActivity(clearIntent);
                //finish();
                return true;


            default:
        }


        switch (id) {
            case R.id.action_settings:
                Intent myIntentSettings;
                myIntentSettings = new Intent(this, SettingsActivity.class);
                myIntentSettings.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntentSettings);

                return true;
            case R.id.about_the_developer:
                Intent myIntentAbout;
                myIntentAbout = new Intent(this, ActivityAbout.class);
                startActivity(myIntentAbout);
                //here is changing in the float icon aka settings
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }


    public void nextActivity(View view) {


        Intent myIntent;
        myIntent = new Intent(this, SaveLocationActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
    }

    public void Navigate(View view) {

        Uri gmmIntentUri;
        Intent mapIntent;

        gmmIntentUri = Uri.parse("google.navigation:q=" + Double.parseDouble(altitude) + "," + Double.parseDouble(longitude) + "&mode=w");
        mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }


    }

    @Override
    public void onMapReady(GoogleMap map) {

    try{
        LatLng sydney = new LatLng(Double.parseDouble(altitude), Double.parseDouble(longitude));

        Log.d("sydney",sydney.toString());
        ActivityCompat.requestPermissions(HomeActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                1);
        mFusedLocationClient = getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
        Marker marker = null;
        String temp = myDBfile.getString("saved","false");
        Log.d("myDBFiletemp",temp);


        if (temp.equals("false")) {
            marker = map.addMarker(new MarkerOptions()
                    .title("Welcome!")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .snippet("Click Save to Store Your Car's Location")
                    .position(sydney));
            dropPinEffect(marker);
        } else if (temp.equals("true")) {
            marker = map.addMarker(new MarkerOptions()
                    .title("Your Car")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .snippet("This is Where You've Parked Your Car")
                    .position(sydney));
        }
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

    @Override
    public void onRefresh() {
        Toast.makeText(this, "Clear Cache", Toast.LENGTH_SHORT).show();

        mSwipeRefreshLayout.setRefreshing(true);
        myEditor.putString("longitude", "34.77539057");
        myEditor.putString("latitude", "32.07481721");
        myEditor.putString("saved", "false");
        myEditor.commit(); //"commit" saves the file
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                recreate();
            }
        }, 3000);
    }

}




