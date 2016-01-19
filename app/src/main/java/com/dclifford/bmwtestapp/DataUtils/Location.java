package com.dclifford.bmwtestapp.DataUtils;

import android.graphics.Bitmap;

/**
 * Created by DSClifford on 1/14/2016.
 */
public class Location {

    private int ID;
    private String Name;
    private double Latitude;
    private double Longitude;
    private String Address;
    private String ArrivalTime;
    private Bitmap bitmap;
    private String imageURL;
    private double distance;

    public Location(int ID, String Name, double Latitude,
                    double Longitude, String Address, String ArrivalTime) {
        this.ID = ID;
        this.Name = Name;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Address = Address;
        this.ArrivalTime = ArrivalTime;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public String getAddress() {
        return Address;
    }

    public String getArrivalTime() {
        return ArrivalTime;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImageURL(String name) {

        /* Right now, just using URLs from Google places to demonstrate. However,
        if the JSON response included the Google Places ID of the location, then
        this could be done programmatically using the Google Places API
         */

        switch (name) {
            case "Doughnut Vault Canal":
                return "http://doughnutvault.com/wp-content/uploads/IMG_4993.jpg";

            case "Monk’s Pub":
                return "https://lh5.googleusercontent.com/proxy/Bk4VJfnugZnswu6sRnQVFnKTElmFePQsPuQEyZqWBRcz-DopQ-xZICWwvZPUZ12dS8NUsFYOpITrdPw0hYK14eeldSGLScOhAUP23qgK2s5BdQVayTmDRLrOOd4LEt-lE5s66FYq55WxaIZwIn4gm9GqtMb88uo=w240-h160";

            case "Cubby Bear":
                return "https://lh4.googleusercontent.com/proxy/CR4CdeEdnn0MU_H8e6wTb2wD9_L_XX7mvQVzOF6M4OPo6Q4AcSwRM1VX4lCq_Ssr-gL04OL0RzgbZbVr6b5Bufb3rav0hbDEOfRZvhjYgrblZw8YTXF8nD3bQoKrZ6_FJ1WefifWB9DQ955aV5JKtTDPgqAfodY=w213-h160";

            case "Murphy’s Bleachers":
                return "https://lh6.googleusercontent.com/proxy/O-xjN-cJbq-XQlyjvWr7te3aZ4L7_bK1XyJzHmlI0Nmv22RGyzc-QOwvct12mo0A3oX_IMrXSScH5JuOfSwdZtZArSCvvlU0wOxIJuNNNAQUeAgBXRl1I8_8GhRsdYylmymA-WEGc82U66F44YYRAFaQeKN686A=w243-h160";

            case "Wrigley Field":
                return "http://mlb.mlb.com/chc/images/ballpark/480x200_wrigley_field.jpg";

            case "Al’s Beef":
                return "http://www.alsbeef.com/uploads/1/5/9/4/15946036/1406907922.png";

            case "Bull & Bear":
                return "http://www.rantlifestyle.com/wp-content/uploads/2014/04/158.jpg";

            case "Frontera Grill":
                return "http://www.thedeliciouslife.com/wp-content/plugins/hot-linked-image-cacher/upload/farm4.static.flickr.com/3300/3216153318_7ab77505e3_o.jpg";

            case "Lou Malnati's Pizzeria":
                return "http://intelligenttravel.nationalgeographic.com/files/2012/07/lou-malnatis-night.jpg";
        }
        return "http://i.stack.imgur.com/LU782.png";
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

}