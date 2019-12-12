package com.example.proyecto.view;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto.R;
import com.example.proyecto.model.Utils;
import com.example.proyecto.model.adapter.recycler_view.AddOrderActivityOrdersAdapter;
import com.example.proyecto.model.adapter.recycler_view.AddOrderActivityProductsAdapter;
import com.example.proyecto.model.data.Employee;
import com.example.proyecto.model.data.Invoice;
import com.example.proyecto.model.data.Order;
import com.example.proyecto.model.data.Product;
import com.example.proyecto.model.view.AddOrderActivityVM;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class AddOrderActivity extends AppCompatActivity
{
    //View model
    private AddOrderActivityVM addOrderActivityVM;
    private AddOrderActivityProductsAdapter addOrderActivityProductsAdapter;
    private AddOrderActivityOrdersAdapter addOrderActivityOrdersAdapter;

    //Layout components
    private ProgressBar pbLoading;
    private ConstraintLayout clActivity;
    private TextView tvProductsNoData, tvOrdersNoData;
    private RecyclerView rvProducts, rvOrders;
    private TextView tvTotalOrderPrice;
    private Button btAddOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //////////////////////////////////////////////////////////////////////////

        initComponents();
        assignViewModel();

        if(!addOrderActivityVM.isStarted())
        {
            Bundle bundle = getIntent().getExtras();
            Employee employee = (Employee) bundle.get("employee");
            Invoice invoice = (Invoice) bundle.get("invoice");
            if((employee == null || employee.getId() <= 0) || (invoice == null || invoice.getId() <= 0)) finish();
            else
            {
                addOrderActivityVM.setEmployee(employee);
                addOrderActivityVM.setInvoice(invoice);
                addOrderActivityVM.setTvProductsNoDataVisible(false);
                addOrderActivityVM.setTvOrdersNoDataVisible(true);
                setActivityLoading(true);
                addOrderActivityVM.indexProducts();
                addOrderActivityVM.setStarted(true);
            }
        }

        assignComponentsInfo();
        assignEvents();
    }

    private void initComponents()
    {
        pbLoading = findViewById(R.id.pbLoading);
        clActivity = findViewById(R.id.clActivity);
        tvProductsNoData = findViewById(R.id.tvProductsNoData);
        tvOrdersNoData = findViewById(R.id.tvOrdersNoData);
        rvProducts = findViewById(R.id.rvProducts);
        rvOrders = findViewById(R.id.rvOrders);
        tvTotalOrderPrice = findViewById(R.id.tvTotalOrderPrice);
        btAddOrders = findViewById(R.id.btAddOrders);

        addOrderActivityProductsAdapter = new AddOrderActivityProductsAdapter(this);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        rvProducts.setAdapter(addOrderActivityProductsAdapter);

        addOrderActivityOrdersAdapter = new AddOrderActivityOrdersAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvOrders.setLayoutManager(linearLayoutManager);
        rvOrders.setAdapter(addOrderActivityOrdersAdapter);
    }

    private void assignViewModel()
    {
        addOrderActivityVM = ViewModelProviders.of(this).get(AddOrderActivityVM.class);
    }

    private void assignComponentsInfo()
    {
        setActivityTouchable(addOrderActivityVM.isTouchable());
        setActivityLoading(addOrderActivityVM.isLoading());
        tvProductsNoData.setVisibility(addOrderActivityVM.isTvProductsNoDataVisible() ? View.VISIBLE : View.INVISIBLE);
        tvOrdersNoData.setVisibility(addOrderActivityVM.isTvOrdersNoDataVisible() ? View.VISIBLE : View.INVISIBLE);
        addOrderActivityOrdersAdapter.setData(addOrderActivityVM.getOrders());
        setTotalPrice();
    }

    private void assignEvents()
    {
        addOrderActivityVM.getIndexProductsLiveData().observe(this, new Observer<ArrayList<Product>>()
        {
            @Override
            public void onChanged(ArrayList<Product> products)
            {
                if(products == null || products.size() <= 0) addOrderActivityVM.setTvProductsNoDataVisible(true);
                else addOrderActivityProductsAdapter.setData(products);
                addOrderActivityVM.setLoading(false);
                assignComponentsInfo();
            }
        });

        addOrderActivityProductsAdapter.setOnProductClickListener(new AddOrderActivityProductsAdapter.OnProductClickListener()
        {
            @Override
            public void onClick(Product product)
            {
                addOrderActivityVM.addOrder(product);
                addOrderActivityVM.setTvOrdersNoDataVisible(false);
                tvOrdersNoData.setVisibility(addOrderActivityVM.isTvOrdersNoDataVisible() ? View.VISIBLE : View.INVISIBLE);
                setTotalPrice();
                addOrderActivityOrdersAdapter.setData(addOrderActivityVM.getOrders());
            }
        });

        addOrderActivityOrdersAdapter.setOnRemoveUnitClickListener(new AddOrderActivityOrdersAdapter.OnRemoveUnitClickListener()
        {
            @Override
            public void onClick(Order order)
            {
                addOrderActivityVM.removeOrder(order);
                tvOrdersNoData.setVisibility(addOrderActivityVM.isTvOrdersNoDataVisible() ? View.VISIBLE : View.INVISIBLE);
                setTotalPrice();
                addOrderActivityOrdersAdapter.setData(addOrderActivityVM.getOrders());
            }
        });

        addOrderActivityOrdersAdapter.setOnAddUnitClickListener(new AddOrderActivityOrdersAdapter.OnAddUnitClickListener()
        {
            @Override
            public void onClick(Order order)
            {
                addOrderActivityVM.addOrder(order);
                addOrderActivityVM.setTvOrdersNoDataVisible(false);
                tvOrdersNoData.setVisibility(addOrderActivityVM.isTvOrdersNoDataVisible() ? View.VISIBLE : View.INVISIBLE);
                setTotalPrice();
                addOrderActivityOrdersAdapter.setData(addOrderActivityVM.getOrders());
            }
        });

        btAddOrders.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setActivityLoading(true);
                if(addOrderActivityVM.getOrders().size() > 0)
                {
                    addOrderActivityVM.setCurrentOrderIndex(0);
                    addOrderActivityVM.postOrder(addOrderActivityVM.getOrders().get(addOrderActivityVM.getCurrentOrderIndex()));
                }
                else
                {
                    Snackbar.make(btAddOrders, getString(R.string.tvNoData), Snackbar.LENGTH_LONG).show();
                    setActivityLoading(false);
                }
            }
        });

        addOrderActivityVM.getPostOrderLiveData().observe(this, new Observer<Long>()
        {
            @Override
            public void onChanged(Long id)
            {
                addOrderActivityVM.setCurrentOrderIndex(addOrderActivityVM.getCurrentOrderIndex() + 1);
                if(addOrderActivityVM.getCurrentOrderIndex() >= addOrderActivityVM.getOrders().size())
                {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("invoice", addOrderActivityVM.getInvoice());
                    resultIntent.putExtra("total", addOrderActivityVM.getTotalPrice());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
                else addOrderActivityVM.postOrder(addOrderActivityVM.getOrders().get(addOrderActivityVM.getCurrentOrderIndex()));
            }
        });
    }

    private void setTotalPrice()
    {
        float total_price = addOrderActivityVM.getTotalPrice();
        NumberFormat nfTotal = NumberFormat.getNumberInstance();
        String total_string = nfTotal.format(total_price);

        tvTotalOrderPrice.setText(getText(R.string.tvTotalOrderPrice) + " " + total_string + "€");
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
        addOrderActivityVM.setLoading(toggle);
    }

    private void setActivityTouchable(boolean toggle)
    {
        addOrderActivityVM.setTouchable(toggle);
        Utils.toggleActivityTouchable(this, addOrderActivityVM.isTouchable());
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
