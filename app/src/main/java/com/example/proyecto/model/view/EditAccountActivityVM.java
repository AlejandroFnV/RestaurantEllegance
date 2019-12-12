package com.example.proyecto.model.view;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proyecto.model.data.Employee;
import com.example.proyecto.model.repository.EmployeeRepository;

import java.io.File;

public class EditAccountActivityVM extends AndroidViewModel
{
    //View model control
    private boolean started;
    private boolean touchable;

    //Repository
    private EmployeeRepository employeeRepository;

    //Other
    private boolean imageChanged;

    //Activity attrs
    private Employee employee;
    private Uri ivUriAccount;
    private boolean loading;
    private boolean tilUserError_fix;
    private String tilUserError;
    private String tietUser;
    private boolean tilPasswordError_fix;
    private String tilPasswordError;
    private String tietPassword;

    public EditAccountActivityVM(@NonNull Application application)
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

    public boolean isImageChanged()
    {
        return imageChanged;
    }

    public void setImageChanged(boolean imageChanged)
    {
        this.imageChanged = imageChanged;
    }

    public Employee getEmployee()
    {
        return employee;
    }

    public void setEmployee(Employee employee)
    {
        this.employee = employee;
    }

    public Uri getIvUriAccount()
    {
        return ivUriAccount;
    }

    public void setIvUriAccount(Uri ivUriAccount)
    {
        this.ivUriAccount = ivUriAccount;
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

    public void update(Long id, Employee employee)
    {
        employeeRepository.update(id, employee);
    }

    public LiveData<Boolean> getUpdateLiveData()
    {
        return employeeRepository.getUpdateLiveData();
    }

    public void upload_image(Long id, File image)
    {
        employeeRepository.upload_image(id, image);
    }

    public LiveData<Boolean> getUpload_imageLiveData()
    {
        return employeeRepository.getUpload_imageLiveData();
    }
}
