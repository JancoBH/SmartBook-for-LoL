package com.jancobh.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.jancobh.commons.Commons;
import com.jancobh.data.Items;
import com.jancobh.fragments.R;

import java.util.ArrayList;

/* Created by Janco.*/

public class GridViewItemsAdapter extends ArrayAdapter<Items> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Items> data = new ArrayList<>();

    public GridViewItemsAdapter(Context context, int layoutResourceId,
                                ArrayList<Items> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;


        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.itemImage = (ImageView) row.findViewById(R.id.itemImage);
            holder.progress = (ProgressBar) row.findViewById(R.id.imageProgress);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Items item = data.get(position);
        AQuery aq = new AQuery(holder.itemImage);
        aq.progress(holder.progress).image(Commons.ITEM_IMAGES_BASE_URL + String.valueOf(item.getId()) + ".png", true, true);
        return row;
    }

    private static class ViewHolder {
        ImageView itemImage;
        ProgressBar progress;
    }

}