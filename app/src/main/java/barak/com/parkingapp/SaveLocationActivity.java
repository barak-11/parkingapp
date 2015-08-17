package barak.com.parkingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;


public class SaveLocationActivity extends ActionBarActivity implements LocationListener {

    SharedPreferences myDBfile; // create a file or return a reference to an exist file
    SharedPreferences.Editor myEditor;

    LocationManager _manager;

   // WebView myWebViewHomeAct;
    WebView myWebViewSaveLocationAct;

    double longitude;
    double latitude;

    String a;
    String b;

    TextView myTextViewLati;
    TextView myTextViewLong;

    TextView myTextViewLatiNotationN;
    TextView myTextViewAltiNotationE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_location);


        myDBfile = getSharedPreferences("file1", MODE_PRIVATE);
        myEditor=myDBfile.edit();

        //// the code above is for the Database, the one beneath is for the GPS


        myWebViewSaveLocationAct=(WebView)findViewById(R.id.webViewSaveLocation);
        myWebViewSaveLocationAct.setWebViewClient(new WebViewClient());

        WebSettings webSettings = myWebViewSaveLocationAct.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationDatabasePath(getFilesDir().getPath());
        webSettings.setGeolocationEnabled(true);




        _manager =(LocationManager) getSystemService(this.LOCATION_SERVICE);
        _manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3 * 60 * 1000, 1, this);


        Location l= _manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude=l.getLongitude();
        latitude=l.getLatitude();

        a=String.valueOf(longitude);
        b=String.valueOf(latitude);

        myTextViewLati=(TextView)findViewById(R.id.latitude);
        myTextViewLati.setText(Double.toString(latitude));

        myTextViewLong=(TextView)findViewById(R.id.longtitude);
        myTextViewLong.setText(Double.toString(longitude));

        ///----
        myTextViewLatiNotationN=(TextView)findViewById(R.id.textViewNotationAltitude);
        myTextViewLatiNotationN.setText("°N");

        myTextViewAltiNotationE=(TextView)findViewById(R.id.textViewNotationLongtitude);
        myTextViewAltiNotationE.setText("°E");

        String strUri="https://www.google.co.il/maps/@"+latitude+","+longitude+",16z";

        myWebViewSaveLocationAct.loadUrl(strUri);



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
                String alt,lon;
                lon=myDBfile.getString("longitude","100" );
                alt=myDBfile.getString("latitude","100" );
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
            case R.id.mylinkdinpage:
                // composeMessage();
                Intent myIntentLinkedin;
                myIntentLinkedin = new Intent(this, LinkedinActivity.class);
                startActivity(myIntentLinkedin);
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


    public void save(View view) {


        myEditor.putString("longitude", ((TextView) findViewById(R.id.longtitude)).getText().toString());
        myEditor.putString("latitude", ((TextView) findViewById(R.id.latitude)).getText().toString());
        myEditor.commit(); //"commit" saves the file
        Intent myIntent;
        myIntent= new Intent(this,HomeActivity.class);
        startActivity(myIntent);
    }


    @Override
    public void onLocationChanged(Location location) {

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        //myWebViewSaveLocationAct=(WebView)findViewById(R.id.webViewSaveLocation);

        a=String.valueOf(longitude);
        b=String.valueOf(latitude);

        myTextViewLati=(TextView)findViewById(R.id.latitude);
        myTextViewLati.setText(Double.toString(latitude));

        myTextViewLong=(TextView)findViewById(R.id.longtitude);
        myTextViewLong.setText(Double.toString(longitude));

        String strUri="https://www.google.co.il/maps/@"+latitude+","+longitude+",13z";
       // String strUri="www.ynet.co.il";

        myWebViewSaveLocationAct.loadUrl(strUri);
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

        Intent myIntent;
        myIntent= new Intent(this,HomeActivity.class);
        startActivity(myIntent);
}

    public void updateCurrentLocation(View view) {

        _manager =(LocationManager) getSystemService(this.LOCATION_SERVICE);
        _manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this);


        Location l= _manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude=l.getLongitude();
        latitude=l.getLatitude();


        myTextViewLati=(TextView)findViewById(R.id.latitude);
        myTextViewLati.setText(Double.toString(latitude));

        myTextViewLong=(TextView)findViewById(R.id.longtitude);
        myTextViewLong.setText(Double.toString(longitude));

    }

    @Override
    protected void onStop() {
        super.onStop(); // in case the app crashes - onStop will save the data

        myEditor.putString("longitude", ((TextView) findViewById(R.id.longtitude)).getText().toString());
        myEditor.putString("latitude", ((TextView) findViewById(R.id.latitude)).getText().toString());
        myEditor.commit();

    }

}

