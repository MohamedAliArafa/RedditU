package com.zeowls.redditu;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by root on 4/15/17.
 */

interface RedditApiEndpointInterface {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("r/popular/.json")
    Call<MainResponse> getPopular();

    @GET("r/{url}/.json")
    Call<List<DetailResponse>> getDetails(@Path("url") String url);
}
