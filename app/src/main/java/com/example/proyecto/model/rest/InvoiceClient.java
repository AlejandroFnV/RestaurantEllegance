package com.example.proyecto.model.rest;

import com.example.proyecto.model.data.Invoice;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface InvoiceClient
{
    @POST("invoice")
    Call<Long> post(@Body Invoice invoice);

    @GET("invoice/actives")
    Call<ArrayList<Invoice>> getActiveInvoices();

    @GET("invoice/inactives")
    Call<ArrayList<Invoice>> getInactiveInvoices();

    @PUT("invoice/{id}")
    Call<Boolean> update(@Path("id") Long id, @Body Invoice invoice);

    @GET("invoice/table_in_use/{table}")
    Call<Boolean> isTableInUse(@Path("table") Long table);

    @GET("invoice/id/{id}")
    Call<Invoice> getByIdInvoice(@Path("id") long idInvoice);
}
