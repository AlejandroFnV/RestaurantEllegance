package com.example.proyecto.view;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto.R;
import com.example.proyecto.model.Utils;
import com.example.proyecto.model.adapter.recycler_view.OrderInvoiceAdapter;
import com.example.proyecto.model.data.Employee;
import com.example.proyecto.model.data.Invoice;
import com.example.proyecto.model.data.Order;
import com.example.proyecto.model.data.PrintInvoice;
import com.example.proyecto.model.data.Product;
import com.example.proyecto.model.view.InactiveInvoiceActivityVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class InactiveInvoiceActivity extends AppCompatActivity
{
    //Static
    //private static final int PRINT_RESULT = 1;

    //View model
    private InactiveInvoiceActivityVM inactiveInvoiceActivityVM;
    private OrderInvoiceAdapter orderInvoiceAdapter;

    //Other
    private Invoice invoice;
    private Employee openEmployee;
    private Employee closeEmployee;

    //Layout components
    private ProgressBar pbLoading;
    private CardView cvInactiveInvoice;
    private ConstraintLayout clActivity;
    private TextView tvInvoice, tvOpenEmployee, tvOpenTime, tvCloseEmployee, tvCloseTime, tvTable, tvTotal;
    private RecyclerView rvOrder;
    private TextView tvNoData;
    private Button btPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inactive_invoice);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //////////////////////////////////////////////////////////////////////////

        initComponents();
        assignViewModel();

        if(!inactiveInvoiceActivityVM.isStarted())
        {
            Bundle bundle = getIntent().getExtras();
            invoice = (Invoice) bundle.get("invoice");
            if(invoice == null || invoice.getId() <= 0) finish();
            else
            {
                setActivityLoading(true);
                inactiveInvoiceActivityVM.setInvoice(invoice);
                inactiveInvoiceActivityVM.setTvNoDataVisible(true);
                inactiveInvoiceActivityVM.getEmployeeFromDB(invoice.getIdOpenEmployee());
                inactiveInvoiceActivityVM.setStarted(true);
            }
        }

        assignComponentsInfo();
        assignEvents();
    }

    private void initComponents()
    {
        pbLoading = findViewById(R.id.pbLoading);
        cvInactiveInvoice = findViewById(R.id.cvInactiveInvoice);
        clActivity = findViewById(R.id.clActivity);
        tvInvoice = findViewById(R.id.tvInvoice);
        tvOpenEmployee = findViewById(R.id.tvOpenEmployee);
        tvOpenTime = findViewById(R.id.tvOpenTime);
        tvCloseEmployee = findViewById(R.id.tvCloseEmployee);
        tvCloseTime = findViewById(R.id.tvCloseTime);
        tvTable = findViewById(R.id.tvTable);
        tvTotal = findViewById(R.id.tvTotal);
        rvOrder = findViewById(R.id.rvOrder);
        tvNoData = findViewById(R.id.tvNoData);
        btPrint = findViewById(R.id.btPrint);

        orderInvoiceAdapter = new OrderInvoiceAdapter(this);
        rvOrder.setLayoutManager(new LinearLayoutManager(this));
        rvOrder.setAdapter(orderInvoiceAdapter);
    }

    private void assignViewModel()
    {
        inactiveInvoiceActivityVM = ViewModelProviders.of(this).get(InactiveInvoiceActivityVM.class);
        inactiveInvoiceActivityVM.setCurrentOrderProductIndex(0);
    }

    private void assignComponentsInfo()
    {
        invoice = inactiveInvoiceActivityVM.getInvoice();
        openEmployee = inactiveInvoiceActivityVM.getOpenEmployee();
        closeEmployee = inactiveInvoiceActivityVM.getCloseEmployee();

        setActivityLoading(inactiveInvoiceActivityVM.isLoading());
        setActivityTouchable(inactiveInvoiceActivityVM.isTouchable());
        tvNoData.setVisibility(inactiveInvoiceActivityVM.isTvNoDataVisible() ? View.VISIBLE : View.INVISIBLE);

        tvInvoice.setText(getText(R.string.tvInvoice) + " " + invoice.getId());
        tvOpenEmployee.setText(getText(R.string.tvOpenEmployee) + " " + (openEmployee == null ? invoice.getIdOpenEmployee() : openEmployee.getLogin() + " (" + openEmployee.getId() + ")"));
        tvOpenTime.setText(getText(R.string.tvOpenTime) + " " + invoice.getOpen());
        tvCloseEmployee.setText(getText(R.string.tvCloseEmployee) + " " + (closeEmployee == null ? invoice.getIdCloseEmployee() : closeEmployee.getLogin() + " (" + closeEmployee.getId() + ")"));
        tvCloseTime.setText(getText(R.string.tvCloseTime) + " " + invoice.getClose());
        tvTable.setText(getText(R.string.tvTable) + " " + invoice.getTable());

        NumberFormat nfTotal = NumberFormat.getNumberInstance();
        String total_string = nfTotal.format(invoice.getTotal());
        tvTotal.setText(getText(R.string.tvTotal) + " " + total_string + "€");
    }

    private void assignEvents()
    {
        inactiveInvoiceActivityVM.getGetEmployeeLiveData().observe(this, new Observer<Employee>()
        {
            @Override
            public void onChanged(Employee employee)
            {
                if(employee == null || employee.getId() <= 0) finish();
                else
                {
                    if(inactiveInvoiceActivityVM.isOpenEmployeeAdded())
                    {
                        inactiveInvoiceActivityVM.setCloseEmployee(employee);
                        inactiveInvoiceActivityVM.getByInvoice(invoice.getId());
                    }
                    else
                    {
                        inactiveInvoiceActivityVM.setOpenEmployee(employee);
                        inactiveInvoiceActivityVM.setOpenEmployeeAdded(true);
                        inactiveInvoiceActivityVM.getEmployeeFromDB(invoice.getIdCloseEmployee());
                    }
                }
            }
        });

        inactiveInvoiceActivityVM.getGetByInvoiceLiveData().observe(this, new Observer<ArrayList<Order>>()
        {
            @Override
            public void onChanged(ArrayList<Order> orders)
            {
                if(orders != null && orders.size() > 0)
                {
                    inactiveInvoiceActivityVM.setOrders(orders);
                    orderInvoiceAdapter.setData(orders);
                    inactiveInvoiceActivityVM.resolveOrdersProductsName();
                    inactiveInvoiceActivityVM.setTvNoDataVisible(false);

                }
                else inactiveInvoiceActivityVM.setTvNoDataVisible(true);
                inactiveInvoiceActivityVM.setLoading(false);
                inactiveInvoiceActivityVM.setTouchable(false);
                assignComponentsInfo();
            }
        });

        inactiveInvoiceActivityVM.getProductShowLiveData().observe(this, new Observer<Product>()
        {
            @Override
            public void onChanged(Product product)
            {
                if(inactiveInvoiceActivityVM.getCurrentOrderProductIndex() < inactiveInvoiceActivityVM.getOrders().size())
                {
                    if(product != null)
                        inactiveInvoiceActivityVM.getOrders().get(inactiveInvoiceActivityVM.getCurrentOrderProductIndex()).setProduct_name(product.getName());

                    inactiveInvoiceActivityVM.setCurrentOrderProductIndex(inactiveInvoiceActivityVM.getCurrentOrderProductIndex() + 1);
                    if(inactiveInvoiceActivityVM.getCurrentOrderProductIndex() >= inactiveInvoiceActivityVM.getOrders().size())
                        orderInvoiceAdapter.setData(inactiveInvoiceActivityVM.getOrders());
                    else inactiveInvoiceActivityVM.getProductById(inactiveInvoiceActivityVM.getOrders().get(inactiveInvoiceActivityVM.getCurrentOrderProductIndex()).getIdProduct());
                }
            }
        });

        btPrint.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(InactiveInvoiceActivity.this, PrintInvoice.class);
                intent.putExtra("idInvoice", invoice.getId());
                startActivity(intent);
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
        inactiveInvoiceActivityVM.setLoading(toggle);
    }

    private void setActivityTouchable(boolean toggle)
    {
        inactiveInvoiceActivityVM.setTouchable(toggle);
        Utils.toggleActivityTouchable(this, inactiveInvoiceActivityVM.isTouchable());
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
