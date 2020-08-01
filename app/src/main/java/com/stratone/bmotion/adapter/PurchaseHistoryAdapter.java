package com.stratone.bmotion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stratone.bmotion.R;
import com.stratone.bmotion.model.PurchaseHistory;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class PurchaseHistoryAdapter extends ArrayAdapter<PurchaseHistory>
{
    private List<PurchaseHistory> list;
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

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        final PurchaseHistoryHolder holder;

        if (v == null) {
            LayoutInflater vi = ((AppCompatActivity) context).getLayoutInflater();
            v = vi.inflate(layout, parent, false);

            holder = new PurchaseHistoryHolder();
            holder.trxDate = v.findViewById(R.id.trxDate);
            holder.outlet = v.findViewById(R.id.outlet);
            holder.liter = v.findViewById(R.id.liter);

            v.setTag(holder);
        } else {
            holder = (PurchaseHistoryHolder) v.getTag();
        }

        final PurchaseHistory purchaseHistory = list.get(position);
        holder.trxDate.setText(purchaseHistory.getTransactionDate());
        holder.outlet.setText(purchaseHistory.getOutletNo());
        holder.liter.setText(purchaseHistory.getLiter());

        return v;
    }

    public PurchaseHistoryAdapter(Context context, int layout, List<PurchaseHistory> historyList) {
        super(context, layout, historyList);
        this.context = context;
        this.layout = layout;
        this.list = historyList;
    }

    static class PurchaseHistoryHolder {
        TextView trxDate, outlet, liter;
    }
}
