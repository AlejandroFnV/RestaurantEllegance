package com.example.proyecto.model.data;

import android.util.Log;

import java.util.ArrayList;

public class ProductList {

    private ArrayList<Product> listaProductos;

    public ProductList(){
        listaProductos = new ArrayList<>();
    }

    public void add(Product product){
        listaProductos.add(product);
    }

    public Product get(Long id){
        Log.v("ola", String.valueOf(id));
        Product product = null;
        for(Product producto : listaProductos){
            Log.v("ola", "ID : " + producto.getId());
            if (producto.getId() ==  id){
                product = producto;
            }
        }
        return product;
    }


    public int size(){
        return listaProductos.size();
    }

    public ArrayList<Product> getAll(){
        return this.listaProductos;
    }
}