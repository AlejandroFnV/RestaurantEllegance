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
import com.example.proyecto.model.data.Product;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.List;

public class AddOrderActivityProductsAdapter extends RecyclerView.Adapter<AddOrderActivityProductsAdapter.ItemHolder>
{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Product> products;

    private OnProductClickListener onProductClickListener;

    public AddOrderActivityProductsAdapter(Context context)
    {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AddOrderActivityProductsAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = layoutInflater.inflate(R.layout.item_product, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddOrderActivityProductsAdapter.ItemHolder holder, int position)
    {
        final Product product = products.get(position);

        Glide
                .with(context)
                .load(Utils.getServerUrl() + "upload/images/products/" + product.getId() + ".jpg")
                .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_default_product)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.ivProduct);

        holder.tvProductName.setText(product.getName());

        NumberFormat nfTotal = NumberFormat.getNumberInstance();
        String price_string = nfTotal.format(product.getPrice());
        holder.tvProductPrice.setText(price_string + "€");

        holder.mcdProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(onProductClickListener != null)
                    onProductClickListener.onClick(product);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return products == null ? 0 : products.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder
    {
        private MaterialCardView mcdProduct;
        private ImageView ivProduct;
        private TextView tvProductName, tvProductPrice;

        public ItemHolder(@NonNull View itemView)
        {
            super(itemView);
            mcdProduct = itemView.findViewById(R.id.mcdProduct);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }

    public void setData(List<Product> products)
    {
        this.products = products;
        notifyDataSetChanged();
    }

    public void setOnProductClickListener(OnProductClickListener onProductClickListener)
    {
        this.onProductClickListener = onProductClickListener;
    }

    public interface OnProductClickListener
    {
        void onClick(Product product);
    }
}
