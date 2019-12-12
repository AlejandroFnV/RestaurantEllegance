package com.example.proyecto.model.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Invoice implements Parcelable
{
    private Long id;
    private Long table;
    private String open;
    private Long idOpenEmployee;
    private String close;
    private Long idCloseEmployee;
    private Float total;

    public Invoice()
    {

    }

    protected Invoice(Parcel in)
    {
        if(in.readByte() == 0) { id = null; }
        else { id = in.readLong(); }
        if(in.readByte() == 0) { table = null; }
        else { table = in.readLong(); }
        open = in.readString();
        if(in.readByte() == 0) { idOpenEmployee = null; }
        else { idOpenEmployee = in.readLong(); }
        close = in.readString();
        if(in.readByte() == 0) { idCloseEmployee = null; }
        else { idCloseEmployee = in.readLong(); }
        if(in.readByte() == 0) { total = null; }
        else { total = in.readFloat(); }
    }

    public static final Creator<Invoice> CREATOR = new Creator<Invoice>()
    {
        @Override
        public Invoice createFromParcel(Parcel in)
        {
            return new Invoice(in);
        }

        @Override
        public Invoice[] newArray(int size)
        {
            return new Invoice[size];
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

    public Long getTable()
    {
        return table;
    }

    public void setTable(Long table)
    {
        this.table = table;
    }

    public String getOpen()
    {
        return open;
    }

    public void setOpen(String open)
    {
        this.open = open;
    }

    public Long getIdOpenEmployee()
    {
        return idOpenEmployee;
    }

    public void setIdOpenEmployee(Long idOpenEmployee)
    {
        this.idOpenEmployee = idOpenEmployee;
    }

    public String getClose()
    {
        return close;
    }

    public void setClose(String close)
    {
        this.close = close;
    }

    public Long getIdCloseEmployee()
    {
        return idCloseEmployee;
    }

    public void setIdCloseEmployee(Long idCloseEmployee)
    {
        this.idCloseEmployee = idCloseEmployee;
    }

    public Float getTotal()
    {
        return total;
    }

    public void setTotal(Float total)
    {
        this.total = total;
    }

    @Override
    public String toString()
    {
        return "Invoice{" + "id=" + id + ", table=" + table + ", open='" + open + '\'' + ", idOpenEmployee=" + idOpenEmployee + ", close='" + close + '\'' + ", idCloseEmployee=" + idCloseEmployee + ", total=" + total + '}';
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
        if(table == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeLong(table);
        }
        dest.writeString(open);
        if(idOpenEmployee == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeLong(idOpenEmployee);
        }
        dest.writeString(close);
        if(idCloseEmployee == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeLong(idCloseEmployee);
        }
        if(total == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeFloat(total);
        }
    }
}
