package barak.com.parkingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class NavigateActivity extends ActionBarActivity implements LocationListener {

    WebView myWeb;
    String alt;
    String lon;

    LocationManager _manager;
    double longitude;
    double latitude;

    TextView tvLong;
    TextView tvLati;
    TextView tvNewLong;
    TextView tvNewLati;

    SharedPreferences myDBfile; // create a file or return a reference to an exist file
    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);

        //Intent i = getIntent();
       // lon = i.getStringExtra(HomeActivity.keyMessage);
        //alt = i.getStringExtra(HomeActivity.keyMessage2);
        myDBfile = getSharedPreferences("file1", MODE_PRIVATE);
        myEditor=myDBfile.edit();
        lon=myDBfile.getString("longitude","100" );
        alt=myDBfile.getString("latitude","100" );



        _manager =(LocationManager) getSystemService(this.LOCATION_SERVICE);
        _manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this);


        Location l= _manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude=l.getLongitude();
        latitude=l.getLatitude();

        /*
        tvLong = (TextView) findViewById(R.id.textViewLong);
        tvLati = (TextView) findViewById(R.id.textViewLati);
        tvNewLong = (TextView) findViewById(R.id.textViewNewLong);
        tvNewLati = (TextView) findViewById(R.id.textViewNewLati);

        tvLong.setText(lon);
        tvLati.setText(alt);
        tvNewLong.setText(Double.toString(longitude));
        tvNewLati.setText(Double.toString(latitude));
    */

        myWeb=(WebView)findViewById(R.id.webViewNavigation);
        WebViewClient myClient= new WebViewClient();
        myWeb.setWebViewClient(myClient);
        //myWebViewHomeAct.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationDatabasePath(getFilesDir().getPath());
        webSettings.setGeolocationEnabled(true);


        String strUri ="https://www.google.co.il/maps/dir/"+latitude+","+longitude+"/"+Double.parseDouble(alt)+","+Double.parseDouble(lon)+"/@"+Double.parseDouble(alt)+","+Double.parseDouble(lon)+",16z/data=!4m2!4m1!3e2";
        myWeb.loadUrl(strUri);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.homeActivity:
                Intent myIntent;
                myIntent = new Intent(this, HomeActivity.class);
                startActivity(myIntent);
                return true;


            case R.id.navigate:
                Intent myIntentt;
                myIntentt = new Intent(this, NavigateActivity.class);
                startActivity(myIntentt);
                return true;
            case R.id.showParkingLots:
                // composeMessage();

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

    public void back(View view) {

        Intent myIntent;
        myIntent= new Intent(this,HomeActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onLocationChanged(Location location) {

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
}
