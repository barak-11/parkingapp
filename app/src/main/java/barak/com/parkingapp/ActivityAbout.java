package barak.com.parkingapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import java.net.URI;


public class ActivityAbout extends ActionBarActivity {



    VideoView v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_about);

        v =(VideoView) findViewById(R.id.videoViewAbout);
        Uri myUri= Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.awesome);
        v.setVideoURI(myUri);
        v.setMediaController(new MediaController(this));
        v.requestFocus();
        v.start();
        // v.setVideoPath("https://www.youtube.com/watch?v=9YLMrbmkUOs");
       // v.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.homeActivity:
                Intent myIntent;
                myIntent= new Intent(this,HomeActivity.class);
                startActivity(myIntent);
                return true;


            case R.id.lastparkingplace:
                Intent myIntentt;
                myIntentt= new Intent(this,SaveLocationActivity.class);
                startActivity(myIntentt);
                return true;
            case R.id.showParkingLots:
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=parking+lots");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
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




            default:
        }



        switch(id) {
            case R.id.action_settings:
                Intent myIntentSettings;
                myIntentSettings= new Intent(this,SettingsActivity.class);
                startActivity(myIntentSettings);
                //here is changing in the float icon aka settings
                return true;
            case R.id.about_the_developer:
                Intent myIntentAbout;
                myIntentAbout= new Intent(this,ActivityAbout.class);
                startActivity(myIntentAbout);
                //here is changing in the float icon aka settings
                return true;
            default:
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openLinkedin(View view) {

        Intent myIntentLinkedin;
        myIntentLinkedin= new Intent(this,LinkedinActivity.class);
        startActivity(myIntentLinkedin);
    }

    public void openVideo(View view) {

        Button but = (Button) findViewById(R.id.buttonReturn);
        Button butWatchmyVideo = (Button) findViewById(R.id.buttonWatchMyVideo);
        LinearLayout linear = (LinearLayout) findViewById(R.id.linear);
        VideoView vid = (VideoView) findViewById(R.id.videoViewAbout);
        linear.setVisibility(view.GONE);
        vid.setVisibility(view.VISIBLE);
       but.setVisibility(view.VISIBLE);
        butWatchmyVideo.setVisibility(view.GONE);
        //linear.setBackgroundColor(-256);
    }

    public void oshowCintactMe(View view) {

        Button but = (Button) findViewById(R.id.buttonReturn);
        Button butWatchmyVideo = (Button) findViewById(R.id.buttonWatchMyVideo);
        LinearLayout linear = (LinearLayout) findViewById(R.id.linear);
        VideoView vid = (VideoView) findViewById(R.id.videoViewAbout);
        linear.setVisibility(view.VISIBLE);
        vid.setVisibility(view.GONE);
        but.setVisibility(view.GONE);
        butWatchmyVideo.setVisibility(view.VISIBLE);
    }


    public void openEmail(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.putExtra(Intent.EXTRA_EMAIL,new String("barakse11@gmail.com"));
        emailIntent.setType("text/plain");
        startActivity(emailIntent);
    }
}
