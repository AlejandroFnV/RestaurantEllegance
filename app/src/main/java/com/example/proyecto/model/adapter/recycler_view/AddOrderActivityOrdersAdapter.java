package com.example.proyecto.model.adapter.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.R;
import com.example.proyecto.model.data.Order;
import com.example.proyecto.model.view.AddOrderActivityVM;

import java.text.NumberFormat;
import java.util.List;

public class AddOrderActivityOrdersAdapter extends RecyclerView.Adapter<AddOrderActivityOrdersAdapter.ItemHolder>
{
    private LayoutInflater layoutInflater;
    private List<Order> orders;

    private OnRemoveUnitClickListener onRemoveUnitClickListener;
    private OnAddUnitClickListener onAddUnitClickListener;

    public AddOrderActivityOrdersAdapter(Context context)
    {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AddOrderActivityOrdersAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = layoutInflater.inflate(R.layout.item_add_order, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddOrderActivityOrdersAdapter.ItemHolder holder, int position)
    {
        final Order order = orders.get(position);
        holder.tvProductName.setText(order.getProduct_name());
        holder.tvUnits.setText(order.getUnits() + "");

        NumberFormat nfTotal = NumberFormat.getNumberInstance();
        String total_string = nfTotal.format(order.getPrice());
        holder.tvTotalPrice.setText(total_string + "€");

        holder.ivRemoveUnit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(onRemoveUnitClickListener != null)
                    onRemoveUnitClickListener.onClick(order);
            }
        });

        holder.ivAddUnit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(onAddUnitClickListener != null)
                    onAddUnitClickListener.onClick(order);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return orders == null ? 0 : orders.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder
    {
        private TextView tvProductName;
        private ImageView ivRemoveUnit;
        private TextView tvUnits;
        private ImageView ivAddUnit;
        private TextView tvTotalPrice;

        public ItemHolder(@NonNull View itemView)
        {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            ivRemoveUnit = itemView.findViewById(R.id.ivRemoveUnit);
            tvUnits = itemView.findViewById(R.id.tvUnits);
            ivAddUnit = itemView.findViewById(R.id.ivAddUnit);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }
    }

    public void setData(List<Order> orders)
    {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public void setOnRemoveUnitClickListener(OnRemoveUnitClickListener onRemoveUnitClickListener)
    {
        this.onRemoveUnitClickListener = onRemoveUnitClickListener;
    }

    public void setOnAddUnitClickListener(OnAddUnitClickListener onAddUnitClickListener)
    {
        this.onAddUnitClickListener = onAddUnitClickListener;
    }

    public interface OnRemoveUnitClickListener
    {
        void onClick(Order order);
    }

    public interface OnAddUnitClickListener
    {
        void onClick(Order order);
    }
}
