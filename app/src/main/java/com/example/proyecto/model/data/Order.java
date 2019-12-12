package com.example.proyecto.model.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable
{
    private Long id;
    private Long idInvoice;
    private Long idProduct;
    private Long idEmployee;
    private Integer units;
    private Float price;
    private Integer delivered;
    private transient String product_name;
    private transient Float product_price;

    public Order()
    {
    }

    protected Order(Parcel in)
    {
        if(in.readByte() == 0) { id = null; }
        else { id = in.readLong(); }
        if(in.readByte() == 0) { idInvoice = null; }
        else { idInvoice = in.readLong(); }
        if(in.readByte() == 0) { idProduct = null; }
        else { idProduct = in.readLong(); }
        if(in.readByte() == 0) { idEmployee = null; }
        else { idEmployee = in.readLong(); }
        if(in.readByte() == 0) { units = null; }
        else { units = in.readInt(); }
        if(in.readByte() == 0) { price = null; }
        else { price = in.readFloat(); }
        if(in.readByte() == 0) { delivered = null; }
        else { delivered = in.readInt(); }
    }

    public static final Creator<Order> CREATOR = new Creator<Order>()
    {
        @Override
        public Order createFromParcel(Parcel in)
        {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size)
        {
            return new Order[size];
        }
    };

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getIdInvoice()
    {
        return idInvoice;
    }

    public void setIdInvoice(Long idInvoice)
    {
        this.idInvoice = idInvoice;
    }

    public Long getIdProduct()
    {
        return idProduct;
    }

    public void setIdProduct(Long idProduct)
    {
        this.idProduct = idProduct;
    }

    public Long getIdEmployee()
    {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee)
    {
        this.idEmployee = idEmployee;
    }

    public Integer getUnits()
    {
        return units;
    }

    public void setUnits(Integer units)
    {
        this.units = units;
    }

    public Float getPrice()
    {
        return price;
    }

    public void setPrice(Float price)
    {
        this.price = price;
    }

    public Integer getDelivered()
    {
        return delivered;
    }

    public void setDelivered(Integer delivered)
    {
        this.delivered = delivered;
    }

    public String getProduct_name()
    {
        return product_name;
    }

    public void setProduct_name(String product_name)
    {
        this.product_name = product_name;
    }

    public Float getProduct_price()
    {
        return product_price;
    }

    public void setProduct_price(Float product_price)
    {
        this.product_price = product_price;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        if(id == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        if(idInvoice == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeLong(idInvoice);
        }
        if(idProduct == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeLong(idProduct);
        }
        if(idEmployee == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeLong(idEmployee);
        }
        if(units == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeInt(units);
        }
        if(price == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeFloat(price);
        }
        if(delivered == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeInt(delivered);
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", idInvoice=" + idInvoice +
                ", idProduct=" + idProduct +
                ", idEmployee=" + idEmployee +
                ", units=" + units +
                ", price=" + price +
                ", delivered=" + delivered +
                ", product_name='" + product_name + '\'' +
                ", product_price=" + product_price +
                '}';
    }
}
