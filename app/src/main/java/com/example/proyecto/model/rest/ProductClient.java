package com.example.proyecto.model.rest;

import com.example.proyecto.model.data.Product;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductClient
{
    @GET("product")
    Call<ArrayList<Product>> index();

    @GET("product/{id}")
    Call<Product> show(@Path("id") Long id);
}
