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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.jancobh.commons.Commons;
import com.jancobh.data.Data;
import com.jancobh.data.Gold;
import com.jancobh.data.Image;
import com.jancobh.data.Item;
import com.jancobh.fragments.R;

import java.util.ArrayList;

public class ItemsAdapter extends ArrayAdapter<Item>{

    private Context mContext;
    private int layoutId;
    private ArrayList<Item> items;

    public ItemsAdapter(Context context, int resource, ArrayList<Item> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.layoutId = resource;
        this.items = objects;
    }

    @Override
    public int getCount() {
        if(items == null){
            return 0;
        }
        return items.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutId, parent, false);

            holder = new ViewHolder();
            holder.itemImage = (ImageView)convertView.findViewById(R.id.item_image);
            holder.textContainer = (RelativeLayout)convertView.findViewById(R.id.textContainer);
            holder.itemName = (TextView)convertView.findViewById(R.id.itemName);
            holder.shortDescription = (TextView)convertView.findViewById(R.id.shortDescription);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = getItem(position);
        assert item != null;
        Data itemData = item.getData();
        if(itemData != null) {
            String itemName = itemData.getName();
            if(itemName != null){
                holder.itemName.setText(itemName);
            }
            Gold gold = itemData.getGold();
            if(gold != null){
                int total = gold.getTotal();
                holder.shortDescription.setText(total + " " + mContext.getString(R.string.gold));
            }

            Image itemImage = itemData.getImage();
            if(itemImage != null){
                String imageUrl = itemImage.getFull();
                if(imageUrl != null){
                    imageUrl = Commons.ITEM_IMAGES_BASE_URL + imageUrl;
                    //  LolApplication.imageLoader.loadImage(imageUrl, null);
                    AQuery aq = new AQuery(holder.itemImage);
                    aq.image(imageUrl, true, true, 0, 0, null, android.R.anim.fade_in);
                }
            }


        }



        return convertView;
    }


    private static class ViewHolder{
        ImageView itemImage;
        RelativeLayout textContainer;
        TextView itemName;
        TextView shortDescription;
    }
}

