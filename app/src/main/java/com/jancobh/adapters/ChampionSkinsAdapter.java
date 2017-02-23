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
import com.jancobh.commons.Commons;
import com.jancobh.data.Skin;
import com.jancobh.fragments.R;

import java.util.List;

public class ChampionSkinsAdapter extends ArrayAdapter<Skin> {
    private Context mContext;
    private String key;

    public ChampionSkinsAdapter(Context context, int resource, List<Skin> objects, String key) {
        super(context, resource, objects);
        this.mContext = context;
        this.key = key;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listrow_champion_skins, parent, false);

            holder = new ViewHolder();
            holder.skinImage = (ImageView)convertView.findViewById(R.id.championSkin);
            holder.skinName = (TextView)convertView.findViewById(R.id.skinName);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Skin skin = getItem(position);
        AQuery aQuery = new AQuery(holder.skinImage);
        ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.imageProgress);
        aQuery.id(R.id.championSkin).progress(progress).image(Commons.URL_CHAMPION_SKIN_BASE + key + "_" + position + ".jpg", true, true);

        assert skin != null;
        holder.skinName.setText(skin.getName());

        return convertView;
    }

    static class ViewHolder {
        ImageView skinImage;
        TextView skinName;
    }
}
