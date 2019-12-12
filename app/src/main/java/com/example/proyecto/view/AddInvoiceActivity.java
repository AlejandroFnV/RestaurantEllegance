package com.example.proyecto.view;

import android.os.Bundle;

import com.example.proyecto.R;
import com.example.proyecto.model.Utils;
import com.example.proyecto.model.data.Employee;
import com.example.proyecto.model.data.Invoice;
import com.example.proyecto.model.view.AddInvoiceActivityVM;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AddInvoiceActivity extends AppCompatActivity
{
    //Static
    private static final int MAX_TABLE_STRING_LENGTH = 4;
    private static final int WAIT_MILIS_TO_CHECK_TABLE = 500;

    //View model
    private AddInvoiceActivityVM addInvoiceActivityVM;

    //Other
    private Employee employee;
    private Handler handler;

    //Layout components
    private ProgressBar pbLoading;
    private ConstraintLayout clActivity;
    private TextView tvTable, tvErrorMessage;
    private Button btNum7, btNum8, btNum9, btNum4, btNum5, btNum6, btNum1, btNum2, btNum3, btNum0, btDelete, btAddInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //////////////////////////////////////////////////////////////////////////

        initComponents();
        assignViewModel();

        if(!addInvoiceActivityVM.isStarted())
        {
            employee = (Employee) getIntent().getExtras().get("employee");
            if(employee == null || employee.getId() <= 0) finish();
            else
            {
                addInvoiceActivityVM.setTouchable(true);
                addInvoiceActivityVM.setLoading(false);
                addInvoiceActivityVM.setHandler(new Handler());
                addInvoiceActivityVM.setEmployee(employee);
                addInvoiceActivityVM.setTvErrorMessageVisible(false);
                addInvoiceActivityVM.setTable("");
                addInvoiceActivityVM.setStarted(true);
            }
        }

        assignComponentsInfo();
        assignEvents();
    }

    private void initComponents()
    {
        pbLoading = findViewById(R.id.pbLoading);
        clActivity = findViewById(R.id.clActivity);
        tvTable = findViewById(R.id.tvTable);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        btNum7 = findViewById(R.id.btNum7);
        btNum8 = findViewById(R.id.btNum8);
        btNum9 = findViewById(R.id.btNum9);
        btNum4 = findViewById(R.id.btNum4);
        btNum5 = findViewById(R.id.btNum5);
        btNum6 = findViewById(R.id.btNum6);
        btNum1 = findViewById(R.id.btNum1);
        btNum2 = findViewById(R.id.btNum2);
        btNum3 = findViewById(R.id.btNum3);
        btNum0 = findViewById(R.id.btNum0);
        btDelete = findViewById(R.id.btDelete);
        btAddInvoice = findViewById(R.id.btAddInvoice);
    }

    private void assignViewModel()
    {
        addInvoiceActivityVM = ViewModelProviders.of(this).get(AddInvoiceActivityVM.class);
    }

    private void assignComponentsInfo()
    {
        setActivityTouchable(addInvoiceActivityVM.isTouchable());
        setActivityLoading(addInvoiceActivityVM.isLoading());
        handler = addInvoiceActivityVM.getHandler();
        tvErrorMessage.setVisibility(addInvoiceActivityVM.isTvErrorMessageVisible() ? View.VISIBLE : View.INVISIBLE);
        tvTable.setText(addInvoiceActivityVM.getTable());
    }

    Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            if(addInvoiceActivityVM.getTable().length() > 0)
            {
                Long table = Long.parseLong(addInvoiceActivityVM.getTable());
                if(table > 0) addInvoiceActivityVM.isTableInUse(table);
            }
            else addInvoiceActivityVM.isTableInUse(0l);
        }
    };

    private void assignEvents()
    {
        btNum7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(addInvoiceActivityVM.getTable().length() < MAX_TABLE_STRING_LENGTH)
                {
                    handler.removeCallbacks(runnable);
                    addInvoiceActivityVM.setTable(addInvoiceActivityVM.getTable() + "7");
                    tvTable.setText(addInvoiceActivityVM.getTable());
                    handler.postDelayed(runnable, WAIT_MILIS_TO_CHECK_TABLE);
                }
            }
        });

        btNum8.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(addInvoiceActivityVM.getTable().length() < MAX_TABLE_STRING_LENGTH)
                {
                    handler.removeCallbacks(runnable);
                    addInvoiceActivityVM.setTable(addInvoiceActivityVM.getTable() + "8");
                    tvTable.setText(addInvoiceActivityVM.getTable());
                    handler.postDelayed(runnable, WAIT_MILIS_TO_CHECK_TABLE);
                }
            }
        });

        btNum9.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(addInvoiceActivityVM.getTable().length() < MAX_TABLE_STRING_LENGTH)
                {
                    handler.removeCallbacks(runnable);
                    addInvoiceActivityVM.setTable(addInvoiceActivityVM.getTable() + "9");
                    tvTable.setText(addInvoiceActivityVM.getTable());
                    handler.postDelayed(runnable, WAIT_MILIS_TO_CHECK_TABLE);
                }
            }
        });

        btNum4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(addInvoiceActivityVM.getTable().length() < MAX_TABLE_STRING_LENGTH)
                {
                    handler.removeCallbacks(runnable);
                    addInvoiceActivityVM.setTable(addInvoiceActivityVM.getTable() + "4");
                    tvTable.setText(addInvoiceActivityVM.getTable());
                    handler.postDelayed(runnable, WAIT_MILIS_TO_CHECK_TABLE);
                }
            }
        });

        btNum5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(addInvoiceActivityVM.getTable().length() < MAX_TABLE_STRING_LENGTH)
                {
                    handler.removeCallbacks(runnable);
                    addInvoiceActivityVM.setTable(addInvoiceActivityVM.getTable() + "5");
                    tvTable.setText(addInvoiceActivityVM.getTable());
                    handler.postDelayed(runnable, WAIT_MILIS_TO_CHECK_TABLE);
                }
            }
        });

        btNum6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(addInvoiceActivityVM.getTable().length() < MAX_TABLE_STRING_LENGTH)
                {
                    handler.removeCallbacks(runnable);
                    addInvoiceActivityVM.setTable(addInvoiceActivityVM.getTable() + "6");
                    tvTable.setText(addInvoiceActivityVM.getTable());
                    handler.postDelayed(runnable, WAIT_MILIS_TO_CHECK_TABLE);
                }
            }
        });

        btNum1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(addInvoiceActivityVM.getTable().length() < MAX_TABLE_STRING_LENGTH)
                {
                    handler.removeCallbacks(runnable);
                    addInvoiceActivityVM.setTable(addInvoiceActivityVM.getTable() + "1");
                    tvTable.setText(addInvoiceActivityVM.getTable());
                    handler.postDelayed(runnable, WAIT_MILIS_TO_CHECK_TABLE);
                }
            }
        });

        btNum2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(addInvoiceActivityVM.getTable().length() < MAX_TABLE_STRING_LENGTH)
                {
                    handler.removeCallbacks(runnable);
                    addInvoiceActivityVM.setTable(addInvoiceActivityVM.getTable() + "2");
                    tvTable.setText(addInvoiceActivityVM.getTable());
                    handler.postDelayed(runnable, WAIT_MILIS_TO_CHECK_TABLE);
                }
            }
        });

        btNum3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(addInvoiceActivityVM.getTable().length() < MAX_TABLE_STRING_LENGTH)
                {
                    handler.removeCallbacks(runnable);
                    addInvoiceActivityVM.setTable(addInvoiceActivityVM.getTable() + "3");
                    tvTable.setText(addInvoiceActivityVM.getTable());
                    handler.postDelayed(runnable, WAIT_MILIS_TO_CHECK_TABLE);
                }
            }
        });

        btNum0.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(addInvoiceActivityVM.getTable().length() < MAX_TABLE_STRING_LENGTH)
                {
                    handler.removeCallbacks(runnable);
                    addInvoiceActivityVM.setTable(addInvoiceActivityVM.getTable() + "0");
                    tvTable.setText(addInvoiceActivityVM.getTable());
                    handler.postDelayed(runnable, WAIT_MILIS_TO_CHECK_TABLE);
                }
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(addInvoiceActivityVM.getTable().length() > 0)
                {
                    handler.removeCallbacks(runnable);
                    String table = addInvoiceActivityVM.getTable();
                    addInvoiceActivityVM.setTable(table.substring(0, table.length() - 1));
                    tvTable.setText(addInvoiceActivityVM.getTable());
                    handler.postDelayed(runnable, WAIT_MILIS_TO_CHECK_TABLE);
                }
            }
        });

        btAddInvoice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(addInvoiceActivityVM.getTable().length() > 0)
                {
                    handler.removeCallbacks(runnable);
                    Long table = Long.parseLong(addInvoiceActivityVM.getTable());
                    if(table > 0)
                    {
                        setActivityLoading(true);
                        addInvoiceActivityVM.setLongTable(table);
                        addInvoiceActivityVM.isTableInUse(table);
                    }
                    else Snackbar.make(btAddInvoice, getString(R.string.toastInvalidTable), Snackbar.LENGTH_LONG).show();
                }
                else Snackbar.make(btAddInvoice, getString(R.string.toastInvalidTable), Snackbar.LENGTH_LONG).show();
            }
        });

        addInvoiceActivityVM.getIsTableInUseLiveData().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean used)
            {
                if(addInvoiceActivityVM.isLoading())
                {
                    if(used == null || !used)
                    {
                        Invoice invoice = new Invoice();
                        invoice.setTable(addInvoiceActivityVM.getLongTable());
                        invoice.setOpen(Utils.getCurrentTime());
                        invoice.setIdOpenEmployee(employee.getId());
                        invoice.setTotal(0.0f);
                        addInvoiceActivityVM.postInvoice(invoice);
                    }
                    else
                    {
                        setActivityLoading(false);
                        Snackbar.make(btAddInvoice, getString(R.string.toastTableInUse), Snackbar.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(used == null || !used) addInvoiceActivityVM.setTvErrorMessageVisible(false);
                    else addInvoiceActivityVM.setTvErrorMessageVisible(true);

                    tvErrorMessage.setVisibility(addInvoiceActivityVM.isTvErrorMessageVisible() ? View.VISIBLE : View.INVISIBLE);
                }
            }
        });

        addInvoiceActivityVM.getPostInvoiceLiveData().observe(this, new Observer<Long>()
        {
            @Override
            public void onChanged(Long id)
            {
                if(id != null && id > 0)
                {
                    setResult(RESULT_OK);
                    finish();
                }
                else
                {
                    setActivityLoading(false);
                    Snackbar.make(btAddInvoice, getString(R.string.toastCreateInvoiceError), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setActivityLoading(boolean toggle)
    {
        if(toggle)
        {
            pbLoading.setVisibility(View.VISIBLE);
            clActivity.setVisibility(View.INVISIBLE);
        }
        else
        {
            pbLoading.setVisibility(View.INVISIBLE);
            clActivity.setVisibility(View.VISIBLE);
        }
        setActivityTouchable(!toggle);
        addInvoiceActivityVM.setLoading(toggle);
    }

    private void setActivityTouchable(boolean toggle)
    {
        addInvoiceActivityVM.setTouchable(toggle);
        Utils.toggleActivityTouchable(this, addInvoiceActivityVM.isTouchable());
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

    @Override
    protected void onResume()
    {
        super.onResume();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
