package com.example.npm_visualiser;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.npm_visualiser.utils.CustomRvAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentOne extends Fragment {

    RecyclerView recyclerView;
    NestedScrollView sw;
    int fragHeight;

    public static FragmentOne newInstance() {
        return new FragmentOne();
    }
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.fragment_one, container, false);
            recyclerView = view.findViewById(R.id.rv_fragment);
            sw = view.findViewById(R.id.sview);
            view.post(new Runnable() {
                @Override
                public void run() {
                    fragHeight = view.getMeasuredHeight();
                }
            });
            return view;
        }

    public void setRvAdapter(CustomRvAdapter s) {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(s);
    }
    public int getFragHeight(){return fragHeight;}


}
