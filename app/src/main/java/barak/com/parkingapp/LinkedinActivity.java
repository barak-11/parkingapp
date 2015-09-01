package barak.com.parkingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class LinkedinActivity extends ActionBarActivity {

    WebView myWebViewHomeAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        myWebViewHomeAct = (WebView) findViewById(R.id.webViewLinkedin);
        WebViewClient myClient = new WebViewClient();
        myWebViewHomeAct.setWebViewClient(myClient);
        WebSettings webSettings = myWebViewHomeAct.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationDatabasePath(getFilesDir().getPath());
        webSettings.setGeolocationEnabled(true);


        String strUri = "https://il.linkedin.com/pub/barak-moshe-schieber/b0/858/9b7";

        myWebViewHomeAct.loadUrl(strUri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_linkedin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        SharedPreferences myDBfile; // create a file or return a reference to an exist file
        myDBfile = getSharedPreferences("file1", MODE_PRIVATE);

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
                lon = myDBfile.getString("longitude", "100");
                alt = myDBfile.getString("latitude", "100");
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
}

