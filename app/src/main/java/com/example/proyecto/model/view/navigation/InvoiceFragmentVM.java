package com.example.proyecto.model.view.navigation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proyecto.model.data.Invoice;
import com.example.proyecto.model.repository.InvoiceRepository;

import java.util.List;

public class InvoiceFragmentVM extends AndroidViewModel
{
    //View model control
    private boolean started;

    //Repository
    private InvoiceRepository invoiceRepository;

    //Fragment attrs
    private boolean loading;
    private boolean tvNoDataVisible;

    public InvoiceFragmentVM(@NonNull Application application)
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

    public void post(Invoice invoice)
    {
        invoiceRepository.post(invoice);
    }

    public void getActiveInvoices()
    {
        invoiceRepository.getActiveInvoices();
    }

    public void update(Long id, Invoice invoice)
    {
        invoiceRepository.update(id, invoice);
    }

    public LiveData<Long> getPostLiveData()
    {
        return invoiceRepository.getPostLiveData();
    }

    public LiveData<List<Invoice>> getGetActiveInvoicesLiveData()
    {
        return invoiceRepository.getGetActiveInvoicesLiveData();
    }

    public LiveData<Boolean> getUpdateLiveData()
    {
        return invoiceRepository.getUpdateLiveData();
    }
}
