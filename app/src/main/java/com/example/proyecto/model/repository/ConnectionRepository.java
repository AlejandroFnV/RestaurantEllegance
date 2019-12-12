package com.example.proyecto.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.proyecto.model.Utils;
import com.example.proyecto.model.rest.ConnectionClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionRepository
{
    private ConnectionClient connectionClient;
    private MutableLiveData<Boolean> isConnectedLiveData = new MutableLiveData<>();

    public ConnectionRepository()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.getServerUrl() + "api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        connectionClient = retrofit.create(ConnectionClient.class);
    }

    public void isConnected()
    {
        Call<Boolean> call = connectionClient.isConnected();
        call.enqueue(new Callback<Boolean>()
        {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response)
            {
                isConnectedLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                isConnectedLiveData.setValue(null);
            }
        });
    }

    public MutableLiveData<Boolean> getIsConnectedLiveData()
    {
        return isConnectedLiveData;
    }
}
