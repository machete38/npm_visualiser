package com.example.npm_visualiser.utils;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.npm_visualiser.R;

public class CustomRvAdapter extends RecyclerView.Adapter<CustomVH> {

    TabObject object;
    public CustomRvAdapter(TabObject object) {
        this.object = object;
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new CustomVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, int position) {
        holder.name.setText(object.getList().get(position).getName()+"@"+object.getList().get(position).getVersion());
        if (object.getList().get(position).getList().size()!=0)
        {
            CustomRvAdapter rvAdapter = new CustomRvAdapter(object.getList().get(position));
            holder.rv.setAdapter(rvAdapter);
            holder.rv.addItemDecoration(new DividerItemDecoration(holder.rv.getContext(), DividerItemDecoration.HORIZONTAL));
            holder.chev.setVisibility(View.VISIBLE);
            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.rbottom.getVisibility() == View.GONE)
                    {
                        holder.rbottom.setVisibility(View.VISIBLE);
                        holder.chev.setImageDrawable(view.getResources().getDrawable(R.drawable.chevrone_down));
                    }
                    else
                    {
                        holder.rbottom.setVisibility(View.GONE);
                        holder.chev.setImageDrawable(view.getResources().getDrawable(R.drawable.chevrone_left));
                    }
                }
            });
        }
        else
        {
            holder.name.setTextColor(holder.itemView.getResources().getColor(R.color.orange));
            holder.chev.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return object.list.size();
    }
}
class CustomVH extends RecyclerView.ViewHolder{

    RelativeLayout rl, rbottom;
    RecyclerView rv;
    TextView name;
    ImageView chev;
    public CustomVH(@NonNull View itemView) {
        super(itemView);
        rbottom = itemView.findViewById(R.id.rv_rl_bottom);
        rl = itemView.findViewById(R.id.rl_rv_top);
        rv = itemView.findViewById(R.id.rv_bottomrv);
        name = itemView.findViewById(R.id.rv_name);
        chev = itemView.findViewById(R.id.rv_chev);
    }
}
