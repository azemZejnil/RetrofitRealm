package com.example.azem.retrofitrealm;

import com.example.azem.retrofitrealm.model.Values;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitClient {

    @GET("/posts")
    public Call<Values> getPosts();


}
