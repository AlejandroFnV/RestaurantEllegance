package com.example.proyecto.model.rest;

import com.example.proyecto.model.data.Order;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderClient
{
    @POST("order")
    Call<Long> post(@Body Order order);

    @GET("order/invoice/{id}")
    Call<ArrayList<Order>> getByInvoice(@Path("id") Long id);

    @GET("order/undelivered")
    Call<ArrayList<Order>> getUndelivered();

    @PUT("order/{id}")
    Call<Boolean> update(@Path("id") Long id, @Body Order order);

    @DELETE("order/{id}")
    Call<Boolean> destroy(@Path("id") Long id);
}
