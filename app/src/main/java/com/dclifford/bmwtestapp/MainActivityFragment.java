package com.dclifford.bmwtestapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.dclifford.bmwtestapp.DataUtils.Location;
import com.dclifford.bmwtestapp.DataUtils.LocationAPI;
import com.dclifford.bmwtestapp.DataUtils.LocationAdapter;
import com.dclifford.bmwtestapp.DataUtils.SimpleLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    RecyclerView locationRecView;
    List<Location> locationList;
    Context ctx;
    String sortParam;
    ProgressBar pb;

    public String QUERY_URL = "http://localsearch.azurewebsites.net/api/";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_main, container, false);
        pb = (ProgressBar) mainView.findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
        if (isOnline()) {

            Button mainMapBtn = (Button) mainView.findViewById(R.id.button);

            Spinner sortSpinner = (Spinner) mainView.findViewById(R.id.sortspinner);
            setUpSpinner(sortSpinner);
            sortParam = sortSpinner.getSelectedItem().toString();
            Log.e("Spinner", sortParam);

            locationRecView = (RecyclerView) mainView.findViewById(R.id.locationRecView);
            locationRecView.setClickable(true);
            locationRecView.setFocusable(true);

            getLocations();
            sortLocations();

            mainMapBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchMap();
                }
            });

        } else {
            Toast.makeText(ctx, "Network isn't available", Toast.LENGTH_LONG).show();
        }
        
        return mainView;
    }

    private void setUpSpinner(Spinner sortSpinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx,
                R.array.sorting_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                pb.setVisibility(View.VISIBLE);
                sortParam = parentView.getSelectedItem().toString();
                sortLocations();
                pb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void sortLocations() {
        Log.e("Spinner", sortParam);
        if(locationList!=null) {
            if (sortParam.contains("Name")) {
                Collections.sort(locationList, new NameComparator());
            }
            if (sortParam.contains("Time")) {
                Collections.sort(locationList, new ArivTimeComparator());
            }
            if (sortParam.contains("Distance")) {
                Collections.sort(locationList, new DistanceComparator());
            }
            if (locationList != null) {
                LocationAdapter adapter = new LocationAdapter(locationList, this);
                locationRecView.setAdapter(adapter);
            }
        }
    }

    private void getLocations() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(QUERY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LocationAPI api = retrofit.create(LocationAPI.class);
        Call<List<Location>> call = api.getFeed();
        call.enqueue(new Callback<List<Location>>() {

            @Override
            public void onResponse(Response<List<Location>> response, Retrofit retrofit) {
                locationList = response.body();
                locationRecView.setLayoutManager(new LinearLayoutManager(ctx));
                LocationAdapter adapter = new LocationAdapter(locationList, MainActivityFragment.this);
                locationRecView.setAdapter(adapter);

                pb.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public class NameComparator implements Comparator<Location> {
        @Override
        public int compare(Location l1, Location l2) {
            return l1.getName().compareTo(l2.getName());
        }
    }
    public class ArivTimeComparator implements Comparator<Location> {
        @Override
        public int compare(Location l1, Location l2) {
            return l1.getArrivalTime().compareTo(l2.getArrivalTime());
        }
    }
    public class DistanceComparator implements Comparator<Location> {
        @Override
        public int compare(Location l1, Location l2) {
            return String.valueOf(l1.getDistance()).compareTo(String.valueOf(l2.getDistance()));
        }
    }

    public void launchMap() {
        ArrayList<SimpleLocation> simpleLocations = new ArrayList<>();
        for (Location location: locationList
        ){
            SimpleLocation simpleLocation = new SimpleLocation(location.getID(),location.getName(),location.getLatitude(),
            location.getLongitude(),location.getAddress(),location.getArrivalTime());
            simpleLocations.add(simpleLocation);

        }

        Intent intent = new Intent(ctx, MapsActivity.class);
        intent.putParcelableArrayListExtra("locations", simpleLocations);
        ctx.startActivity(intent);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ctx = context;

    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
