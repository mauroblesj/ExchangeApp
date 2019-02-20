package com.example.exchangeapp.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.exchangeapp.R;
import com.example.exchangeapp.Bean.Rate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio Robles on 19/02/19.
 */

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.ItemViewHolder> {

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_value;
        private CardView cardView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txt_value = (TextView) itemView.findViewById(R.id.txt_value);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }

    private Context context;
    private List<Rate> arrayList;

    public RateAdapter(Context context, List<Rate> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rate, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.txt_value.setText(arrayList.get(position).getValue() + " " + arrayList.get(position).getCountry());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



}

