package com.dclifford.bmwtestapp.DataUtils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DSClifford on 1/16/2016.
 */
public class SimpleLocation implements Parcelable {

    private int ID;
    private String Name;
    private double Latitude;
    private double Longitude;
    private String Address;
    private String ArrivalTime;


    public SimpleLocation(int ID, String Name, double Latitude,
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



    protected SimpleLocation(Parcel in) {
        ID = in.readInt();
        Name = in.readString();
        Latitude = in.readDouble();
        Longitude = in.readDouble();
        Address = in.readString();
        ArrivalTime = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(Name);
        dest.writeDouble(Latitude);
        dest.writeDouble(Longitude);
        dest.writeString(Address);
        dest.writeString(ArrivalTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SimpleLocation> CREATOR = new Parcelable.Creator<SimpleLocation>() {
        @Override
        public SimpleLocation createFromParcel(Parcel in) {
            return new SimpleLocation(in);
        }

        @Override
        public SimpleLocation[] newArray(int size) {
            return new SimpleLocation[size];
        }
    };
}
