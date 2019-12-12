package com.example.proyecto.model.adapter.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.R;
import com.example.proyecto.model.data.Invoice;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.List;

public class InvoiceHistoryFragmentAdapter extends RecyclerView.Adapter<InvoiceHistoryFragmentAdapter.ItemHolder>
{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Invoice> invoices;

    private OnInvoiceClickListener onInvoiceClickListener;

    public InvoiceHistoryFragmentAdapter(Context context)
    {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public InvoiceHistoryFragmentAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = layoutInflater.inflate(R.layout.item_inactive_invoice, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceHistoryFragmentAdapter.ItemHolder holder, int position)
    {
        final Invoice invoice = invoices.get(position);

        holder.tvInvoice.setText(context.getText(R.string.tvInvoice) + " " + invoice.getId());
        holder.tvInvoiceOpenTime.setText(context.getText(R.string.tvInvoiceOpenTime) + " " + invoice.getOpen());
        holder.tvInvoiceCloseTime.setText(context.getText(R.string.tvInvoiceCloseTime) + " " + invoice.getClose());

        NumberFormat nfTotal = NumberFormat.getNumberInstance();
        String total_string = nfTotal.format(invoice.getTotal());
        holder.tvInvoiceTotal.setText(context.getText(R.string.tvInvoiceTotal) + " " + total_string + "€");

        holder.mcdUndeliveredOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(onInvoiceClickListener != null)
                    onInvoiceClickListener.onClick(invoice);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return invoices == null ? 0 : invoices.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder
    {
        private MaterialCardView mcdUndeliveredOrder;
        private TextView tvInvoice, tvInvoiceOpenTime, tvInvoiceCloseTime, tvInvoiceTotal;

        public ItemHolder(@NonNull View itemView)
        {
            super(itemView);
            mcdUndeliveredOrder = itemView.findViewById(R.id.mcdUndeliveredOrder);
            tvInvoice = itemView.findViewById(R.id.tvInvoice);
            tvInvoiceOpenTime = itemView.findViewById(R.id.tvInvoiceOpenTime);
            tvInvoiceCloseTime = itemView.findViewById(R.id.tvInvoiceCloseTime);
            tvInvoiceTotal = itemView.findViewById(R.id.tvInvoiceTotal);
        }
    }

    public void setData(List<Invoice> invoices)
    {
        this.invoices = invoices;
        notifyDataSetChanged();
    }

    public void setOnInvoiceClickListener(OnInvoiceClickListener onInvoiceClickListener)
    {
        this.onInvoiceClickListener = onInvoiceClickListener;
    }

    public interface OnInvoiceClickListener
    {
        void onClick(Invoice invoice);
    }
}
