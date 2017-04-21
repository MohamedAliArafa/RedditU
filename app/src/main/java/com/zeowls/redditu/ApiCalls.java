package com.zeowls.redditu;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by root on 4/15/17.
 */
class ApiCalls {

    // Trailing slash is needed
    private static final String BASE_URL = "https://www.reddit.com";
    private static Retrofit retrofit = null;


    static Retrofit getClient() {
        retrofit = null;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    static Retrofit getDetailClient() {
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(DetailResponse.class, new DetailResponse.RepliesDeserializer())
//                .create();

        retrofit = null;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        return retrofit;
    }

//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
//

}
