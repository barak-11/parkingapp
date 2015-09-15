package barak.com.parkingapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class History extends Activity {


    public static ListView listView;
    SharedPreferences myDBfile; // create a file or return a reference to an exist file
    SharedPreferences.Editor myEditor;
    String[] values;
    String[] valuesdebug;
    public static List<String> mylist;

    public int kgg = 0;
    double test = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        myDBfile = getSharedPreferences("file2", MODE_PRIVATE);
        myEditor = myDBfile.edit();


        String testing = myDBfile.getString("k", Integer.toString(-1));
        kgg = Integer.parseInt(testing);

        if (kgg != -1) {
            Log.d("kgg", Integer.toString(kgg));
            if (mylist == null) {
                mylist = new ArrayList<String>();
            }
            mylist.clear();
            for (int i = 0; i < kgg; i++) {
                //if (i>kgg)break;
                String res = myDBfile.getString("test[" + i + "]", "Defualt");
                //test = returnLatitudeOld(res);
                //Log.d("returnLat",Double.toString(test));
                mylist.add(res);
            }
        }

        listView = (ListView) findViewById(R.id.listViewNew);

        if (mylist == null) {
            mylist = new ArrayList<String>();
        }
        //editStringValues(mylist);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, mylist);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                Latlng latlng = returnLatitude(itemValue);

                Geocoder geocoder = new Geocoder();
                geocoder.geocoder = new android.location.Geocoder(getApplicationContext(), Locale.US);
                try {
                    geocoder.addresses = geocoder.geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
    }

    public void reset(View view) {

        mylist.clear();
        myEditor.clear();
        myEditor.commit();
        Intent clearIntent;
        clearIntent = new Intent(this, History.class);
        startActivity(clearIntent);
    }

    class Latlng {
        double longitude;
        double latitude;
    }

    public Latlng returnLatitude(String s) {
        Latlng latlng = new Latlng();
        char[] charArray = s.toCharArray();
        char[] charOutputLat = new char[s.length()];
        char[] charOutputLon = new char[s.length()];
        boolean latitude = true;
        boolean longitude = false;

        int i = 0;
        int g = 0;
        int h = 0;
        String d = new String("");

        boolean flag = false;
        char pointer = charArray[i];


        while (s.length() != i) {
            if (pointer >= '0' && pointer <= '9') {
                flag = true;

            }
            if (flag == false) {
                i++;
                pointer = charArray[i];
                continue;
            } else {


                if (pointer == ',') {
                    i++;
                    pointer = charArray[i];
                    charOutputLat[g] = '\0';
                    longitude = true;
                    latitude = false;


                }
                if (pointer == ')') {
                    break;

                }
                if (longitude) {

                    if (pointer != ',')
                        charOutputLon[h] = pointer;
                    h++;
                    i++;
                    if (s.length() == i) {
                        break;
                    }
                    pointer = charArray[i];

                }

                if (latitude) {
                    charOutputLat[g] = pointer;
                    g++;
                    i++;
                    if (s.length() == i) {
                        break;
                    }
                    pointer = charArray[i];
                }

            }

        }
        System.out.println("charArray : " + String.valueOf(charArray));
        System.out.println("charOutputLat:" + String.valueOf(charOutputLat));
        System.out.println("charOutputLon:" + String.valueOf(charOutputLon));

        d = new String(charOutputLat);
        latlng.latitude = Double.parseDouble(d);
        d = new String(charOutputLon);
        latlng.longitude = Double.parseDouble(d);
        return latlng;
    }


    public double returnLatitudeOld(String s) {
        char[] charArray = s.toCharArray();
        char[] charOutput = new char[s.length()];
        int i = 0;
        int g = 0;
        String d = "";
        boolean flag = false;
        char pointer = charArray[i];
        Log.d("charOutput", String.valueOf(charArray));

        while (pointer != '\0') {
            if (pointer == ',') {
                charOutput[i] = '\0';
                break;

            }
            Log.d("pointer", Character.toString(pointer));

            charOutput[i] = pointer;
            Log.d("valueOFChar", String.valueOf(charOutput));

            i++;
            pointer = charArray[i];
        }
        double res = 0;
        return res;
    }
}


