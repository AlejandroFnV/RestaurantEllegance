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
import com.example.proyecto.model.view.InvoiceActivityVM;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class InvoiceActivity extends AppCompatActivity
{
    //Static
    private static final int ADD_ORDER_RESULT = 1;

    //View model
    private InvoiceActivityVM invoiceActivityVM;
    private OrderInvoiceAdapter orderInvoiceAdapter;

    //Other
    private Invoice invoice;
    private Employee employee;
    private Employee openEmployee;

    //Layout components
    private ProgressBar pbLoading;
    private CardView cvInvoice;
    private ConstraintLayout clActivity;
    private TextView tvInvoice, tvOpenEmployee, tvOpenTime, tvTable, tvTotal;
    private RecyclerView rvOrder;
    private TextView tvNoData;
    private Button btAddOrder, btCloseInvoice;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //////////////////////////////////////////////////////////////////////////

        initComponents();
        assignViewModel();

        if(!invoiceActivityVM.isStarted())
        {
            Bundle bundle = getIntent().getExtras();
            invoice = (Invoice) bundle.get("invoice");
            employee = (Employee) bundle.get("employee");
            if((invoice == null || invoice.getId() <= 0) || (employee == null || employee.getId() <= 0)) finish();
            else
            {
                setActivityLoading(true);
                invoiceActivityVM.setInvoice(invoice);
                invoiceActivityVM.setEmployee(employee);
                invoiceActivityVM.setTvNoDataVisible(true);
                invoiceActivityVM.getEmployeeFromDB(invoice.getIdOpenEmployee());
                invoiceActivityVM.setStarted(true);
            }
        }

        assignComponentsInfo();
        assignEvents();
    }

    private void initComponents()
    {
        pbLoading = findViewById(R.id.pbLoading);
        cvInvoice = findViewById(R.id.cvInvoice);
        clActivity = findViewById(R.id.clActivity);
        tvInvoice = findViewById(R.id.tvInvoice);
        tvOpenEmployee = findViewById(R.id.tvOpenEmployee);
        tvOpenTime = findViewById(R.id.tvOpenTime);
        tvTable = findViewById(R.id.tvTable);
        tvTotal = findViewById(R.id.tvTotal);
        rvOrder = findViewById(R.id.rvOrder);
        tvNoData = findViewById(R.id.tvNoData);
        btAddOrder = findViewById(R.id.btAddOrder);
        btCloseInvoice = findViewById(R.id.btCloseInvoice);

        orderInvoiceAdapter = new OrderInvoiceAdapter(this);
        rvOrder.setLayoutManager(new LinearLayoutManager(this));
        rvOrder.setAdapter(orderInvoiceAdapter);
    }

    private void assignViewModel()
    {
        invoiceActivityVM = ViewModelProviders.of(this).get(InvoiceActivityVM.class);
        invoiceActivityVM.setCurrentOrderProductIndex(0);
    }

    private void assignComponentsInfo()
    {
        invoice = invoiceActivityVM.getInvoice();
        employee = invoiceActivityVM.getEmployee();
        openEmployee = invoiceActivityVM.getOpenEmployee();

        setActivityLoading(invoiceActivityVM.isLoading());
        setActivityTouchable(invoiceActivityVM.isTouchable());
        tvNoData.setVisibility(invoiceActivityVM.isTvNoDataVisible() ? View.VISIBLE : View.INVISIBLE);

        tvInvoice.setText(getText(R.string.tvInvoice) + " " + invoice.getId());
        tvOpenEmployee.setText(getText(R.string.tvOpenEmployee) + " " + (openEmployee == null ? invoice.getIdOpenEmployee() : openEmployee.getLogin() + " (" + openEmployee.getId() + ")"));
        tvOpenTime.setText(getText(R.string.tvOpenTime) + " " + invoice.getOpen());
        tvTable.setText(getText(R.string.tvTable) + " " + invoice.getTable());

        NumberFormat nfTotal = NumberFormat.getNumberInstance();
        String total_string = nfTotal.format(invoice.getTotal());
        tvTotal.setText(getText(R.string.tvTotal) + " " + total_string + "€");
    }

    private void assignEvents()
    {
        invoiceActivityVM.getGetEmployeeLiveData().observe(this, new Observer<Employee>()
        {
            @Override
            public void onChanged(Employee employee)
            {
                if(employee == null || employee.getId() <= 0) finish();
                else
                {
                    invoiceActivityVM.setOpenEmployee(employee);
                    invoiceActivityVM.getByInvoice(invoice.getId());
                }
            }
        });

        invoiceActivityVM.getGetByInvoiceLiveData().observe(this, new Observer<ArrayList<Order>>()
        {
            @Override
            public void onChanged(ArrayList<Order> orders)
            {
                if(orders != null && orders.size() > 0)
                {
                    invoiceActivityVM.setOrders(orders);
                    orderInvoiceAdapter.setData(orders);
                    invoiceActivityVM.resolveOrdersProductsName();
                    invoiceActivityVM.setTvNoDataVisible(false);

                }
                else invoiceActivityVM.setTvNoDataVisible(true);
                invoiceActivityVM.setLoading(false);
                invoiceActivityVM.setTouchable(false);
                assignComponentsInfo();
            }
        });

        invoiceActivityVM.getProductShowLiveData().observe(this, new Observer<Product>()
        {
            @Override
            public void onChanged(Product product)
            {
                if(invoiceActivityVM.getCurrentOrderProductIndex() < invoiceActivityVM.getOrders().size())
                {
                    if(product != null)
                        invoiceActivityVM.getOrders().get(invoiceActivityVM.getCurrentOrderProductIndex()).setProduct_name(product.getName());

                    invoiceActivityVM.setCurrentOrderProductIndex(invoiceActivityVM.getCurrentOrderProductIndex() + 1);
                    if(invoiceActivityVM.getCurrentOrderProductIndex() >= invoiceActivityVM.getOrders().size())
                        orderInvoiceAdapter.setData(invoiceActivityVM.getOrders());
                    else invoiceActivityVM.getProductById(invoiceActivityVM.getOrders().get(invoiceActivityVM.getCurrentOrderProductIndex()).getIdProduct());
                }
            }
        });

        btAddOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setActivityTouchable(false);

                Intent intent = new Intent(InvoiceActivity.this, AddOrderActivity.class);
                intent.putExtra("employee", employee);
                intent.putExtra("invoice", invoice);
                startActivityForResult(intent, ADD_ORDER_RESULT);
            }
        });

        btCloseInvoice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setActivityTouchable(false);
                invoice.setClose(Utils.getCurrentTime());
                invoice.setIdCloseEmployee(employee.getId());
                invoiceActivityVM.updateInvoice(invoice.getId(), invoice);
                Intent intent = new Intent(InvoiceActivity.this, PrintInvoice.class);
                intent.putExtra("idInvoice", invoice.getId());
                startActivity(intent);
            }
        });

        invoiceActivityVM.getUpdateLiveData().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean updated)
            {
                if(invoiceActivityVM.isTouchable())
                {
                    if(updated != null && updated)
                    {
                        assignComponentsInfo();
                    }
                }
                else
                {
                    if(updated == null || !updated)
                    {
                        Snackbar.make(btCloseInvoice, getString(R.string.toastCantUpdate), Snackbar.LENGTH_LONG).show();
                        setActivityLoading(false);
                    }
                    else
                    {
                        setResult(RESULT_OK);
                        finish();
                    }
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
        invoiceActivityVM.setLoading(toggle);
    }

    private void setActivityTouchable(boolean toggle)
    {
        invoiceActivityVM.setTouchable(toggle);
        Utils.toggleActivityTouchable(this, invoiceActivityVM.isTouchable());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_ORDER_RESULT)
        {
            setActivityTouchable(true);
            if(resultCode == RESULT_OK && data != null)
            {
                invoice.setTotal(invoice.getTotal() + (Float) data.getExtras().get("total"));
                invoiceActivityVM.updateInvoice(invoice.getId(), invoice);
                invoiceActivityVM.getByInvoice(invoice.getId());
                Snackbar.make(btCloseInvoice, getString(R.string.fabOrdersCreated), Snackbar.LENGTH_LONG).show();
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
