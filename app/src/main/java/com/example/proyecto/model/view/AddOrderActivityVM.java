package com.example.proyecto.model.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proyecto.model.data.Employee;
import com.example.proyecto.model.data.Invoice;
import com.example.proyecto.model.data.Order;
import com.example.proyecto.model.data.Product;
import com.example.proyecto.model.repository.OrderRepository;
import com.example.proyecto.model.repository.ProductRepository;

import java.util.ArrayList;

public class AddOrderActivityVM extends AndroidViewModel
{
    //View model control
    private boolean started;
    private boolean touchable;

    //Repository
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    //Other
    private ArrayList<Order> orders = new ArrayList<>();
    private int currentOrderIndex;

    //Activity attrs
    private boolean loading;
    private Employee employee;
    private Invoice invoice;
    private boolean tvProductsNoDataVisible;
    private boolean tvOrdersNoDataVisible;

    public AddOrderActivityVM(@NonNull Application application)
    {
        super(application);
        productRepository = new ProductRepository();
        orderRepository = new OrderRepository();
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

    public boolean isLoading()
    {
        return loading;
    }

    public void setLoading(boolean loading)
    {
        this.loading = loading;
    }

    public Employee getEmployee()
    {
        return employee;
    }

    public void setEmployee(Employee employee)
    {
        this.employee = employee;
    }

    public Invoice getInvoice()
    {
        return invoice;
    }

    public void setInvoice(Invoice invoice)
    {
        this.invoice = invoice;
    }

    public boolean isTvProductsNoDataVisible()
    {
        return tvProductsNoDataVisible;
    }

    public void setTvProductsNoDataVisible(boolean tvProductsNoDataVisible)
    {
        this.tvProductsNoDataVisible = tvProductsNoDataVisible;
    }

    public boolean isTvOrdersNoDataVisible()
    {
        return tvOrdersNoDataVisible;
    }

    public void setTvOrdersNoDataVisible(boolean tvOrdersNoDataVisible)
    {
        this.tvOrdersNoDataVisible = tvOrdersNoDataVisible;
    }

    public int getCurrentOrderIndex()
    {
        return currentOrderIndex;
    }

    public void setCurrentOrderIndex(int currentOrderIndex)
    {
        this.currentOrderIndex = currentOrderIndex;
    }

    public ArrayList<Order> getOrders()
    {
        return orders;
    }

    public void addOrder(Product product)
    {
        Order currentOrder = null;
        for(Order i : orders)
        {
            if(product.getId() == i.getIdProduct())
            {
                currentOrder = i;
                break;
            }
        }

        if(currentOrder == null)
        {
            Order order = new Order();
            order.setIdInvoice(invoice.getId());
            order.setIdProduct(product.getId());
            order.setIdEmployee(employee.getId());
            order.setUnits(1);
            order.setPrice(product.getPrice());
            order.setDelivered(0);
            order.setProduct_name(product.getName());
            order.setProduct_price(product.getPrice());
            orders.add(order);
        }
        else
        {
            currentOrder.setUnits(currentOrder.getUnits() + 1);
            currentOrder.setPrice(currentOrder.getPrice() + product.getPrice());
        }
    }

    public void addOrder(Order order)
    {
        for(Order i : orders)
        {
            if(order.getIdProduct() == i.getIdProduct())
            {
                i.setUnits(i.getUnits() + 1);
                i.setPrice(i.getPrice() + i.getProduct_price());
                break;
            }
        }
    }

    public void removeOrder(Order order)
    {
        for(Order i : orders)
        {
            if(order.getIdProduct() == i.getIdProduct())
            {
                i.setUnits(i.getUnits() - 1);
                i.setPrice(i.getPrice() - i.getProduct_price());
                if(i.getUnits() <= 0) orders.remove(i);
                break;
            }
        }
    }

    public float getTotalPrice()
    {
        float total = 0.0f;
        for(Order i : orders)
        {
            total += i.getPrice();
        }
        return total;
    }

    //Repository
    public void indexProducts()
    {
        productRepository.index();
    }

    public LiveData<ArrayList<Product>> getIndexProductsLiveData()
    {
        return productRepository.getIndexLiveData();
    }

    public void postOrder(Order order)
    {
        orderRepository.post(order);
    }

    public LiveData<Long> getPostOrderLiveData()
    {
        return orderRepository.getPostLiveData();
    }
}
