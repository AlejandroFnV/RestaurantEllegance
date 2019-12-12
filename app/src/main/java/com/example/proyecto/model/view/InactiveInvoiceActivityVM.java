package com.example.proyecto.model.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proyecto.model.data.Employee;
import com.example.proyecto.model.data.Invoice;
import com.example.proyecto.model.data.Order;
import com.example.proyecto.model.data.Product;
import com.example.proyecto.model.repository.EmployeeRepository;
import com.example.proyecto.model.repository.InvoiceRepository;
import com.example.proyecto.model.repository.OrderRepository;
import com.example.proyecto.model.repository.ProductRepository;

import java.util.ArrayList;

public class InactiveInvoiceActivityVM extends AndroidViewModel
{
    //View model control
    private boolean started;
    private boolean touchable;

    //Repository
    private InvoiceRepository invoiceRepository;
    private EmployeeRepository employeeRepository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    //Other
    private boolean openEmployeeAdded;
    private Invoice invoice;
    private Employee openEmployee;
    private Employee closeEmployee;
    private ArrayList<Order> orders;
    private int currentOrderProductIndex;

    //Activity attrs
    private boolean loading;
    private boolean tvNoDataVisible;

    public InactiveInvoiceActivityVM(@NonNull Application application)
    {
        super(application);
        invoiceRepository = new InvoiceRepository();
        employeeRepository = new EmployeeRepository();
        orderRepository = new OrderRepository();
        productRepository = new ProductRepository();
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

    public boolean isOpenEmployeeAdded()
    {
        return openEmployeeAdded;
    }

    public void setOpenEmployeeAdded(boolean openEmployeeAdded)
    {
        this.openEmployeeAdded = openEmployeeAdded;
    }

    public Invoice getInvoice()
    {
        return invoice;
    }

    public void setInvoice(Invoice invoice)
    {
        this.invoice = invoice;
    }

    public Employee getOpenEmployee()
    {
        return openEmployee;
    }

    public void setOpenEmployee(Employee openEmployee)
    {
        this.openEmployee = openEmployee;
    }

    public Employee getCloseEmployee()
    {
        return closeEmployee;
    }

    public void setCloseEmployee(Employee closeEmployee)
    {
        this.closeEmployee = closeEmployee;
    }

    public ArrayList<Order> getOrders()
    {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders)
    {
        this.orders = orders;
    }

    public int getCurrentOrderProductIndex()
    {
        return currentOrderProductIndex;
    }

    public void setCurrentOrderProductIndex(int currentOrderProductIndex)
    {
        this.currentOrderProductIndex = currentOrderProductIndex;
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
    public void getEmployeeFromDB(Long id)
    {
        employeeRepository.get(id);
    }

    public void getByInvoice(Long id)
    {
        orderRepository.getByInvoice(id);
    }

    public void updateInvoice(Long id, Invoice invoice)
    {
        invoiceRepository.update(id, invoice);
    }

    public LiveData<Employee> getGetEmployeeLiveData()
    {
        return employeeRepository.getGetLiveData();
    }

    public LiveData<ArrayList<Order>> getGetByInvoiceLiveData()
    {
        return orderRepository.getGetByInvoiceLiveData();
    }

    public void getProductById(Long id)
    {
        productRepository.show(id);
    }

    public LiveData<Product> getProductShowLiveData()
    {
        return productRepository.getShowLiveData();
    }

    public void resolveOrdersProductsName()
    {
        if(orders.size() > 0)
        {
            currentOrderProductIndex = 0;
            getProductById(orders.get(0).getIdProduct());
        }
    }
}
