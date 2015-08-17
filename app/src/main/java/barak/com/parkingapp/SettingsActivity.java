package barak.com.parkingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsActivity extends ActionBarActivity {


    SharedPreferences myDBfile; // create a file or return a reference to an exist file
    SharedPreferences.Editor myEditor;

    TextView t;
    RadioGroup rg;
    RadioButton rbAutomatic;
    RadioButton rbManual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        t=(TextView)findViewById(R.id.AutomatedManuelTextView);
        rg=(RadioGroup) findViewById(R.id.RadioGroupID);
        rbAutomatic=(RadioButton)findViewById(R.id.radioButtonAutomatic);
        rbManual=(RadioButton)findViewById(R.id.radioButtonManual);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId==R.id.radioButtonAutomatic) {
                    t.setText("Automated");
                }
                else
                {
                    t.setText("Manual");
                }
            }
        });

        myDBfile =getSharedPreferences("file1",MODE_PRIVATE);
        myEditor=myDBfile.edit();

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


        myEditor.putString("Name", ((EditText) findViewById(R.id.editTextEnterName)).getText().toString());
        myEditor.putString("CarNumber", ((EditText) findViewById(R.id.editTextEnterCarNumber)).getText().toString());
        myEditor.putString("CarType", ((EditText) findViewById(R.id.editTextCarType)).getText().toString());
        myEditor.commit();

        LayoutInflater myInflater=getLayoutInflater();
        View newToastView;
        newToastView=myInflater.inflate(R.layout.toastlayout,null);
        TextView tempTextView=(TextView)newToastView.findViewById(R.id.toastTexView);
        tempTextView.setText("Saved");

        Toast myMessage= new Toast(this);
        myMessage.setGravity(Gravity.BOTTOM, 0, 50);
        myMessage.setView(newToastView);
        myMessage.setDuration(Toast.LENGTH_SHORT);
        myMessage.show();
    }

    public void back(View view) {

         Intent myIntent;
         myIntent= new Intent(this,HomeActivity.class);
        startActivity(myIntent);
    }
    @Override
    protected void onStop() {
        super.onStop(); // in case the app crashes - onStop will save the data

        myEditor.putString("Name", ((EditText) findViewById(R.id.editTextEnterName)).getText().toString());
        myEditor.putString("CarNumber", ((EditText) findViewById(R.id.editTextEnterCarNumber)).getText().toString());
        myEditor.putString("CarType", ((EditText) findViewById(R.id.editTextCarType)).getText().toString());
        myEditor.commit(); //"commit" saves the file

    }


}
