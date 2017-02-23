package com.jancobh.adapters;
/* Created by Janco.*/

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.jancobh.data.Champion;
import com.jancobh.fragments.R;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Champion> {

    private Context mContext;

    public ListAdapter(Context context, int resource, List<Champion> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.recycler_item, parent, false);

            holder = new ViewHolder();
            holder.championImage = (ImageView)convertView.findViewById(R.id.imageViewChampionImage);
            holder.championName = (TextView)convertView.findViewById(R.id.textViewChampionName);
            holder.dateInterval = (TextView)convertView.findViewById(R.id.textViewDateInterval);
            holder.rpIcon = (ImageView)convertView.findViewById(R.id.imageViewRp);
            holder.ipIcon = (ImageView)convertView.findViewById(R.id.imageViewIp);
            holder.rpPrice = (TextView)convertView.findViewById(R.id.textViewRp);
            holder.ipPrice = (TextView)convertView.findViewById(R.id.textViewIp);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.imageProgress);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Champion champion = getItem(position);
        assert champion != null;
        holder.championName.setText(champion.getChampionName());
        AQuery aq = new AQuery(holder.championImage);
        aq.progress(holder.progress).image(champion.getChampionImageUrl(), true, true, 0, 0, null, android.R.anim.fade_in);
        holder.dateInterval.setText(champion.getDateInterval());
        if(champion.getChampionRp() != null && champion.getChampionRp().length() > 0){
            holder.rpPrice.setText(champion.getChampionRp());
        }
        if(champion.getChampionIp() != null && champion.getChampionIp().length() > 0){
            holder.ipPrice.setText(champion.getChampionIp());
        }



        return convertView;

    }

    static class ViewHolder {
        ImageView championImage;
        TextView championName;
        TextView dateInterval;
        ImageView rpIcon;
        ImageView ipIcon;
        TextView rpPrice;
        TextView ipPrice;
        public ProgressBar progress;
    }

}