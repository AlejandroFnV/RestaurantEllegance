package com.example.proyecto.model.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proyecto.model.repository.ConnectionRepository;

public class ConnectionActivityVM extends AndroidViewModel
{
    //View model control
    private boolean started;
    private boolean touchable;

    //Repository
    private ConnectionRepository connectionRepository;

    //Activity attrs
    private boolean loading;
    private boolean tilServerError_fix;
    private String tilServerError;
    private String tietServer;
    private boolean cbRememberConnection;

    public ConnectionActivityVM(@NonNull Application application)
    {
        super(application);
        connectionRepository = new ConnectionRepository();
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

    public boolean isTilServerError_fix()
    {
        return tilServerError_fix;
    }

    public void setTilServerError_fix(boolean tilServerError_fix)
    {
        this.tilServerError_fix = tilServerError_fix;
    }

    public String getTilServerError()
    {
        return tilServerError;
    }

    public void setTilServerError(String tilServerError)
    {
        this.tilServerError = tilServerError;
    }

    public String getTietServer()
    {
        return tietServer;
    }

    public void setTietServer(String tietServer)
    {
        this.tietServer = tietServer;
    }

    public boolean isCbRememberConnection()
    {
        return cbRememberConnection;
    }

    public void setCbRememberConnection(boolean cbRememberConnection)
    {
        this.cbRememberConnection = cbRememberConnection;
    }

    public void instanceNewConnectionRepository()
    {
        connectionRepository = new ConnectionRepository();
    }

    public void isConnected()
    {
        connectionRepository.isConnected();
    }

    public LiveData<Boolean> getIsConnectedLiveData()
    {
        return connectionRepository.getIsConnectedLiveData();
    }
}
