package com.example.proyecto.model.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.proyecto.model.data.Employee;
import com.example.proyecto.model.Utils;
import com.example.proyecto.model.rest.EmployeeClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployeeRepository
{
    private EmployeeClient employeeClient;

    private MutableLiveData<Employee> getLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> updateLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> upload_imageLiveData = new MutableLiveData<>();

    public EmployeeRepository()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.getServerUrl() + "api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        employeeClient = retrofit.create(EmployeeClient.class);
    }

    public void get(Long id)
    {
        Call<Employee> call = employeeClient.get(id);
        call.enqueue(new Callback<Employee>()
        {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response)
            {
                getLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t)
            {
                getLiveData.setValue(null);
            }
        });
    }

    public void get(String name)
    {
        Call<Employee> call = employeeClient.get(name);
        call.enqueue(new Callback<Employee>()
        {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response)
            {
                getLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t)
            {
                getLiveData.setValue(null);
            }
        });
    }

    public void update(Long id, Employee employee)
    {
        Call<Boolean> call = employeeClient.update(id, employee);
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

    public void upload_image(Long id, File image)
    {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), image);
        MultipartBody.Part request = MultipartBody.Part.createFormData("file", id + ".jpg", requestBody);

        Call<Boolean> call = employeeClient.upload_image(request);
        call.enqueue(new Callback<Boolean>()
        {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response)
            {
                upload_imageLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                upload_imageLiveData.setValue(null);
            }
        });
    }

    public LiveData<Employee> getGetLiveData()
    {
        return getLiveData;
    }

    public LiveData<Boolean> getUpdateLiveData()
    {
        return updateLiveData;
    }

    public LiveData<Boolean> getUpload_imageLiveData()
    {
        return upload_imageLiveData;
    }
}
