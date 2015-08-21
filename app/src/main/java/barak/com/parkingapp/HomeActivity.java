package barak.com.parkingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vstechlab.easyfonts.EasyFonts;


public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {


    SharedPreferences myDBfile;
    SharedPreferences.Editor myEditor;

    String longtitude;
    String altitude;

    TextView tvWelcomeBack;
    TextView tvCarNumber;
    TextView tvCarType;
    TextView tvDriverNAme;
    TextView tvDriverCarNumber;
    TextView tvDriverCarType;
    TextView tvCarLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myDBfile = getSharedPreferences("file1", MODE_PRIVATE);
        myEditor=myDBfile.edit();


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

        longtitude = myDBfile.getString("longitude", "34.77539057");
        altitude = myDBfile.getString("latitude", "32.07481721");

        ((TextView) findViewById(R.id.driverNameID)).setText(x);
        ((TextView) findViewById(R.id.CarNumberID)).setText(z);
        ((TextView) findViewById(R.id.CarTypeID)).setText(y);


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
                gmmIntentUri = Uri.parse("http://maps.google.com/maps?q=&layer=c&cbll=" + Double.parseDouble(altitude) + "," + Double.parseDouble(longtitude) + "&cbp=11,0,0,0,0");
                mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);

                }

                return true;
            case R.id.mylinkdinpage:
                Intent myIntentLinkedin;
                myIntentLinkedin = new Intent(this, LinkedinActivity.class);
                startActivity(myIntentLinkedin);
                return true;
            case R.id.demoMap:
                Intent myMapIntent;
                myMapIntent = new Intent(this, MapsActivity.class);
                startActivity(myMapIntent);
                return true;
            case R.id.clearCache:

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

        gmmIntentUri = Uri.parse("google.navigation:q=" + Double.parseDouble(altitude) + "," + Double.parseDouble(longtitude) + "&mode=w");
        mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }


    }

    @Override
    public void onMapReady(GoogleMap map) {


        LatLng sydney = new LatLng(Double.parseDouble(altitude), Double.parseDouble(longtitude));

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
        Marker marker = null;
        String temp = myDBfile.getString("saved", "false");
/*
Marker marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(latitude, longitude)));
*/

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
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    .snippet("This is Where You've Parked Your Car")
                    .position(sydney));
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
}
