package com.example.proyecto.model.view.navigation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proyecto.model.data.Order;
import com.example.proyecto.model.repository.OrderRepository;

import java.util.ArrayList;

public class OrderFragmentVM extends AndroidViewModel
{
    //View model control
    private boolean started;

    //Repository
    private OrderRepository orderRepository;

    //Fragment attrs
    private boolean loading;
    private boolean tvNoDataVisible;

    public OrderFragmentVM(@NonNull Application application)
    {
        super(application);
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
    public void getUndeliveredOrders()
    {
        orderRepository.getUndelivered();
    }

    public LiveData<ArrayList<Order>> getGetUndeliveredOrdersLiveData()
    {
        return orderRepository.getGetUndeliveredLiveData();
    }

    public void updateOrder(Long id, Order order)
    {
        orderRepository.update(id, order);
    }

    public LiveData<Boolean> getUpdateOrderLiveData()
    {
        return orderRepository.getUpdateLiveData();
    }
}
