package com.chandan.hospinfo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListViewAdapter2 extends ArrayAdapter<RowItem> {
    Context context;

    public CustomListViewAdapter2(Context context, int resourceId, List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }


    private class ViewHolder {
        TextView tv_p_name;
        TextView tv_d_name;
        TextView tv_problem;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CustomListViewAdapter2.ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_adapter2, null);
            holder = new CustomListViewAdapter2.ViewHolder();
            holder.tv_d_name = (TextView) convertView.findViewById(R.id.d_name_adapter);
            holder.tv_p_name = (TextView) convertView.findViewById(R.id.p_name_adapter);
            holder.tv_problem = (TextView) convertView.findViewById(R.id.problem_adapter);
            convertView.setTag(holder);
        } else
            holder = (CustomListViewAdapter2.ViewHolder) convertView.getTag();

        holder.tv_d_name.setText(rowItem.getDoctor());
        holder.tv_p_name.setText(rowItem.getPatient());
        holder.tv_problem.setText(rowItem.getProblem());
        return convertView;
    }
}
