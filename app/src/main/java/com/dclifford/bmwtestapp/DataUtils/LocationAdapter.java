package com.dclifford.bmwtestapp.DataUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dclifford.bmwtestapp.MainActivityFragment;
import com.dclifford.bmwtestapp.R;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by DSClifford on 1/14/2016.
 */
public class LocationAdapter extends
        RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    static List<Location> mLocations;
    static Location location;
    static Context ctx;
    Fragment homeFragment;

    public LocationAdapter(List<Location> locations, MainActivityFragment fragment) {
        mLocations = locations;
        homeFragment = fragment;
    }

    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);

        // Inflate the custom layout
        View locationView = inflater.inflate(R.layout.item_location, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(locationView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LocationAdapter.ViewHolder viewHolder, int position) {

        location = mLocations.get(position);
//Calculating distance//////////////////////////////////////////////////////////////////////////////
        double distance = 0.0;
        android.location.Location placeLocation = new android.location.Location("point A");
        placeLocation.setLatitude(location.getLatitude());
        placeLocation.setLongitude(location.getLongitude());
        android.location.Location currentLocation = new android.location.Location("point B");
        try {
            LocationManager lm = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
            android.location.Location gpsLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            currentLocation.setLongitude(gpsLocation.getLongitude());
            currentLocation.setLatitude(gpsLocation.getLatitude());
            distance = currentLocation.distanceTo(placeLocation);
        }catch (Exception e){
        }
        Log.e("Distance", String.valueOf(distance * 0.000621371));
        location.setDistance(distance * 0.000621371);
////////////////////////////////////////////////////////////////////////////////////////////////////
//Extracting hour/min from arrival time, for display////////////////////////////////////////////
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        Date parsedDate = new Date();
        try {
            parsedDate = dateFormat.parse(location.getArrivalTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
        String stringTime = time.format(parsedDate).toString();
////////////////////////////////////////////////////////////////////////////////////////////////////
        // Set item views based on the data model
        TextView nameTV = viewHolder.nameTextView;
            nameTV.setText(location.getName());
        TextView addressTV = viewHolder.addressTextView;
            addressTV.setText(location.getAddress());

        TextView distanceTV = viewHolder.distanceView;
        String distanceText = new String(String.format("%.2f", distance * 0.000621371));
        final SpannableStringBuilder distanceString = new SpannableStringBuilder(distanceText+" miles away");
        // Span to make text bold
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        distanceString.setSpan(bss, 0, distanceText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        distanceTV.setText(distanceString);

        TextView arrivalTV = viewHolder.arrivalTView;
        final SpannableStringBuilder arrivaleString = new SpannableStringBuilder("Est. Arrival Time: "+stringTime);
        // Span to make text bold
        arrivaleString.setSpan(bss, arrivaleString.length() - stringTime.length(), arrivaleString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            arrivalTV.setText(arrivaleString);

        if (location.getBitmap() != null) {
            viewHolder.imageView.setImageBitmap(location.getBitmap());
        }else{
            LocationAndView container = new LocationAndView();
            container.location = location;
            container.viewHolder = viewHolder;

            ImageLoader loader = new ImageLoader();
            loader.execute(container);

        }

        viewHolder.cardView.setContentDescription(location.getName().toString());

    }

    class LocationAndView {
        public Location location;
        public ViewHolder viewHolder;
        public Bitmap bitmap;

    }

    public class ImageLoader extends AsyncTask<LocationAndView, Void, LocationAndView> {

        @Override
        protected LocationAndView doInBackground(LocationAndView... params) {

            LocationAndView  container = params[0];
            Location location = container.location;

            try{
                String imageUrl = location.getImageURL(location.getName());
                Log.e("ID", location.getName());
                Log.e("ImageURL", imageUrl);
                InputStream in = (InputStream) new URL(imageUrl).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                location.setBitmap(bitmap);
                in.close();
                container.bitmap = bitmap;
                return container;
            }catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(LocationAndView result){

            ImageView image = result.viewHolder.imageView;
            image.setImageBitmap(result.bitmap);
            result.location.setBitmap(result.bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return mLocations.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView addressTextView;
        public ImageView imageView;
        public TextView distanceView;
        public TextView arrivalTView;
        public ImageView mapBtn;
        public ImageView dirBtn;
        public CardView cardView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTV);
            addressTextView = (TextView) itemView.findViewById(R.id.addressTV);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            distanceView = (TextView) itemView.findViewById(R.id.distanceTV);
            arrivalTView = (TextView) itemView.findViewById(R.id.arrivalTV);
            mapBtn = (ImageView) itemView.findViewById(R.id.mapBtnImV);
            dirBtn = (ImageView) itemView.findViewById(R.id.dirBtnImV);

            mapBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Location location = mLocations.get(getAdapterPosition());
                    Uri gmmIntentUri = Uri.parse("geo:" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude())
                            + "?q=" + location.getName());
                    Intent directionIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    directionIntent.setPackage("com.google.android.apps.maps");
                    if (directionIntent.resolveActivity(ctx.getPackageManager()) != null) {
                        ctx.startActivity(directionIntent);
                    }
                }
            });

            dirBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Location location = mLocations.get(getAdapterPosition());
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + location.getAddress());
                    Intent directionIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    directionIntent.setPackage("com.google.android.apps.maps");
                    if (directionIntent.resolveActivity(ctx.getPackageManager()) != null) {
                        ctx.startActivity(directionIntent);
                    }
                }
            });

        }

    }
}