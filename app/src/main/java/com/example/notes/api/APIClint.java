package com.example.notes.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClint {
    private static final String BASE_URL = "https://crdudbapp.000webhostapp.com/notesapi/";
    private static Retrofit retrofit;

    public static Retrofit getApiClint() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
