package com.example.proyecto.model.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proyecto.model.data.Employee;
import com.example.proyecto.model.repository.EmployeeRepository;

public class LoginActivityVM extends AndroidViewModel
{
    //View model control
    private boolean started;
    private boolean touchable;

    //Repositories
    private EmployeeRepository employeeRepository;

    //Activity attrs
    private boolean loading;
    private boolean tilUserError_fix;
    private String tilUserError;
    private String tietUser;
    private boolean tilPasswordError_fix;
    private String tilPasswordError;
    private String tietPassword;
    private boolean cbRememberUser;

    public LoginActivityVM(@NonNull Application application)
    {
        super(application);
        employeeRepository = new EmployeeRepository();
    }

    public boolean isStarted()
    {
        return started;
    }

    public void setStarted(boolean started)
    {
        this.started = started;
    }

    public boolean isTouchable()
    {
        return touchable;
    }

    public void setTouchable(boolean touchable)
    {
        this.touchable = touchable;
    }

    public boolean isLoading()
    {
        return loading;
    }

    public void setLoading(boolean loading)
    {
        this.loading = loading;
    }

    public boolean isTilUserError_fix()
    {
        return tilUserError_fix;
    }

    public void setTilUserError_fix(boolean tilUserError_fix)
    {
        this.tilUserError_fix = tilUserError_fix;
    }

    public String getTilUserError()
    {
        return tilUserError;
    }

    public void setTilUserError(String tilUserError)
    {
        this.tilUserError = tilUserError;
    }

    public String getTietUser()
    {
        return tietUser;
    }

    public void setTietUser(String tietUser)
    {
        this.tietUser = tietUser;
    }

    public boolean isTilPasswordError_fix()
    {
        return tilPasswordError_fix;
    }

    public void setTilPasswordError_fix(boolean tilPasswordError_fix)
    {
        this.tilPasswordError_fix = tilPasswordError_fix;
    }

    public String getTilPasswordError()
    {
        return tilPasswordError;
    }

    public void setTilPasswordError(String tilPasswordError)
    {
        this.tilPasswordError = tilPasswordError;
    }

    public String getTietPassword()
    {
        return tietPassword;
    }

    public void setTietPassword(String tietPassword)
    {
        this.tietPassword = tietPassword;
    }

    public boolean isCbRememberUser()
    {
        return cbRememberUser;
    }

    public void setCbRememberUser(boolean cbRememberUser)
    {
        this.cbRememberUser = cbRememberUser;
    }

    public void get(String name)
    {
        employeeRepository.get(name);
    }

    public LiveData<Employee> getGetLiveData()
    {
        return employeeRepository.getGetLiveData();
    }
}
