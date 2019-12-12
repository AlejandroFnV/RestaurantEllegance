package com.example.proyecto.model.rest;

import com.example.proyecto.model.data.Employee;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface EmployeeClient
{
    @GET("employee/{id}")
    Call<Employee> get(@Path("id") Long id);

    @GET("employee/name/{name}")
    Call<Employee> get(@Path("name") String name);

    @PUT("employee/{id}")
    Call<Boolean> update(@Path("id") Long id, @Body Employee employee);

    @Multipart
    @POST("employee/upload_image")
    Call<Boolean> upload_image(@Part MultipartBody.Part image);
}
