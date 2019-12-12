package com.example.proyecto.model.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.proyecto.model.Utils;
import com.example.proyecto.model.data.Invoice;
import com.example.proyecto.model.rest.InvoiceClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvoiceRepository
{
    private InvoiceClient invoiceClient;

    private MutableLiveData<Long> postLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Invoice>> getActiveInvoicesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Invoice>> getInactiveInvoicesLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> updateLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isTableInUseLiveData = new MutableLiveData<>();
    private MutableLiveData<Invoice> getByIdInvoiceLiveData = new MutableLiveData<>();

    public InvoiceRepository()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.getServerUrl() + "api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        invoiceClient = retrofit.create(InvoiceClient.class);
    }

    public void post(Invoice invoice)
    {
        Call<Long> call = invoiceClient.post(invoice);
        call.enqueue(new Callback<Long>()
        {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response)
            {
                postLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t)
            {
                postLiveData.setValue(null);
            }
        });
    }

    public void getActiveInvoices()
    {
        Call<ArrayList<Invoice>> call = invoiceClient.getActiveInvoices();
        call.enqueue(new Callback<ArrayList<Invoice>>()
        {
            @Override
            public void onResponse(Call<ArrayList<Invoice>> call, Response<ArrayList<Invoice>> response)
            {
                getActiveInvoicesLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Invoice>> call, Throwable t)
            {
                getActiveInvoicesLiveData.setValue(null);
            }
        });
    }

    public void getInactiveInvoices()
    {
        Call<ArrayList<Invoice>> call = invoiceClient.getInactiveInvoices();
        call.enqueue(new Callback<ArrayList<Invoice>>()
        {
            @Override
            public void onResponse(Call<ArrayList<Invoice>> call, Response<ArrayList<Invoice>> response)
            {
                getInactiveInvoicesLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Invoice>> call, Throwable t)
            {
                getInactiveInvoicesLiveData.setValue(null);
            }
        });
    }

    public void update(Long id, Invoice invoice)
    {
        Call<Boolean> call = invoiceClient.update(id, invoice);
        call.enqueue(new Callback<Boolean>()
        {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response)
            {
                updateLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                updateLiveData.setValue(null);
            }
        });
    }

    public void isTableInUse(Long table)
    {
        Call<Boolean> call = invoiceClient.isTableInUse(table);
        call.enqueue(new Callback<Boolean>()
        {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response)
            {
                isTableInUseLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                isTableInUseLiveData.setValue(null);
            }
        });
    }

    public void getByIdInvoice(long idInvoice){
        Call<Invoice> call = invoiceClient.getByIdInvoice(idInvoice);
        call.enqueue(new Callback<Invoice>()
        {
            @Override
            public void onResponse(Call<Invoice> call, Response<Invoice> response)
            {
                getByIdInvoiceLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Invoice> call, Throwable t)
            {
                getByIdInvoiceLiveData.setValue(null);
            }
        });
    }

    public MutableLiveData<Invoice> getInvoiceById(){
        return getByIdInvoiceLiveData;
    }

    public MutableLiveData<Long> getPostLiveData()
    {
        return postLiveData;
    }

    public MutableLiveData<List<Invoice>> getGetActiveInvoicesLiveData()
    {
        return getActiveInvoicesLiveData;
    }

    public MutableLiveData<List<Invoice>> getGetInactiveInvoicesLiveData()
    {
        return getInactiveInvoicesLiveData;
    }

    public MutableLiveData<Boolean> getUpdateLiveData()
    {
        return updateLiveData;
    }

    public MutableLiveData<Boolean> getIsTableInUseLiveData()
    {
        return isTableInUseLiveData;
    }
}
