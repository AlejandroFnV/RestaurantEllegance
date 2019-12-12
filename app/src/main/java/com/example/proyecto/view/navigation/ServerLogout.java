package com.example.proyecto.view.navigation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyecto.view.MainActivity;

public class ServerLogout extends Fragment
{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.disconnect();
    }
}
