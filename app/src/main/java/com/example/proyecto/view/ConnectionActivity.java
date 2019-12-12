package com.example.proyecto.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.example.proyecto.R;
import com.example.proyecto.model.Utils;
import com.example.proyecto.model.view.ConnectionActivityVM;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ConnectionActivity extends AppCompatActivity
{
    //View model
    private ConnectionActivityVM connectionActivityVM;

    //Layout components
    private ImageView ivLoading;
    private ConstraintLayout clForm;
    private TextInputLayout tilServer;
    private TextInputEditText tietServer;
    private CheckBox cbRememberConnection;
    private Button btConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        //////////////////////////////////////////////////////////////////////////

        initComponents();
        assignViewModel();

        if(!connectionActivityVM.isStarted())
        {
            connectionActivityVM.setLoading(true);

            SharedPreferences sharedPreferences = getSharedPreferences(Utils.getSharedPreferences(), Context.MODE_PRIVATE);
            connectionActivityVM.setCbRememberConnection(sharedPreferences.getBoolean("remember_server", false));
            connectionActivityVM.setTietServer(sharedPreferences.getString("server", ""));

            if(connectionActivityVM.isCbRememberConnection()) tryConnection();
            else connectionActivityVM.setLoading(false);

            connectionActivityVM.setStarted(true);
            connectionActivityVM.setTouchable(true);
        }

        assignComponentsInfo();
        assignEvents();
    }

    private void initComponents()
    {
        ivLoading = findViewById(R.id.ivLoading);
        clForm = findViewById(R.id.clForm);
        tilServer = findViewById(R.id.tilServer);
        tietServer = findViewById(R.id.tietServer);
        cbRememberConnection = findViewById(R.id.cbRememberConnection);
        btConnect = findViewById(R.id.btConnect);
    }

    private void assignViewModel()
    {
        connectionActivityVM = ViewModelProviders.of(this).get(ConnectionActivityVM.class);
        connectionActivityVM.setTilServerError_fix(false);
    }

    private void assignComponentsInfo()
    {
        setActivityTouchable(connectionActivityVM.isTouchable());
        setActivityLoading(connectionActivityVM.isLoading());
        tilServer.setError(connectionActivityVM.getTilServerError());
        tietServer.setText(connectionActivityVM.getTietServer());
        cbRememberConnection.setChecked(connectionActivityVM.isCbRememberConnection());
    }

    private void assignEvents()
    {
        tietServer.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                if(connectionActivityVM.isTilServerError_fix())
                {
                    connectionActivityVM.setTilServerError(null);
                    tilServer.setError(connectionActivityVM.getTilServerError());
                }
                else connectionActivityVM.setTilServerError_fix(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                connectionActivityVM.setTietServer(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        cbRememberConnection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                connectionActivityVM.setCbRememberConnection(isChecked);
            }
        });

        btConnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                connectionActivityVM.setTietServer(tietServer.getText().toString());
                if(connectionActivityVM.getTietServer().isEmpty())
                {
                    connectionActivityVM.setTilServerError(ConnectionActivity.this.getString(R.string.tietInvalidValue));
                    tilServer.setError(connectionActivityVM.getTilServerError());
                }
                else tryConnection();
            }
        });
    }

    private void tryConnection()
    {
        setActivityLoading(true);

        Utils.setServer(connectionActivityVM.getTietServer());
        connectionActivityVM.instanceNewConnectionRepository();
        connectionActivityVM.isConnected();
        connectionActivityVM.getIsConnectedLiveData().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean connected)
            {
                if(connected == null || !connected)
                {
                    Snackbar.make(btConnect, getText(R.string.toastInvalidServer), Snackbar.LENGTH_LONG).show();
                    tilServer.setError(connectionActivityVM.getTilServerError());
                    setActivityLoading(false);
                }
                else
                {
                    if(connectionActivityVM.isCbRememberConnection())
                    {
                        SharedPreferences sharedPreferences = getSharedPreferences(Utils.getSharedPreferences(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("remember_server", connectionActivityVM.isCbRememberConnection());
                        editor.putString("server", Utils.getServer());
                        editor.apply();
                    }

                    Intent intent = new Intent(ConnectionActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setActivityLoading(boolean toggle)
    {
        if(toggle)
        {
            ivLoading.setVisibility(View.VISIBLE);
            ivLoading.setAnimation(AnimationUtils.loadAnimation(this, R.anim.loading_image_animation));
            clForm.setVisibility(View.INVISIBLE);
        }
        else
        {
            ivLoading.setVisibility(View.INVISIBLE);
            ivLoading.setAnimation(null);
            clForm.setVisibility(View.VISIBLE);
        }
        setActivityTouchable(!toggle);
        connectionActivityVM.setLoading(toggle);
    }

    private void setActivityTouchable(boolean toggle)
    {
        connectionActivityVM.setTouchable(toggle);
        Utils.toggleActivityTouchable(this, connectionActivityVM.isTouchable());
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}
