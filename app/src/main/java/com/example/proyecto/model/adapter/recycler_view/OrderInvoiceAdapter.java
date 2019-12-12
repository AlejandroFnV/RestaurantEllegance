package com.example.proyecto.model.adapter.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.R;
import com.example.proyecto.model.data.Order;

import java.text.NumberFormat;
import java.util.List;

public class OrderInvoiceAdapter extends RecyclerView.Adapter<OrderInvoiceAdapter.ItemHolder>
{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Order> orders;

    public OrderInvoiceAdapter(Context context)
    {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OrderInvoiceAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = layoutInflater.inflate(R.layout.item_order, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderInvoiceAdapter.ItemHolder holder, int position)
    {
        Order order = orders.get(position);
        holder.tvProductName.setText(order.getProduct_name() == null ? order.getIdProduct() + "" : order.getProduct_name());
        holder.tvDelivered.setText(order.getDelivered() == 0 ? context.getText(R.string.state_delivered_no) : context.getText(R.string.state_delivered_yes));
        holder.tvUnits.setText(order.getUnits() + "");

        NumberFormat nfTotal = NumberFormat.getNumberInstance();
        String price_string = nfTotal.format(order.getPrice());
        holder.tvTotal.setText(price_string + "€");
    }

    @Override
    public int getItemCount()
    {
        return orders == null ? 0 : orders.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder
    {
        private TextView tvProductName, tvDelivered, tvUnits, tvTotal;

        public ItemHolder(@NonNull View itemView)
        {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvDelivered = itemView.findViewById(R.id.tvDelivered);
            tvUnits = itemView.findViewById(R.id.tvUnits);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }

    public void setData(List<Order> orders)
    {
        this.orders = orders;
        notifyDataSetChanged();
    }
}
