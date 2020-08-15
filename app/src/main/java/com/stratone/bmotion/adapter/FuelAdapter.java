package com.stratone.bmotion.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stratone.bmotion.R;
import com.stratone.bmotion.model.Fuel;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class FuelAdapter extends ArrayAdapter<Fuel>
{
    private List<Fuel> list;
    private Context context;
    private int layout;

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

            /*Fuel fuelList = list.get(i);*/
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        final FuelListHolder holder;

        if (v == null) {
            LayoutInflater vi = ((AppCompatActivity) context).getLayoutInflater();
            v = vi.inflate(layout, parent, false);

            holder = new FuelListHolder();
            holder.lnFuel = v.findViewById(R.id.lnFuelSubsidy);
            holder.tvFuelName = v.findViewById(R.id.tvFuelSubsidy);
            holder.edFuel = v.findViewById(R.id.edFuelSubsidy);
            holder.edFuel.setFilters(new InputFilter[] {
                    new InputFilter.LengthFilter(3)
            });

            Fuel fuelModel = list.get(position);
            holder.tvFuelName.setBackgroundColor(Color.parseColor(fuelModel.getBackgroundColor()));
            holder.tvFuelName.setTextColor(Color.parseColor(fuelModel.getTextColor()));

            v.setTag(holder);
        } else {
            holder = (FuelListHolder) v.getTag();
        }

        final Fuel fuelList = list.get(position);
        holder.tvFuelName.setText(fuelList.getName());
        holder.fuelid = fuelList.getFuelId();

        holder.edFuel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list.get(position).setLiter(Integer.parseInt(holder.edFuel.getText().toString().equals("")?"0":holder.edFuel.getText().toString()));
            }
        });
        return v;
    }

    public FuelAdapter(Context context, int layout, List<Fuel> fuelList) {
        super(context, layout, fuelList);
        this.context = context;
        this.layout = layout;
        this.list = fuelList;
    }

    static class FuelListHolder {
        TextView tvFuelName;
        LinearLayout lnFuel;
        EditText edFuel;
        int fuelid = 0;
    }
}
