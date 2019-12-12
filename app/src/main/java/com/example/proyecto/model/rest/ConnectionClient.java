package com.example.proyecto.model.rest;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ConnectionClient
{
    @GET("connection")
    Call<Boolean> isConnected();
}
