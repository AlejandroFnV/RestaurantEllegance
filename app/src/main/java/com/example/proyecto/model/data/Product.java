package com.example.proyecto.model.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable
{
    private Long id;
    private String name;
    private Float price;
    private Integer destination;

    public Product(Long id, String name, Float price, Integer destination)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.destination = destination;
    }

    protected Product(Parcel in)
    {
        if(in.readByte() == 0) { id = null; }
        else { id = in.readLong(); }
        name = in.readString();
        if(in.readByte() == 0) { price = null; }
        else { price = in.readFloat(); }
        if(in.readByte() == 0) { destination = null; }
        else { destination = in.readInt(); }
    }

    public static final Creator<Product> CREATOR = new Creator<Product>()
    {
        @Override
        public Product createFromParcel(Parcel in)
        {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size)
        {
            return new Product[size];
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Float getPrice()
    {
        return price;
    }

    public void setPrice(Float price)
    {
        this.price = price;
    }

    public Integer getDestination()
    {
        return destination;
    }

    public void setDestination(Integer destination)
    {
        this.destination = destination;
    }

    @Override
    public String toString()
    {
        return "Product{" + "id=" + id + ", name='" + name + '\'' + ", price=" + price + ", destination=" + destination + '}';
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
        dest.writeString(name);
        if(price == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeFloat(price);
        }
        if(destination == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeInt(destination);
        }
    }
}
