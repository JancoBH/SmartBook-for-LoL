package com.jancobh.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.jancobh.data.Champion;
import com.jancobh.fragments.R;

import java.util.List;

/**
 * Created by Janco on 08-Feb-17.
 */

public class AllChampionsListAdapter extends ArrayAdapter<Champion> {

    private Context mContext;
    private AQuery listAq;

    public AllChampionsListAdapter(@NonNull Context context, @LayoutRes int resource, List<Champion> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listAq = new AQuery(this.mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_allchampions, parent, false);

            holder = new ViewHolder();
            holder.relativeLayoutTextContainer = (RelativeLayout)convertView.findViewById(R.id.relativeLayoutTextContainer);
            holder.championImage = (ImageView) convertView.findViewById(R.id.imageViewChampionImage);
            holder.rightArrow = (ImageView) convertView.findViewById(R.id.rightArrow);
            holder.championName = (TextView) convertView.findViewById(R.id.textViewChampionName);
            holder.champInfo = (TextView) convertView.findViewById(R.id.textViewChampInfo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Champion champion = getItem(position);
        holder.championName.setText(champion.getChampionName());

        listAq.id(holder.championImage).image(champion.getChampionImageUrl(), false, true, 0, 0, null, android.R.anim.fade_in);
        holder.champInfo.setText(champion.getTitle());
        Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/dinproregular.ttf");
        holder.championName.setTypeface(typeFace);
        holder.champInfo.setTypeface(typeFace);

        return convertView;
    }

    static class ViewHolder {
        public RelativeLayout relativeLayoutTextContainer;
        public ImageView championImage;
        public ImageView rightArrow;
        public TextView championName;
        public TextView champInfo;
    }
}
