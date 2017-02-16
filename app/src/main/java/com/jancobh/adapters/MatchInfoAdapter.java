package com.jancobh.adapters;
/* Created by Janco.*/

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.jancobh.commons.Commons;
import com.jancobh.data.Summoner;
import com.jancobh.fragments.R;

import java.util.List;

public class MatchInfoAdapter extends ArrayAdapter<Summoner> {
    private Context mContext;
    private AQuery aq;

    public MatchInfoAdapter(Context context, int resource, List<Summoner> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.match_info_detail_listrow, parent, false);

            holder = new ViewHolder();
            holder.champImage = (ImageView)convertView.findViewById(R.id.champImage);
            holder.champName = (TextView)convertView.findViewById(R.id.champName);
            holder.userName = (TextView)convertView.findViewById(R.id.userName);
            holder.progress = (ProgressBar)convertView.findViewById(R.id.progress);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Summoner summoner = getItem(position);

        aq = new AQuery(holder.champImage);
        aq.progress(holder.progress).image("http://ddragon.leagueoflegends.com/cdn/" + Commons.LATEST_VERSION + "/img/champion/" + summoner.getKey() + ".png", true, true);
        holder.champName.setText(mContext.getString(R.string.chosenChampion) + " " + summoner.getChampName());
        holder.userName.setText(mContext.getString(R.string.player) + " " + summoner.getSummonerName());


        return convertView;
    }


    static class ViewHolder {
        public ImageView champImage;
        public TextView champName;
        public TextView userName;
        public ProgressBar progress;
    }
}
