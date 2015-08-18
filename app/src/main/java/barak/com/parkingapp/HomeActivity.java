package barak.com.parkingapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.vstechlab.easyfonts.EasyFonts;


public class HomeActivity extends AppCompatActivity {


    SharedPreferences myDBfile;
    SharedPreferences.Editor myEditor;

    WebView myWebViewHomeAct;
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

        myDBfile = getSharedPreferences("file1", MODE_PRIVATE);


        tvWelcomeBack=(TextView)findViewById(R.id.welcomeBackID);
        tvCarNumber=(TextView)findViewById(R.id.textView2);
        tvCarType=(TextView)findViewById(R.id.textView);
        tvDriverNAme=(TextView)findViewById(R.id.driverNameID);
        tvDriverCarNumber=(TextView)findViewById(R.id.CarNumberID);
        tvDriverCarType=(TextView)findViewById(R.id.CarTypeID);
        tvCarLocation=(TextView)findViewById(R.id.textView4);

        tvWelcomeBack.setTypeface(EasyFonts.robotoThin(this));
        tvCarNumber.setTypeface(EasyFonts.robotoThin(this));
        tvCarType.setTypeface(EasyFonts.robotoThin(this));
        tvDriverNAme.setTypeface(EasyFonts.robotoThin(this));
        tvDriverCarNumber.setTypeface(EasyFonts.robotoThin(this));
        tvDriverCarType.setTypeface(EasyFonts.robotoThin(this));
        tvCarLocation.setTypeface(EasyFonts.robotoThin(this));

        String x=myDBfile.getString("Name","Yossi Cohen"); // asking for KEY names x (was created in save method) or a default file (in case x doesn't exist) named "haha"
        String y=myDBfile.getString("CarType","Honda");
        String z=myDBfile.getString("CarNumber","11-222-33");

        longtitude = myDBfile.getString("longitude", "34.77539057");
        altitude = myDBfile.getString("latitude", "32.07481721");

        ((TextView)findViewById(R.id.driverNameID)).setText(x);
        ((TextView)findViewById(R.id.CarNumberID)).setText(z);
        ((TextView)findViewById(R.id.CarTypeID)).setText(y);


        myWebViewHomeAct=(WebView)findViewById(R.id.webViewCurrentCarLocation);
        WebViewClient myClient= new WebViewClient();
        myWebViewHomeAct.setWebViewClient(myClient);
        WebSettings webSettings = myWebViewHomeAct.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationDatabasePath(getFilesDir().getPath());
        webSettings.setGeolocationEnabled(true);


        String strUri="https://www.google.co.il/maps/@"+Double.parseDouble(altitude)+","+Double.parseDouble(longtitude)+",16z";

        myWebViewHomeAct.loadUrl(strUri);
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
        switch (item.getItemId()){
            case R.id.lastparkingplace:
                Intent myIntent;
                myIntent= new Intent(this,SaveLocationActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
                finish();
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
                gmmIntentUri = Uri.parse("http://maps.google.com/maps?q=&layer=c&cbll="+Double.parseDouble(altitude)+","+Double.parseDouble(longtitude)+"&cbp=11,0,0,0,0");
                mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);

                }

                return true;
            case R.id.mylinkdinpage:
               // composeMessage();
                Intent myIntentLinkedin;
                myIntentLinkedin= new Intent(this,LinkedinActivity.class);
                startActivity(myIntentLinkedin);
                return true;
            case R.id.demoMap:
                // composeMessage();
                Intent myMapIntent;
                myMapIntent= new Intent(this,MapsActivity.class);
                startActivity(myMapIntent);
                return true;




            default:
        }



        switch(id) {
            case R.id.action_settings:
                Intent myIntentSettings;
                myIntentSettings= new Intent(this,SettingsActivity.class);
                myIntentSettings.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntentSettings);

                return true;
            case R.id.about_the_developer:
                Intent myIntentAbout;
                myIntentAbout= new Intent(this,ActivityAbout.class);
                startActivity(myIntentAbout);
                //here is changing in the float icon aka settings
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }



    public void nextActivity(View view) {


        Intent myIntent;
        myIntent= new Intent(this,SaveLocationActivity.class);
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
            finish();
        }



    }
}
