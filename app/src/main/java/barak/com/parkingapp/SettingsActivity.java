package barak.com.parkingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    SharedPreferences myDBfile; // create a file or return a reference to an exist file
    SharedPreferences.Editor myEditor;

    TextView t;
    RadioGroup rg;
    RadioButton rbAutomatic;
    RadioButton rbManual;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        t = (TextView) findViewById(R.id.AutomatedManuelTextView);
        rg = (RadioGroup) findViewById(R.id.RadioGroupID);
        rbAutomatic = (RadioButton) findViewById(R.id.radioButtonAutomatic);
        rbManual = (RadioButton) findViewById(R.id.radioButtonManual);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioButtonAutomatic) {
                    t.setText("Automated");
                } else {
                    t.setText("Manual");
                }
            }
        });

        myDBfile = getSharedPreferences("file1", MODE_PRIVATE);
        myEditor = myDBfile.edit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
            case R.id.lastparkingplace:
                Intent myIntentt;
                myIntentt = new Intent(this, SaveLocationActivity.class);
                startActivity(myIntentt);
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

                return super.onOptionsItemSelected(item);
        }
    }

    public void save(View view) {


        myEditor.putString("Name", ((EditText) findViewById(R.id.editTextEnterName)).getText().toString());
        myEditor.putString("CarNumber", ((EditText) findViewById(R.id.editTextEnterCarNumber)).getText().toString());
        myEditor.putString("CarType", ((EditText) findViewById(R.id.editTextCarType)).getText().toString());
        myEditor.commit();

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        Intent myIntent;
        myIntent = new Intent(this, HomeActivity.class);
        startActivity(myIntent);
    }

    public void back(View view) {

        Intent myIntent;
        myIntent = new Intent(this, HomeActivity.class);
        startActivity(myIntent);
    }
    /*
    @Override
    protected void onStop() {
        super.onStop(); // in case the app crashes - onStop will save the data

        myEditor.putString("Name", ((EditText) findViewById(R.id.editTextEnterName)).getText().toString());
        myEditor.putString("CarNumber", ((EditText) findViewById(R.id.editTextEnterCarNumber)).getText().toString());
        myEditor.putString("CarType", ((EditText) findViewById(R.id.editTextCarType)).getText().toString());
        myEditor.commit(); //"commit" saves the file

    }
    */


    public void onRefresh() {
        Toast.makeText(this, "Clear Cache", Toast.LENGTH_SHORT).show();

        mSwipeRefreshLayout.setRefreshing(true);
        myEditor.putString("longitude", "34.77539057");
        myEditor.putString("latitude", "32.07481721");
        myEditor.putString("saved", "false");
        myEditor.putString("Name", "Yossi Cohen"); // asking for KEY names x (was created in save method) or a default file (in case x doesn't exist) named "haha"
        myEditor.putString("CarType", "Honda");
        myEditor.putString("CarNumber", "11-222-33");
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
