package barak.com.parkingapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Place{
    String address;
    String createdDate;
    String uid;
    String longitude;
    String latitude;

    public Place(String address, String createdDate, String uid, String longitude,String latitude){
        this.address=address;
        this.createdDate=createdDate;
        this.uid=uid;
        this.longitude=longitude;
        this.latitude=latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedDate() {
        return createdDate;
    }
   /* public Date getCreatedDateInDateFormat() {

        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        Date date = new Date();
        try {
            date = format.parse(createdDate);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    */

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
