package com.firozanwar.sample.restaurantapp.services;

import com.firozanwar.sample.restaurantapp.models.DataItem;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.firozanwar.sample.restaurantapp.services.MyWebService.FEED;

/**
 * Created by firozanwar on 12/10/17.
 */

public interface MyWebService {

    String BASE_URL = "http://560057.youcanlearnit.net/";
    String FEED = "services/json/itemsfeed.php";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET(FEED)
    Call<DataItem[]> dataitems();

    @GET(FEED)
    Call<DataItem[]> dataitems(@Query("category") String category);
}
