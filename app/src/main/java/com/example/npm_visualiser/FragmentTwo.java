package com.example.npm_visualiser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentTwo extends Fragment {
    int viewHeight;
    public static FragmentTwo newInstance() {
        return new FragmentTwo();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        viewHeight = view.getHeight();
        return view;
    }
    public int getHeight()
    {
        return viewHeight;
    }
}
