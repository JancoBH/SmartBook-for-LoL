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
import com.jancobh.data.Rune;
import com.jancobh.fragments.R;

import java.util.List;

public class RuneAdapter extends ArrayAdapter<Rune> {
    private Context mContext;

    public RuneAdapter(Context context, int resource, List<Rune> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.rune_row, parent, false);

            holder = new ViewHolder();
            holder.runeImage = (ImageView)convertView.findViewById(R.id.imageViewChampionImage);
            holder.runeName = (TextView)convertView.findViewById(R.id.textViewChampionName);
            holder.runeSanitizedDescription = (TextView)convertView.findViewById(R.id.textViewDateInterval);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.imageProgress);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Rune rune = getItem(position);
        assert rune != null;
        holder.runeName.setText(rune.getName());
        AQuery aq = new AQuery(holder.runeImage);
        aq.progress(holder.progress).image(Commons.RUNES_IMAGES_BASE_URL + rune.getImageUrl(), true, true);
        holder.runeSanitizedDescription.setText(rune.getSanitizedDescription());

        return convertView;
    }

    static class ViewHolder {
        ImageView runeImage;
        TextView runeName;
        TextView runeSanitizedDescription;
        public ProgressBar progress;
    }
}
