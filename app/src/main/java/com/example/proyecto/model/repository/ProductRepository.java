package com.example.proyecto.model.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.proyecto.model.Utils;
import com.example.proyecto.model.data.Product;
import com.example.proyecto.model.rest.ProductClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductRepository
{
    private ProductClient productClient;

    private MutableLiveData<ArrayList<Product>> indexLiveData = new MutableLiveData<>();
    private MutableLiveData<Product> showLiveData = new MutableLiveData<>();

    public ProductRepository()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.getServerUrl() + "api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        productClient = retrofit.create(ProductClient.class);
    }

    public void index()
    {
        Call<ArrayList<Product>> call = productClient.index();
        call.enqueue(new Callback<ArrayList<Product>>()
        {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response)
            {
                indexLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t)
            {
                indexLiveData.setValue(null);
            }
        });
    }

    public void show(Long id)
    {
        Call<Product> call = productClient.show(id);
        call.enqueue(new Callback<Product>()
        {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response)
            {
                showLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t)
            {
                showLiveData.setValue(null);
            }
        });
    }

    public MutableLiveData<ArrayList<Product>> getIndexLiveData()
    {
        return indexLiveData;
    }

    public MutableLiveData<Product> getShowLiveData()
    {
        return showLiveData;
    }
}
