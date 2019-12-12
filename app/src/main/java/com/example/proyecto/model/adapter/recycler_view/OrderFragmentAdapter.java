package com.example.proyecto.model.adapter.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto.R;
import com.example.proyecto.model.Utils;
import com.example.proyecto.model.data.Order;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class OrderFragmentAdapter extends RecyclerView.Adapter<OrderFragmentAdapter.ItemHolder>
{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Order> orders;

    private OnOrderClickListener onOrderClickListener;

    public OrderFragmentAdapter(Context context)
    {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OrderFragmentAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = layoutInflater.inflate(R.layout.item_undelivered_order, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderFragmentAdapter.ItemHolder holder, int position)
    {
        final Order order = orders.get(position);

        Glide
                .with(context)
                .load(Utils.getServerUrl() + "upload/images/products/" + order.getIdProduct() + ".jpg")
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_default_product)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.ivProduct);

        holder.tvOrderNum.setText(context.getText(R.string.tvOrderNum) + " " + order.getId());

        String product_name = order.getIdProduct() + "";
        if(order.getProduct_name() != null)
            product_name = order.getProduct_name();

        holder.tvProductTitle.setText(context.getText(R.string.tvProductTitle) + " " + product_name);

        holder.tvOrderUnits.setText(context.getText(R.string.tvOrderUnits) + " " + order.getUnits());
        holder.tvOrderInvoice.setText(context.getText(R.string.tvOrderInvoice) + " " + order.getIdInvoice());

        holder.mcdUndeliveredOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(onOrderClickListener != null)
                    onOrderClickListener.onClick(order);
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
        private MaterialCardView mcdUndeliveredOrder;
        private ImageView ivProduct;
        private TextView tvOrderNum, tvProductTitle, tvOrderUnits, tvOrderInvoice;

        public ItemHolder(@NonNull View itemView)
        {
            super(itemView);
            mcdUndeliveredOrder = itemView.findViewById(R.id.mcdUndeliveredOrder);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvOrderNum = itemView.findViewById(R.id.tvOrderNum);
            tvProductTitle = itemView.findViewById(R.id.tvProductTitle);
            tvOrderUnits = itemView.findViewById(R.id.tvOrderUnits);
            tvOrderInvoice = itemView.findViewById(R.id.tvOrderInvoice);
        }
    }

    public void setData(List<Order> orders)
    {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public void setOnOrderClickListener(OnOrderClickListener onOrderClickListener)
    {
        this.onOrderClickListener = onOrderClickListener;
    }

    public interface OnOrderClickListener
    {
        void onClick(Order order);
    }
}
