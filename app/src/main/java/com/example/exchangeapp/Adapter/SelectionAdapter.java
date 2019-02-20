package com.example.exchangeapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.exchangeapp.Bean.Rate;
import com.example.exchangeapp.Database.RealmHelper;
import com.example.exchangeapp.Preferences.SharedPreferences;
import com.example.exchangeapp.R;

import java.util.ArrayList;
import java.util.List;

public class SelectionAdapter extends ArrayAdapter<Rate>{

    public List<Rate> dataSet;
    Context mContext;
    RealmHelper realmHelper;
    SharedPreferences sharedPreferences;

    // View lookup cache
    private static class ViewHolder {
        TextView txtCountry;
        CheckBox checkBox;
    }

    public SelectionAdapter(List<Rate> data, Context context) {
        super(context, R.layout.list_item, data);
        this.dataSet = data;
        this.mContext=context;
        sharedPreferences = new SharedPreferences(mContext);
        realmHelper = new RealmHelper();
    }

    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Rate rate = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.txtCountry = (TextView) convertView.findViewById(R.id.tv_select_country);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.txtCountry.setText(rate.getCountry());
        viewHolder.checkBox.setChecked(rate.isSelected());
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = !dataSet.get(position).isSelected();
                realmHelper.updateSelected(dataSet.get(position).getCountry(),selected);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}