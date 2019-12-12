package com.example.proyecto.model.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proyecto.model.data.Employee;
import com.example.proyecto.model.data.Invoice;
import com.example.proyecto.model.repository.InvoiceRepository;

import android.os.Handler;

public class AddInvoiceActivityVM extends AndroidViewModel
{
    //View model control
    private boolean started;
    private boolean touchable;

    //Repository
    private InvoiceRepository invoiceRepository;

    //Activity attrs
    private Employee employee;
    private Long longTable;
    private Handler handler;
    private boolean loading;
    private boolean tvErrorMessageVisible;
    private String table;

    public AddInvoiceActivityVM(@NonNull Application application)
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

    public boolean isTouchable()
    {
        return touchable;
    }

    public void setTouchable(boolean touchable)
    {
        this.touchable = touchable;
    }

    public Employee getEmployee()
    {
        return employee;
    }

    public void setEmployee(Employee employee)
    {
        this.employee = employee;
    }

    public Long getLongTable()
    {
        return longTable;
    }

    public void setLongTable(Long longTable)
    {
        this.longTable = longTable;
    }

    public Handler getHandler()
    {
        return handler;
    }

    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }

    public boolean isLoading()
    {
        return loading;
    }

    public void setLoading(boolean loading)
    {
        this.loading = loading;
    }

    public boolean isTvErrorMessageVisible()
    {
        return tvErrorMessageVisible;
    }

    public void setTvErrorMessageVisible(boolean tvErrorMessageVisible)
    {
        this.tvErrorMessageVisible = tvErrorMessageVisible;
    }

    public String getTable()
    {
        return table;
    }

    public void setTable(String table)
    {
        this.table = table;
    }

    public void isTableInUse(Long table)
    {
        invoiceRepository.isTableInUse(table);
    }

    public LiveData<Boolean> getIsTableInUseLiveData()
    {
        return invoiceRepository.getIsTableInUseLiveData();
    }

    public void postInvoice(Invoice invoice)
    {
        invoiceRepository.post(invoice);
    }

    public LiveData<Long> getPostInvoiceLiveData()
    {
        return invoiceRepository.getPostLiveData();
    }
}
