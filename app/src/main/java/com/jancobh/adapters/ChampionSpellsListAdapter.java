package com.jancobh.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.jancobh.commons.Commons;
import com.jancobh.data.Spell;
import com.jancobh.fragments.ChampionAbilitiesVideosFragment;
import com.jancobh.fragments.R;

import java.util.List;

/* Created by Janco.*/

public class ChampionSpellsListAdapter extends ArrayAdapter<Spell> implements View.OnClickListener {

    private Context mContext;

    public ChampionSpellsListAdapter(Context context, int resource,
                                     List<Spell> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_abilities, parent,
                    false);

            holder = new ViewHolder();
            holder.spellImage = (ImageView) convertView
                    .findViewById(R.id.imageViewSpellImage);
            holder.spellTitle = (TextView) convertView
                    .findViewById(R.id.textViewSpellTitle);
            holder.spellBody = (TextView) convertView
                    .findViewById(R.id.textViewSpellBody);
            holder.spellKey = (TextView) convertView
                    .findViewById(R.id.textViewSpellKey);
            holder.playVideoButton = (ImageButton) convertView
                    .findViewById(R.id.imageButtonPlayVideo);

            holder.textViewVideo = (TextView) convertView.findViewById(R.id.textViewVideo);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.imageProgress);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Spell championSpell = getItem(position);
        assert championSpell != null;
        holder.spellTitle.setText(championSpell.getName());
        holder.spellBody.setText(championSpell.getSanitizedDescription());
        holder.spellKey.setText(championSpell.getSpellKey());
        AQuery aq = new AQuery(holder.spellImage);

        if(championSpell.getName() == null){
            Toast.makeText(mContext, "Problems to charge the Ability", Toast.LENGTH_SHORT).show();

        } else {
            if (championSpell.getName().contains(getContext().getResources().getString(R.string.passive))) {
                aq.progress(holder.progress).image(Commons.CHAMPION_PASSIVE_IMAGE_BASE_URL
                        + championSpell.getImage().getFull(), true, true);
            } else {
                aq.progress(holder.progress).image(Commons.CHAMPION_SPELL_IMAGE_BASE_URL
                        + championSpell.getImage().getFull(), true, true);
            }
        }
        holder.playVideoButton.setTag(position);
        holder.playVideoButton.setOnClickListener(this);
        holder.textViewVideo.setTag(position);
        holder.textViewVideo.setOnClickListener(this);

        return convertView;

    }

    private static class ViewHolder {
        ImageView spellImage;
        TextView spellTitle;
        TextView spellBody;
        TextView spellKey;
        ImageButton playVideoButton;
        TextView textViewVideo;
        ProgressBar progress;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageButtonPlayVideo || v.getId() == R.id.textViewVideo) {
            int key = (Integer) v.getTag();
            ChampionAbilitiesVideosFragment fragment = new ChampionAbilitiesVideosFragment();
            Bundle args = new Bundle();
            args.putInt(ChampionAbilitiesVideosFragment.EXTRA_PRESSED_KEY_POSITION, key);
            fragment.setArguments(args);
            fragment.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "");
        }

    }

}
