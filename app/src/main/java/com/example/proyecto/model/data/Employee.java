package com.example.proyecto.model.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Employee implements Parcelable
{
    private Long id;
    private String login, password;

    public Employee(Long id, String username, String password)
    {
        this.id = id;
        this.login = username;
        this.password = password;
    }

    protected Employee(Parcel in)
    {
        if(in.readByte() == 0) { id = null; }
        else { id = in.readLong(); }
        login = in.readString();
        password = in.readString();
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>()
    {
        @Override
        public Employee createFromParcel(Parcel in)
        {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size)
        {
            return new Employee[size];
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

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public String toString()
    {
        return "Employee{" + "id=" + id + ", login='" + login + '\'' + ", password='" + password + '\'' + '}';
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
        dest.writeString(login);
        dest.writeString(password);
    }
}
