package com.example.proyecto.view.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.proyecto.model.data.PrintInvoice;
import com.example.proyecto.view.AddInvoiceActivity;
import com.example.proyecto.model.Utils;
import com.example.proyecto.view.AddOrderActivity;
import com.example.proyecto.view.InvoiceActivity;
import com.example.proyecto.R;
import com.example.proyecto.model.adapter.recycler_view.InvoiceFragmentAdapter;
import com.example.proyecto.model.data.Invoice;
import com.example.proyecto.model.view.navigation.InvoiceFragmentVM;
import com.example.proyecto.view.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class InvoiceFragment extends Fragment
{
    //Static
    private static final int INVOICE_ACTIVITY_RESULT = 1;
    private static final int ADD_INVOICE_RESULT = 2;
    private static final int ADD_ORDER_RESULT = 3;

    //Main
    private MainActivity mainActivity;

    //View model
    private InvoiceFragmentVM invoiceFragmentVM;
    private InvoiceFragmentAdapter invoiceFragmentAdapter;

    //Layout components
    private View root;
    private ProgressBar pbLoading;
    private ConstraintLayout clFragment;
    private TextView tvNoData;
    private FloatingActionButton fabAdd;
    private SwipeRefreshLayout slInvoice;
    private RecyclerView rvInvoice;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mainActivity = (MainActivity) getActivity();
        root = inflater.inflate(R.layout.fragment_invoice, container, false);

        //////////////////////////////////////////////////////////////////////////
        
        initComponents();
        assignViewModel();

        if(!invoiceFragmentVM.isStarted())
        {
            invoiceFragmentVM.setLoading(true);
            refreshInvoices();
            invoiceFragmentVM.setTvNoDataVisible(true);
            invoiceFragmentVM.setStarted(true);
        }

        assignComponentsInfo();
        assignEvents();
        return root;
    }

    private void initComponents()
    {
        pbLoading = root.findViewById(R.id.pbLoading);
        clFragment = root.findViewById(R.id.clFragment);
        tvNoData = root.findViewById(R.id.tvNoData);
        fabAdd = root.findViewById(R.id.fabAdd);
        slInvoice = root.findViewById(R.id.slInvoice);
        slInvoice.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimaryDark);

        invoiceFragmentAdapter = new InvoiceFragmentAdapter(mainActivity);
        rvInvoice = root.findViewById(R.id.rvInvoice);
        rvInvoice.setLayoutManager(new LinearLayoutManager(mainActivity));
        rvInvoice.setAdapter(invoiceFragmentAdapter);
    }

    private void assignViewModel()
    {
        invoiceFragmentVM = ViewModelProviders.of(this).get(InvoiceFragmentVM.class);
    }

    private void assignComponentsInfo()
    {
        setActivityLoading(invoiceFragmentVM.isLoading());
        tvNoData.setVisibility(invoiceFragmentVM.isTvNoDataVisible() ? View.VISIBLE : View.INVISIBLE);
    }

    private void assignEvents()
    {
        slInvoice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                refreshInvoices();
            }
        });

        invoiceFragmentVM.getGetActiveInvoicesLiveData().observe(this, new Observer<List<Invoice>>()
        {
            @Override
            public void onChanged(List<Invoice> invoices)
            {
                if(invoices == null || invoices.size() == 0)
                {
                    invoiceFragmentVM.setTvNoDataVisible(true);
                    tvNoData.setVisibility(invoiceFragmentVM.isTvNoDataVisible() ? View.VISIBLE : View.INVISIBLE);
                }
                else
                {
                    invoiceFragmentVM.setTvNoDataVisible(false);
                    tvNoData.setVisibility(invoiceFragmentVM.isTvNoDataVisible() ? View.VISIBLE : View.INVISIBLE);
                }
                invoiceFragmentAdapter.setData(invoices);
                slInvoice.setRefreshing(false);
                setActivityLoading(false);
            }
        });

        invoiceFragmentAdapter.setOnInvoiceClickListener(new InvoiceFragmentAdapter.OnInvoiceClickListener()
        {
            @Override
            public void onClick(Invoice invoice)
            {
                mainActivity.setActivityTouchable(false);
                Intent intent = new Intent(mainActivity, InvoiceActivity.class);
                intent.putExtra("invoice", invoice);
                intent.putExtra("employee", mainActivity.getEmployee());
                startActivityForResult(intent, INVOICE_ACTIVITY_RESULT);
            }
        });

        invoiceFragmentAdapter.setOnAddOrderClickListener(new InvoiceFragmentAdapter.OnAddOrderClickListener()
        {
            @Override
            public void onClick(Invoice invoice)
            {
                mainActivity.setActivityTouchable(false);
                Intent intent = new Intent(mainActivity, AddOrderActivity.class);
                intent.putExtra("employee", mainActivity.getEmployee());
                intent.putExtra("invoice", invoice);
                startActivityForResult(intent, ADD_ORDER_RESULT);
            }
        });

        invoiceFragmentAdapter.setOnCloseInvoiceClickListener(new InvoiceFragmentAdapter.OnCloseInvoiceClickListener()
        {
            @Override
            public void onClick(Invoice invoice)
            {
                mainActivity.setActivityTouchable(false);
                invoice.setClose(Utils.getCurrentTime());
                invoice.setIdCloseEmployee(mainActivity.getEmployee().getId());
                invoiceFragmentVM.update(invoice.getId(), invoice);
                //Imprimimos la factura
                Intent intent = new Intent(getActivity(), PrintInvoice.class);
                intent.putExtra("idInvoice", invoice.getId());
                startActivity(intent);
            }
        });

        invoiceFragmentVM.getUpdateLiveData().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean updated)
            {
                if(mainActivity.isTouchable())
                {
                    if(updated != null && updated)
                        refreshInvoices();
                }
                else
                {
                    if(updated == null || !updated)
                    {
                        Snackbar.make(fabAdd, getString(R.string.toastUpdateFailed), Snackbar.LENGTH_LONG).show();
                        mainActivity.setActivityTouchable(true);
                    }
                    else
                    {
                        Snackbar.make(fabAdd, getString(R.string.toastInvoiceClosed), Snackbar.LENGTH_LONG).show();
                        refreshInvoices();
                    }
                }
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mainActivity.setActivityTouchable(false);
                Intent intent = new Intent(mainActivity, AddInvoiceActivity.class);
                intent.putExtra("employee", mainActivity.getEmployee());
                startActivityForResult(intent, ADD_INVOICE_RESULT);
            }
        });
    }

    private void setActivityLoading(boolean toggle)
    {
        if(toggle)
        {
            pbLoading.setVisibility(View.VISIBLE);
            clFragment.setVisibility(View.INVISIBLE);
        }
        else
        {
            pbLoading.setVisibility(View.INVISIBLE);
            clFragment.setVisibility(View.VISIBLE);
        }
        mainActivity.setActivityTouchable(!toggle);
        invoiceFragmentVM.setLoading(toggle);
    }

    private void refreshInvoices()
    {
        mainActivity.setActivityTouchable(false);
        invoiceFragmentVM.getActiveInvoices();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case INVOICE_ACTIVITY_RESULT:
            {
                mainActivity.setActivityTouchable(true);
                if(resultCode == Activity.RESULT_OK)
                {
                    Snackbar.make(fabAdd, getString(R.string.toastInvoiceClosed), Snackbar.LENGTH_LONG).show();
                }
                refreshInvoices();
                break;
            }
            case ADD_INVOICE_RESULT:
            {
                mainActivity.setActivityTouchable(true);
                if(resultCode == Activity.RESULT_OK)
                {
                    Snackbar.make(fabAdd, getString(R.string.toastInvoiceCreated), Snackbar.LENGTH_LONG).show();
                    refreshInvoices();
                }
                break;
            }
            case ADD_ORDER_RESULT:
            {
                mainActivity.setActivityTouchable(true);
                if(resultCode == Activity.RESULT_OK && data != null)
                {
                    Snackbar.make(fabAdd, getString(R.string.fabOrdersCreated), Snackbar.LENGTH_LONG).show();

                    Bundle bundle = data.getExtras();
                    Invoice invoice = (Invoice) bundle.get("invoice");
                    Float total = (Float) bundle.get("total");
                    invoice.setTotal(invoice.getTotal() + total);
                    invoiceFragmentVM.update(invoice.getId(), invoice);
                }
            }
        }
    }
}
