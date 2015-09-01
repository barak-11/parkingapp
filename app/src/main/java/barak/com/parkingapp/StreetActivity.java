package barak.com.parkingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class StreetActivity extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback {


    SharedPreferences myDBfile;
    SharedPreferences.Editor myEditor;
    String longitude;
    String altitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        myDBfile = getSharedPreferences("file1", MODE_PRIVATE);
        myEditor = myDBfile.edit();

        longitude = myDBfile.getString("longitude", "34.77539057");
        altitude = myDBfile.getString("latitude", "32.07481721");


        setContentView(R.layout.activity_street);
        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_street, menu);
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

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(new LatLng(Double.parseDouble(altitude), Double.parseDouble(longitude)));
    }
}
