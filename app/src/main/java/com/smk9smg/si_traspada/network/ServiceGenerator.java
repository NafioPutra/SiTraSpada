package com.smk9smg.si_traspada.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    //jangan lupa ini diganti
    private static final String BASE_URL = "https://script.google.com/macros/s/AKfycbwiJvEya8pEJw3Mdw0ro3q69PTURRlVLp3mHr7oq7AEtrJJcIA/";

    private static Gson gson = new GsonBuilder().setLenient().create();
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory
                    (GsonConverterFactory.create(gson))
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
