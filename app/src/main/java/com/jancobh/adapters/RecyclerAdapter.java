package com.jancobh.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jancobh.fragments.R;
import com.jancobh.models.CardItemModel;

import java.util.List;

/* Created by JancoBH.*/
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<CardItemModel> cardItems;

    public RecyclerAdapter(List<CardItemModel> cardItems){
        this.cardItems = cardItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;
        ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView)itemView.findViewById(R.id.card_title);
            this.content = (TextView)itemView.findViewById(R.id.card_content);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(cardItems.get(position).title);
        holder.content.setText(cardItems.get(position).content);
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }
}
