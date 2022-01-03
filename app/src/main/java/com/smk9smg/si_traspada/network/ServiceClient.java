package com.smk9smg.si_traspada.network;

import com.smk9smg.si_traspada.login.ResponseLogin;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceClient {
    //ini untuk melakukan login
    @POST("exec")
    Call<ResponseLogin> login(@Query("action")String action,
                              @Query("nis")String nis,
                              @Query("password")String password);
}
