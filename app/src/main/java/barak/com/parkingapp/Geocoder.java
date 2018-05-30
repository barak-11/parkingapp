package barak.com.parkingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Geocoder extends AppCompatActivity {

    SharedPreferences myDBfile;
    SharedPreferences.Editor myEditor;

    SharedPreferences myDBfileNew;
    SharedPreferences.Editor myEditorNew;

    public List<Address> addresses;
    public android.location.Geocoder geocoder;
    public static List<String> mylist;
    Latlng latlng;
    public static Latlng latlngNe;
    public ListView listView;

    public boolean remove[];


    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocoder);
        Toast.makeText(this, "Loading... ", Toast.LENGTH_SHORT).show();
        remove=new boolean[40];

        try{
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.activity_list_item, android.R.id.text1, mylist);
                    listView.setAdapter(adapter);
                }
            };
        }catch (Exception e){
            Toast.makeText(this, "handler: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }



        listView = (ListView) findViewById(R.id.listViewGeo);

        myDBfile = getSharedPreferences("file2", MODE_PRIVATE);
        myEditor = myDBfile.edit();

        myDBfileNew = getSharedPreferences("file1", MODE_PRIVATE);
        myEditorNew = myDBfileNew.edit();

        try{
            String testing = myDBfile.getString("k", Integer.toString(-1));
            final int listSize = Integer.parseInt(testing);

            if (listSize == -1) {
                Log.d("listSize", Integer.toString(listSize));
                if (mylist == null) {
                    mylist = new ArrayList<String>();
                }
                else {
                    mylist.clear();
                }
            }
            mylist = new ArrayList<String>();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    String test = "";
                    for (int i = 0; i < listSize; i++) {

                        for (int j=0; j<remove.length; j++)
                            if (remove[i]) {

                                continue;
                            }
                        String res = myDBfile.getString("test[" + i + "]", "Defualt");
                        if (res.equals("Default")) {
                            test="No entries yet";
                            mylist.add(test);
                            break;
                        }
                        Log.d("HistoryStrings",res);
                        latlng=createLatLng(res);

                        test = getAddress(latlng.latitude, latlng.longitude,1);
                        mylist.add(test);
                    }
                    handler.sendEmptyMessage(0);
                }

            };

            Thread testThread = new Thread(r);
            testThread.start();
        }catch (Exception e){
            Toast.makeText(this, "Retrieving data error: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }





            // Assign adapter to ListView
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    // ListView Clicked item value
                    String itemValue = (String) listView.getItemAtPosition(position);

                    // Show Alert
                    Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_LONG).show();
                }

            });


        registerForContextMenu(listView);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listViewGeo) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(mylist.get(info.position));
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            int menuItemIndex = item.getItemId();
            String[] menuItems = getResources().getStringArray(R.array.menu);
            String menuItemName = menuItems[menuItemIndex];
            String listItemName = mylist.get(info.position);
            String temp = myDBfile.getString("test[" + info.position + "]", "Defualt");
            latlngNe=createLatLng(temp);

        if (menuItemName.equals("Set as Your Car's Location")){



            myEditorNew.putString("longitude",Double.toString(latlngNe.longitude));
            myEditorNew.putString("latitude", Double.toString(latlngNe.latitude));
            myEditorNew.putString("saved", "true");

            Toast.makeText(this, "Your car's location is now set to " + listItemName, Toast.LENGTH_SHORT).show();
            Intent myIntent;
            myIntent = new Intent(this, HomeActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);
        }

        else if(menuItemName.equals("Street View")) {
            myEditorNew.putBoolean("flagFromGeocoder", true);
            Intent street;
            street = new Intent(this, StreetActivity.class);
            startActivity(street);

        }
        else if(menuItemName.equals("Delete")) {

            Log.d("menuItemIndex0",Integer.toString(info.position));
            remove[info.position] = new Boolean(true);
            Toast.makeText(this, "Deleted ", Toast.LENGTH_SHORT).show();

            Intent refresh;
            refresh = new Intent(this, Geocoder.class);
            startActivity(refresh);
        }
        else if(menuItemName.equals("Navigate to")) {
            Uri gmmIntentUri;
            Intent mapIntent;

            gmmIntentUri = Uri.parse("google.navigation:q=" + latlngNe.latitude + "," + latlngNe.longitude + "&mode=w");
            mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        }




        myEditorNew.commit();
        return true;
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_geocoder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class Answer {
        String answer1;
        String answer2;
        String answer3;
    }

    private String getAddress(double latitude, double longitude, int numOfAnswers) {
        StringBuilder result = new StringBuilder();
        try {
            geocoder = new android.location.Geocoder(this, Locale.US);
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, numOfAnswers);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append("\n");
                result.append(address.getAddressLine(0));
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        if (numOfAnswers>1)
        {
            Answer answer= new Answer();
            answer.answer1=addresses.get(0).toString();
            answer.answer2=addresses.get(1).toString();
            answer.answer3=addresses.get(2).toString();
        }

        return result.toString();
    }

    class Latlng {
        double longitude;
        double latitude;
    }

    public Latlng createLatLng(String s) {
        Latlng latlng = new Latlng();
        char[] charArray = s.toCharArray();
        char[] charOutputLat = new char[s.length()];
        char[] charOutputLon = new char[s.length()];
        boolean latitude = true;
        boolean longitude = false;
        int i = 0;
        int g = 0;
        int h = 0;
        String d;
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

        d = new String(charOutputLat);
        latlng.latitude = Double.parseDouble(d);
        d = new String(charOutputLon);
        latlng.longitude = Double.parseDouble(d);
        return latlng;
    }
}
