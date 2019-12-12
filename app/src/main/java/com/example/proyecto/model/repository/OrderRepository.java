package com.example.proyecto.model.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.proyecto.model.Utils;
import com.example.proyecto.model.data.Order;
import com.example.proyecto.model.rest.OrderClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderRepository
{
    private OrderClient orderClient;

    private MutableLiveData<Long> postLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Order>> getByInvoiceLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Order>> getUndeliveredLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> updateLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> destroyLiveData = new MutableLiveData<>();

    public OrderRepository()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.getServerUrl() + "api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        orderClient = retrofit.create(OrderClient.class);
    }

    public void post(Order order)
    {
        Call<Long> call = orderClient.post(order);
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

    public void getByInvoice(Long id)
    {
        Call<ArrayList<Order>> call = orderClient.getByInvoice(id);
        call.enqueue(new Callback<ArrayList<Order>>()
        {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response)
            {
                getByInvoiceLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t)
            {
                getByInvoiceLiveData.setValue(null);
            }
        });
    }

    public void getUndelivered()
    {
        Call<ArrayList<Order>> call = orderClient.getUndelivered();
        call.enqueue(new Callback<ArrayList<Order>>()
        {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response)
            {
                getUndeliveredLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t)
            {
                getUndeliveredLiveData.setValue(null);
            }
        });
    }

    public void update(Long id, Order order)
    {
        Call<Boolean> call = orderClient.update(id, order);
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

    public void destroy(Long id)
    {
        Call<Boolean> call = orderClient.destroy(id);
        call.enqueue(new Callback<Boolean>()
        {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response)
            {
                destroyLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                destroyLiveData.setValue(null);
            }
        });
    }

    public MutableLiveData<Long> getPostLiveData()
    {
        return postLiveData;
    }

    public MutableLiveData<ArrayList<Order>> getGetByInvoiceLiveData()
    {
        return getByInvoiceLiveData;
    }

    public MutableLiveData<ArrayList<Order>> getGetUndeliveredLiveData()
    {
        return getUndeliveredLiveData;
    }

    public MutableLiveData<Boolean> getUpdateLiveData()
    {
        return updateLiveData;
    }

    public MutableLiveData<Boolean> getDestroyLiveData()
    {
        return destroyLiveData;
    }
}
