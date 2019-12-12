package com.example.proyecto.view.navigation;

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

import com.example.proyecto.R;
import com.example.proyecto.model.adapter.recycler_view.InvoiceHistoryFragmentAdapter;
import com.example.proyecto.model.data.Invoice;
import com.example.proyecto.model.view.navigation.InvoiceHistoryFragmentVM;
import com.example.proyecto.view.InactiveInvoiceActivity;
import com.example.proyecto.view.MainActivity;

import java.util.List;

public class InvoiceHistoryFragment extends Fragment
{
    //Static
    private static final int INVOICE_RESULT = 1;

    //Main
    private MainActivity mainActivity;

    //View model
    private InvoiceHistoryFragmentVM invoiceHistoryFragmentVM;
    private InvoiceHistoryFragmentAdapter invoiceHistoryFragmentAdapter;

    //Layout components
    private View root;
    private ProgressBar pbLoading;
    private ConstraintLayout clFragment;
    private TextView tvNoData;
    private SwipeRefreshLayout slInvoice;
    private RecyclerView rvInvoice;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mainActivity = (MainActivity) getActivity();
        root = inflater.inflate(R.layout.fragment_invoice_history, container, false);

        //////////////////////////////////////////////////////////////////////////

        initComponents();
        assignViewModel();

        if(!invoiceHistoryFragmentVM.isStarted())
        {
            invoiceHistoryFragmentVM.setLoading(true);
            refreshInactiveInvoices();
            invoiceHistoryFragmentVM.setTvNoDataVisible(true);
            invoiceHistoryFragmentVM.setStarted(true);
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
        slInvoice = root.findViewById(R.id.slInvoice);
        slInvoice.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimaryDark);
        rvInvoice = root.findViewById(R.id.rvInvoice);

        invoiceHistoryFragmentAdapter = new InvoiceHistoryFragmentAdapter(mainActivity);
        rvInvoice.setLayoutManager(new LinearLayoutManager(mainActivity));
        rvInvoice.setAdapter(invoiceHistoryFragmentAdapter);
    }

    private void assignViewModel()
    {
        invoiceHistoryFragmentVM = ViewModelProviders.of(this).get(InvoiceHistoryFragmentVM.class);
    }

    private void assignComponentsInfo()
    {
        setActivityLoading(invoiceHistoryFragmentVM.isLoading());
        tvNoData.setVisibility(invoiceHistoryFragmentVM.isTvNoDataVisible() ? View.VISIBLE : View.INVISIBLE);
    }

    private void assignEvents()
    {
        slInvoice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                refreshInactiveInvoices();
            }
        });

        invoiceHistoryFragmentVM.getGetInactiveInvoicesLiveData().observe(this, new Observer<List<Invoice>>()
        {
            @Override
            public void onChanged(List<Invoice> invoices)
            {
                if(invoices == null || invoices.size() == 0) invoiceHistoryFragmentVM.setTvNoDataVisible(true);
                else invoiceHistoryFragmentVM.setTvNoDataVisible(false);
                tvNoData.setVisibility(invoiceHistoryFragmentVM.isTvNoDataVisible() ? View.VISIBLE : View.INVISIBLE);
                invoiceHistoryFragmentAdapter.setData(invoices);
                slInvoice.setRefreshing(false);
                setActivityLoading(false);
            }
        });

        invoiceHistoryFragmentAdapter.setOnInvoiceClickListener(new InvoiceHistoryFragmentAdapter.OnInvoiceClickListener()
        {
            @Override
            public void onClick(Invoice invoice)
            {
                mainActivity.setActivityTouchable(false);
                Intent intent = new Intent(mainActivity, InactiveInvoiceActivity.class);
                intent.putExtra("invoice", invoice);
                startActivityForResult(intent, INVOICE_RESULT);
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
        invoiceHistoryFragmentVM.setLoading(toggle);
    }

    private void refreshInactiveInvoices()
    {
        mainActivity.setActivityTouchable(false);
        invoiceHistoryFragmentVM.getInactiveInvoices();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INVOICE_RESULT)
            mainActivity.setActivityTouchable(true);
    }
}
