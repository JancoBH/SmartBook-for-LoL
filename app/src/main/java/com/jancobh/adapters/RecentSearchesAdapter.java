package com.jancobh.adapters;
/* Created by Janco.*/

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jancobh.commons.Commons;
import com.jancobh.data.RecentSearchItem;
import com.jancobh.fragments.R;
import com.jancobh.service.ServiceRequest;
import com.jancobh.view.FadeInNetworkImageView;

import java.util.List;

public class RecentSearchesAdapter extends ArrayAdapter<RecentSearchItem> {

    private Context mContext;
    private int resourceId;

    public RecentSearchesAdapter(Context context, int resource, List<RecentSearchItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(resourceId, parent, false);

            holder = new ViewHolder();
            holder.row = (RelativeLayout) convertView.findViewById(R.id.row);
            holder.summonerProfileIcon = (FadeInNetworkImageView)convertView.findViewById(R.id.summonerProfileIcon);
            holder.summonerName = (TextView)convertView.findViewById(R.id.summonerName);
            holder.summonerRegion = (TextView) convertView.findViewById(R.id.summonerRegion);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        RecentSearchItem item = getItem(position);

        assert item != null;
        if(item.getName() != null && item.getName().length() > 0){
            holder.summonerName.setText(item.getName());
        }else{
            holder.summonerName.setText("???");
        }

        if(item.getRegion() != null && item.getRegion().length() > 0){
            holder.summonerRegion.setText(mContext.getResources().getString(R.string.region) + " " + item.getRegion().toUpperCase());
        }
        holder.summonerProfileIcon.setImageUrl(Commons.PROFILE_ICON_BASE_URL + item.getProfileIconId() + ".png", ServiceRequest.getInstance(mContext).getImageLoader());

        return convertView;

    }


    static class ViewHolder {
        FadeInNetworkImageView summonerProfileIcon;
        TextView summonerName;
        TextView summonerRegion;
        RelativeLayout row;
    }

}
