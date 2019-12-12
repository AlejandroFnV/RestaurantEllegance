package com.example.proyecto.model.view.navigation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proyecto.model.data.Invoice;
import com.example.proyecto.model.repository.InvoiceRepository;

import java.util.List;

public class InvoiceHistoryFragmentVM extends AndroidViewModel
{
    //View model control
    private boolean started;

    //Repository
    private InvoiceRepository invoiceRepository;

    //Fragment attrs
    private boolean loading;
    private boolean tvNoDataVisible;

    public InvoiceHistoryFragmentVM(@NonNull Application application)
    {
        super(application);
        invoiceRepository = new InvoiceRepository();
    }

    public boolean isStarted()
    {
        return started;
    }

    public void setStarted(boolean started)
    {
        this.started = started;
    }

    public boolean isLoading()
    {
        return loading;
    }

    public void setLoading(boolean loading)
    {
        this.loading = loading;
    }

    public boolean isTvNoDataVisible()
    {
        return tvNoDataVisible;
    }

    public void setTvNoDataVisible(boolean tvNoDataVisible)
    {
        this.tvNoDataVisible = tvNoDataVisible;
    }

    //Repository
    public void getInactiveInvoices()
    {
        invoiceRepository.getInactiveInvoices();
    }

    public LiveData<List<Invoice>> getGetInactiveInvoicesLiveData()
    {
        return invoiceRepository.getGetInactiveInvoicesLiveData();
    }
}
