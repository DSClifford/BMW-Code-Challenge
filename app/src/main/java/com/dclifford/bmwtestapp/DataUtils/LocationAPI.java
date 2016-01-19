package com.dclifford.bmwtestapp.DataUtils;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by DSClifford on 1/14/2016.
 */
public interface LocationAPI {


    @GET("Locations")
    Call<List<Location>> getFeed();


}
