package com.zeowls.redditu;

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

    @GET("{url}.json")
    Call<String> getDetails(@Path(value = "url", encoded=true) String url);
}
