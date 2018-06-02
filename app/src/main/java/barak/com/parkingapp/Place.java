package barak.com.parkingapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Place{
    String address;
    String createdDate;
    String uid;
    public Place(String address, String createdDate, String uid){
        this.address=address;
        this.createdDate=createdDate;
        this.uid=uid;
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
