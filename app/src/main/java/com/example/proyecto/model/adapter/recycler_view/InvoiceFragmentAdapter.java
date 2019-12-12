package com.example.proyecto.model.adapter.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.R;
import com.example.proyecto.model.data.Invoice;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.List;

public class InvoiceFragmentAdapter extends RecyclerView.Adapter<InvoiceFragmentAdapter.ItemHolder>
{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Invoice> invoices;

    private OnInvoiceClickListener onInvoiceClickListener;
    private OnAddOrderClickListener onAddOrderClickListener;
    private OnCloseInvoiceClickListener onCloseInvoiceClickListener;

    public InvoiceFragmentAdapter(Context context)
    {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public InvoiceFragmentAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = layoutInflater.inflate(R.layout.item_active_invoice, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceFragmentAdapter.ItemHolder holder, int position)
    {
        final Invoice invoice = invoices.get(position);
        holder.tvInvoice.setText(context.getText(R.string.tvInvoice) + " " + invoice.getId());
        holder.tvTable.setText(context.getText(R.string.tvTable) + " " + invoice.getTable());
        holder.tvOpenTime.setText(invoice.getOpen());

        NumberFormat nfTotal = NumberFormat.getNumberInstance();
        String total_string = nfTotal.format(invoice.getTotal());
        holder.tvTotal.setText(context.getText(R.string.tvTotal) + " " + total_string + "€");

        holder.mcdUndeliveredOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(onInvoiceClickListener != null)
                    onInvoiceClickListener.onClick(invoice);
            }
        });

        holder.btAddOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(onAddOrderClickListener != null)
                    onAddOrderClickListener.onClick(invoice);
            }
        });

        holder.btCloseInvoice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(onCloseInvoiceClickListener != null)
                    onCloseInvoiceClickListener.onClick(invoice);

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
        private TextView tvInvoice, tvOpenTime, tvTable, tvTotal;
        private Button btAddOrder, btCloseInvoice;

        public ItemHolder(@NonNull View itemView)
        {
            super(itemView);
            mcdUndeliveredOrder = itemView.findViewById(R.id.mcdUndeliveredOrder);
            tvInvoice = itemView.findViewById(R.id.tvInvoice);
            tvOpenTime = itemView.findViewById(R.id.tvOpenTime);
            tvTable = itemView.findViewById(R.id.tvTable);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            btAddOrder = itemView.findViewById(R.id.btAddOrder);
            btCloseInvoice = itemView.findViewById(R.id.btCloseInvoice);
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

    public void setOnAddOrderClickListener(OnAddOrderClickListener onAddOrderClickListener)
    {
        this.onAddOrderClickListener = onAddOrderClickListener;
    }

    public void setOnCloseInvoiceClickListener(OnCloseInvoiceClickListener onCloseInvoiceClickListener)
    {
        this.onCloseInvoiceClickListener = onCloseInvoiceClickListener;
    }

    public interface OnInvoiceClickListener
    {
        void onClick(Invoice invoice);
    }

    public interface OnAddOrderClickListener
    {
        void onClick(Invoice invoice);
    }

    public interface OnCloseInvoiceClickListener
    {
        void onClick(Invoice invoice);
    }
}
