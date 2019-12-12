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
import com.example.proyecto.model.data.Employee;
import com.example.proyecto.model.Utils;
import com.example.proyecto.model.view.LoginActivityVM;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity
{
    //View model
    private LoginActivityVM loginActivityVM;

    //Layout components
    private ImageView ivLoading;
    private ConstraintLayout clForm;
    private TextInputLayout tilUser;
    private TextInputEditText tietUser;
    private TextInputLayout tilPassword;
    private TextInputEditText tietPassword;
    private CheckBox cbRememberUser;
    private Button btLogin;
    private Button btChangeServer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //////////////////////////////////////////////////////////////////////////

        initComponents();
        assignViewModel();

        if(!loginActivityVM.isStarted())
        {
            loginActivityVM.setLoading(true);

            SharedPreferences sharedPreferences = getSharedPreferences(Utils.getSharedPreferences(), Context.MODE_PRIVATE);
            loginActivityVM.setCbRememberUser(sharedPreferences.getBoolean("remember_user", false));
            loginActivityVM.setTietUser(sharedPreferences.getString("user", ""));
            loginActivityVM.setTietPassword(sharedPreferences.getString("password", ""));

            if(loginActivityVM.isCbRememberUser()) tryLogin();
            else loginActivityVM.setLoading(false);

            loginActivityVM.setStarted(true);
            loginActivityVM.setTouchable(true);
        }

        assignComponentsInfo();
        assignEvents();
    }

    private void initComponents()
    {
        ivLoading = findViewById(R.id.ivLoading);
        clForm = findViewById(R.id.clForm);
        tilUser = findViewById(R.id.tilUser);
        tietUser = findViewById(R.id.tietUser);
        tilPassword = findViewById(R.id.tilPassword);
        tietPassword = findViewById(R.id.tietPassword);
        cbRememberUser = findViewById(R.id.cbRememberUser);
        btLogin = findViewById(R.id.btLogin);
        btChangeServer = findViewById(R.id.btChangeServer);
    }

    private void assignViewModel()
    {
        loginActivityVM = ViewModelProviders.of(this).get(LoginActivityVM.class);
        loginActivityVM.setTilUserError_fix(false);
        loginActivityVM.setTilPasswordError_fix(false);
    }

    private void assignComponentsInfo()
    {
        setActivityTouchable(loginActivityVM.isTouchable());
        setActivityLoading(loginActivityVM.isLoading());
        tilUser.setError(loginActivityVM.getTilUserError());
        tietUser.setText(loginActivityVM.getTietUser());
        tilPassword.setError(loginActivityVM.getTilPasswordError());
        tietPassword.setText(loginActivityVM.getTietPassword());
        cbRememberUser.setChecked(loginActivityVM.isCbRememberUser());
    }

    private void assignEvents()
    {
        tietUser.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                if(loginActivityVM.isTilUserError_fix())
                {
                    loginActivityVM.setTilUserError(null);
                    tilUser.setError(loginActivityVM.getTilUserError());
                }
                else loginActivityVM.setTilUserError_fix(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                loginActivityVM.setTietUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        tietPassword.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                if(loginActivityVM.isTilPasswordError_fix())
                {
                    loginActivityVM.setTilPasswordError(null);
                    tilPassword.setError(loginActivityVM.getTilPasswordError());
                }
                else loginActivityVM.setTilPasswordError_fix(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                loginActivityVM.setTietPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        cbRememberUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                loginActivityVM.setCbRememberUser(isChecked);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean login = true;
                loginActivityVM.setTietUser(tietUser.getText().toString());
                loginActivityVM.setTietPassword(tietPassword.getText().toString());
                if(loginActivityVM.getTietUser().isEmpty())
                {
                    login = false;
                    loginActivityVM.setTilUserError(LoginActivity.this.getString(R.string.tietInvalidValue));
                    tilUser.setError(loginActivityVM.getTilUserError());
                }
                if(loginActivityVM.getTietPassword().isEmpty())
                {
                    login = false;
                    loginActivityVM.setTilPasswordError(LoginActivity.this.getString(R.string.tietInvalidValue));
                    tilPassword.setError(loginActivityVM.getTilPasswordError());
                }

                if(login)
                    tryLogin();
            }
        });

        loginActivityVM.getGetLiveData().observe(this, new Observer<Employee>()
        {
            @Override
            public void onChanged(Employee employee)
            {
                if(employee != null && loginActivityVM.getTietPassword().equals(employee.getPassword()))
                {
                    Utils.setEmployeeId(employee.getId());

                    if(loginActivityVM.isCbRememberUser())
                    {
                        SharedPreferences sharedPreferences = getSharedPreferences(Utils.getSharedPreferences(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("remember_user", loginActivityVM.isCbRememberUser());
                        editor.putString("user", loginActivityVM.getTietUser());
                        editor.putString("password", loginActivityVM.getTietPassword());
                        editor.apply();
                    }

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("employee", employee);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Snackbar.make(btLogin, getText(R.string.toastInvalidLogin), Snackbar.LENGTH_LONG).show();
                    setActivityLoading(false);
                }
            }
        });

        btChangeServer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setActivityTouchable(false);
                SharedPreferences sharedPreferences = getSharedPreferences(Utils.getSharedPreferences(), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("remember_server");
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, ConnectionActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void tryLogin()
    {
        setActivityLoading(true);
        loginActivityVM.get(loginActivityVM.getTietUser());
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
        loginActivityVM.setLoading(toggle);
    }

    private void setActivityTouchable(boolean toggle)
    {
        loginActivityVM.setTouchable(toggle);
        Utils.toggleActivityTouchable(this, loginActivityVM.isTouchable());
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}
