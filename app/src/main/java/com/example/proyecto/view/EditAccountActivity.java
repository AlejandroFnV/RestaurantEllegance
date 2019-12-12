package com.example.proyecto.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.proyecto.R;
import com.example.proyecto.model.Utils;
import com.example.proyecto.model.data.Employee;
import com.example.proyecto.model.view.EditAccountActivityVM;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;

public class EditAccountActivity extends AppCompatActivity
{
    //Static
    private static final int SELECTED_IMAGE_RESULT = 1;

    //View model
    private EditAccountActivityVM editAccountActivityVM;

    //Other
    private Employee employee;

    //Layout components
    private ProgressBar pbLoading;
    private ConstraintLayout clForm;
    private ImageView ivAccount;
    private TextInputLayout tilUser;
    private TextInputEditText tietUser;
    private TextInputLayout tilPassword;
    private TextInputEditText tietPassword;
    private Button btUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //////////////////////////////////////////////////////////////////////////

        initComponents();
        assignViewModel();

        if(!editAccountActivityVM.isStarted())
        {
            employee = (Employee) getIntent().getExtras().get("employee");
            if(employee == null || employee.getId() <= 0) finish();
            else
            {
                editAccountActivityVM.setEmployee(employee);
                editAccountActivityVM.setIvUriAccount(Uri.parse(Utils.getServerUrl() + "upload/images/employees/" + employee.getId() + ".jpg"));
                editAccountActivityVM.setTietUser(employee.getLogin());
                editAccountActivityVM.setTietPassword(employee.getPassword());
                editAccountActivityVM.setImageChanged(false);
                editAccountActivityVM.setStarted(true);
                editAccountActivityVM.setTouchable(true);
            }
        }

        assignComponentsInfo();
        assignEvents();
    }

    private void initComponents()
    {
        pbLoading = findViewById(R.id.pbLoading);
        clForm = findViewById(R.id.clForm);
        ivAccount = findViewById(R.id.ivAccount);
        tilUser = findViewById(R.id.tilUser);
        tietUser = findViewById(R.id.tietUser);
        tilPassword = findViewById(R.id.tilPassword);
        tietPassword = findViewById(R.id.tietPassword);
        btUpdate = findViewById(R.id.btUpdate);
    }

    private void assignViewModel()
    {
        editAccountActivityVM = ViewModelProviders.of(this).get(EditAccountActivityVM.class);
        editAccountActivityVM.setTilUserError_fix(false);
        editAccountActivityVM.setTilPasswordError_fix(false);
    }

    private void assignComponentsInfo()
    {
        setActivityTouchable(editAccountActivityVM.isTouchable());
        setActivityLoading(editAccountActivityVM.isLoading());

        employee = editAccountActivityVM.getEmployee();
        Glide
                .with(this)
                .load(editAccountActivityVM.getIvUriAccount())
                .apply(
                        new RequestOptions()
                                .placeholder(R.drawable.ic_account_circle_black_200dp)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .circleCrop())
                .listener(new RequestListener<Drawable>()
                {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                    {
                        ivAccount.setImageResource(R.drawable.ic_account_circle_black_200dp);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                    {
                        return false;
                    }
                })
                .into(ivAccount);

        tilUser.setError(editAccountActivityVM.getTilUserError());
        tietUser.setText(editAccountActivityVM.getTietUser());
        tilPassword.setError(editAccountActivityVM.getTilPasswordError());
        tietPassword.setText(editAccountActivityVM.getTietPassword());
    }

    private void assignEvents()
    {
        ivAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, SELECTED_IMAGE_RESULT);
            }
        });

        tietUser.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                if(editAccountActivityVM.isTilUserError_fix())
                {
                    editAccountActivityVM.setTilUserError(null);
                    tilUser.setError(editAccountActivityVM.getTilUserError());
                }
                else editAccountActivityVM.setTilUserError_fix(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                editAccountActivityVM.setTietUser(s.toString());
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
                if(editAccountActivityVM.isTilPasswordError_fix())
                {
                    editAccountActivityVM.setTilPasswordError(null);
                    tilPassword.setError(editAccountActivityVM.getTilPasswordError());
                }
                else editAccountActivityVM.setTilPasswordError_fix(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                editAccountActivityVM.setTietPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean update = true;
                editAccountActivityVM.setTietUser(tietUser.getText().toString());
                editAccountActivityVM.setTietPassword(tietPassword.getText().toString());
                if(editAccountActivityVM.getTietUser().isEmpty())
                {
                    update = false;
                    editAccountActivityVM.setTilUserError(EditAccountActivity.this.getString(R.string.tietInvalidValue));
                    tilUser.setError(editAccountActivityVM.getTilUserError());
                }
                if(editAccountActivityVM.getTietPassword().isEmpty())
                {
                    update = false;
                    editAccountActivityVM.setTilPasswordError(EditAccountActivity.this.getString(R.string.tietInvalidValue));
                    tilPassword.setError(editAccountActivityVM.getTilPasswordError());
                }

                if(update)
                {
                    setActivityLoading(true);
                    employee.setLogin(editAccountActivityVM.getTietUser());
                    employee.setPassword(editAccountActivityVM.getTietPassword());
                    editAccountActivityVM.update(employee.getId(), employee);
                }
            }
        });

        editAccountActivityVM.getUpdateLiveData().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean updated)
            {
                if(updated == null || !updated)
                {
                    Snackbar.make(btUpdate, getString(R.string.toastUpdateFailed), Snackbar.LENGTH_LONG).show();
                    setActivityLoading(false);
                }
                else
                {
                    if(editAccountActivityVM.isImageChanged())
                    {
                        File image = new File(editAccountActivityVM.getIvUriAccount().getPath());
                        editAccountActivityVM.upload_image(employee.getId(), image);
                    }
                    else
                    {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("employee", employee);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                }
            }
        });

        editAccountActivityVM.getUpload_imageLiveData().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean uploaded)
            {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("employee", employee);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void setActivityLoading(boolean toggle)
    {
        if(toggle)
        {
            pbLoading.setVisibility(View.VISIBLE);
            clForm.setVisibility(View.INVISIBLE);
        }
        else
        {
            pbLoading.setVisibility(View.INVISIBLE);
            clForm.setVisibility(View.VISIBLE);
        }
        setActivityTouchable(!toggle);
        editAccountActivityVM.setLoading(toggle);
    }

    private void setActivityTouchable(boolean toggle)
    {
        editAccountActivityVM.setTouchable(toggle);
        Utils.toggleActivityTouchable(this, editAccountActivityVM.isTouchable());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECTED_IMAGE_RESULT && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri imageUri = data.getData();
            File image = Utils.saveSelectedImageInFile(this, imageUri);

            if(image != null)
            {
                editAccountActivityVM.setIvUriAccount(Uri.fromFile(image));
                Glide.with(this)
                        .load(editAccountActivityVM.getIvUriAccount())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_account_circle_black_200dp)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .circleCrop())
                        .listener(new RequestListener<Drawable>()
                        {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                            {
                                ivAccount.setImageResource(R.drawable.ic_account_circle_black_200dp);
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                            {
                                editAccountActivityVM.setImageChanged(true);
                                return false;
                            }
                        })
                        .into(ivAccount);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
