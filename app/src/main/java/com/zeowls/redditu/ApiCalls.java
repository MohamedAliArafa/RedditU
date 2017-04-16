package com.zeowls.redditu;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 4/15/17.
 */
class ApiCalls {

    // Trailing slash is needed
    static final String BASE_URL = "https://www.reddit.com";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

//    public static Retrofit getDetailClient() {
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(DetailResponse.class, new DetailResponse.RepliesDeserializer())
//                .create();
//
//        retrofit = null;
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        return retrofit;
//    }

//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
//

}
