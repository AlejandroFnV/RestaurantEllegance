package com.example.proyecto.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.proyecto.R;
import com.example.proyecto.model.data.Employee;
import com.example.proyecto.model.Utils;
import com.example.proyecto.model.view.MainActivityVM;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity
{
    //Static
    private static final int EDIT_EMPLOYEE_RESULT = 1;

    //View model
    private MainActivityVM mainActivityVM;

    //Other
    private Employee employee;
    private AppBarConfiguration appBarConfiguration;

    //Layout components
    private View headerLayout;
    private LinearLayout lyEmployee;
    private ImageView ivEmployee;
    private TextView tvEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_invoice, R.id.nav_order, R.id.nav_invoice_history, R.id.nav_logout, R.id.nav_server_logout).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        headerLayout = navigationView.getHeaderView(0);

        //////////////////////////////////////////////////////////////////////////

        initComponents();
        assignViewModel();

        if(!mainActivityVM.isStarted())
        {
            employee = (Employee) getIntent().getExtras().get("employee");
            if(employee == null || employee.getId() <= 0) disconnect();
            else
            {
                mainActivityVM.setEmployee(employee);
                mainActivityVM.setStarted(true);
                mainActivityVM.setTouchable(true);
            }
        }

        assignComponentsInfo();
        assignEvents();
    }

    private void initComponents()
    {
        lyEmployee = headerLayout.findViewById(R.id.lyEmployee);
        ivEmployee = headerLayout.findViewById(R.id.ivEmployee);
        tvEmployee = headerLayout.findViewById(R.id.tvEmployee);
    }

    private void assignViewModel()
    {
        mainActivityVM = ViewModelProviders.of(this).get(MainActivityVM.class);
    }

    private void assignComponentsInfo()
    {
        setActivityTouchable(mainActivityVM.isTouchable());
        employee = mainActivityVM.getEmployee();
        if(employee != null && employee.getId() > 0)
        {
            Glide
                    .with(this)
                    .load(Uri.parse(Utils.getServerUrl() + "upload/images/employees/" + employee.getId() + ".jpg"))
                    .apply(
                            new RequestOptions()
                                    .placeholder(R.drawable.ic_account_circle_24dp)
                                    .fitCenter()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .circleCrop())
                    .listener(new RequestListener<Drawable>()
                    {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                        {
                            ivEmployee.setImageResource(R.drawable.ic_account_circle_24dp);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                        {
                            return false;
                        }
                    })
                    .into(ivEmployee);


            tvEmployee.setText(employee.getLogin());
        }
        else
        {
            ivEmployee.setImageResource(R.drawable.ic_account_circle_24dp);
            tvEmployee.setText("");
        }
    }

    private void assignEvents()
    {
        lyEmployee.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setActivityTouchable(false);
                Intent intent = new Intent(MainActivity.this, EditAccountActivity.class);
                intent.putExtra("employee", employee);
                startActivityForResult(intent, EDIT_EMPLOYEE_RESULT);
            }
        });
    }

    public Employee getEmployee()
    {
        return employee;
    }

    public void logout()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.getSharedPreferences(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("remember_user");
        editor.remove("user");
        editor.remove("password");
        editor.apply();

        Utils.setEmployeeId(0);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void disconnect()
    {
        Utils.setEmployeeId(0);

        SharedPreferences sharedPreferences = getSharedPreferences(Utils.getSharedPreferences(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("remember_user");
        editor.remove("password");
        editor.remove("remember_server");
        editor.apply();

        Intent intent = new Intent(this, ConnectionActivity.class);
        startActivity(intent);
        finish();
    }

    public void setActivityTouchable(boolean toggle)
    {
        mainActivityVM.setTouchable(toggle);
        Utils.toggleActivityTouchable(this, mainActivityVM.isTouchable());
    }

    public boolean isTouchable()
    {
        return mainActivityVM.isTouchable();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_EMPLOYEE_RESULT)
        {
            setActivityTouchable(true);
            if(resultCode == RESULT_OK && data != null)
            {
                Snackbar.make(tvEmployee, getString(R.string.toastUserUpdated), Snackbar.LENGTH_LONG).show();
                mainActivityVM.setEmployee((Employee) data.getExtras().get("employee"));
                assignComponentsInfo();

                SharedPreferences sharedPreferences = getSharedPreferences(Utils.getSharedPreferences(), Context.MODE_PRIVATE);
                if(sharedPreferences.getBoolean("remember_user", false))
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user", employee.getLogin());
                    editor.putString("password", employee.getPassword());
                    editor.apply();
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}
