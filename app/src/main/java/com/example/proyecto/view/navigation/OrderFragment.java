package com.example.proyecto.view.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.proyecto.R;
import com.example.proyecto.model.adapter.recycler_view.OrderFragmentAdapter;
import com.example.proyecto.model.data.Order;
import com.example.proyecto.model.view.navigation.OrderFragmentVM;
import com.example.proyecto.view.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class OrderFragment extends Fragment
{
    //Main
    private MainActivity mainActivity;

    //View model
    private OrderFragmentVM orderFragmentVM;
    private OrderFragmentAdapter orderFragmentAdapter;

    //Layout components
    private View root;
    private ProgressBar pbLoading;
    private ConstraintLayout clFragment;
    private TextView tvNoData;
    private SwipeRefreshLayout slOrder;
    private RecyclerView rvOrder;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mainActivity = (MainActivity) getActivity();
        root = inflater.inflate(R.layout.fragment_order, container, false);

        //////////////////////////////////////////////////////////////////////////

        initComponents();
        assignViewModel();

        if(!orderFragmentVM.isStarted())
        {
            orderFragmentVM.setLoading(true);
            refreshUndeliveredOrders();
            orderFragmentVM.setTvNoDataVisible(true);
            orderFragmentVM.setStarted(true);
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
        slOrder = root.findViewById(R.id.slOrder);
        slOrder.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimaryDark);
        rvOrder = root.findViewById(R.id.rvOrder);

        orderFragmentAdapter = new OrderFragmentAdapter(mainActivity);
        rvOrder.setLayoutManager(new LinearLayoutManager(mainActivity));
        rvOrder.setAdapter(orderFragmentAdapter);
    }

    private void assignViewModel()
    {
        orderFragmentVM = ViewModelProviders.of(this).get(OrderFragmentVM.class);
    }

    private void assignComponentsInfo()
    {
        setActivityLoading(orderFragmentVM.isLoading());
        tvNoData.setVisibility(orderFragmentVM.isTvNoDataVisible() ? View.VISIBLE : View.INVISIBLE);
    }

    private void assignEvents()
    {
        slOrder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                refreshUndeliveredOrders();
            }
        });

        orderFragmentVM.getGetUndeliveredOrdersLiveData().observe(this, new Observer<ArrayList<Order>>()
        {
            @Override
            public void onChanged(ArrayList<Order> orders)
            {
                if(orders == null || orders.size() == 0) orderFragmentVM.setTvNoDataVisible(true);
                else orderFragmentVM.setTvNoDataVisible(false);
                tvNoData.setVisibility(orderFragmentVM.isTvNoDataVisible() ? View.VISIBLE : View.INVISIBLE);
                orderFragmentAdapter.setData(orders);
                slOrder.setRefreshing(false);
                setActivityLoading(false);
            }
        });

        orderFragmentAdapter.setOnOrderClickListener(new OrderFragmentAdapter.OnOrderClickListener()
        {
            @Override
            public void onClick(Order order)
            {
                mainActivity.setActivityTouchable(false);
                order.setDelivered(1);
                orderFragmentVM.updateOrder(order.getId(), order);
            }
        });

        orderFragmentVM.getUpdateOrderLiveData().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean updated)
            {
                if(updated == null || !updated)
                {
                    Snackbar.make(rvOrder, getString(R.string.toastOrderNotUpdated), Snackbar.LENGTH_LONG).show();
                    mainActivity.setActivityTouchable(true);
                }
                else
                {
                    Snackbar.make(rvOrder, getString(R.string.toastOrderUpdated), Snackbar.LENGTH_LONG).show();
                    refreshUndeliveredOrders();
                }
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
        orderFragmentVM.setLoading(toggle);
    }

    private void refreshUndeliveredOrders()
    {
        mainActivity.setActivityTouchable(false);
        orderFragmentVM.getUndeliveredOrders();
    }
}
