package com.jancobh.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.jancobh.commons.Commons;
import com.jancobh.listener.ResponseListener;
import com.jancobh.responseclasses.ItemDetailResponse;
import com.jancobh.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemDetailFragment extends Fragment implements ResponseListener {
    public static final String EXTRA_ITEM_ID = "com.jancobh.fragments.ItemDetailFragment.extraitemid";
    public static final String EXTRA_ITEM_IMAGE_URL = "com.jancobh.fragments.ItemDetailFragment.extraitemimageurl";
    private int itemId;
    private String itemImageUrl;
    private ImageView itemImage;
    private TextView itemName, itemGold, itemDescription, descriptionTitle;
    private ProgressBar progress;
    private AQuery aq;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_detail, container, false);
        initUI(v);
        getExtras();

        ArrayList<String> pathParams = new ArrayList<>();
        pathParams.add("static-data");
        pathParams.add(Commons.getRegion());
        pathParams.add("v1.2");
        pathParams.add("item");
        pathParams.add(String.valueOf(itemId));
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("locale", Commons.getLocale());
        queryParams.put("version", Commons.LATEST_VERSION);
        queryParams.put("itemData", "gold,sanitizedDescription");
        queryParams.put("api_key", Commons.API_KEY);

        ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.ITEM_DETAIL_REQUEST, pathParams, queryParams, null, this);

        return v;
    }

    private void getExtras(){
        Bundle args = getArguments();
        itemId = args.getInt(EXTRA_ITEM_ID);
        itemImageUrl = args.getString(EXTRA_ITEM_IMAGE_URL);
    }

    private void initUI(View v){
        itemImage = (ImageView)v.findViewById(R.id.imageViewItemImage);
        itemGold = (TextView)v.findViewById(R.id.textViewItemGold);
        itemDescription = (TextView)v.findViewById(R.id.textViewDetailedDescription);
        itemName = (TextView)v.findViewById(R.id.textViewItemName);
        descriptionTitle = (TextView)v.findViewById(R.id.textViewDescription);
        progress = (ProgressBar) v.findViewById(R.id.imageProgress);
        aq = new AQuery(itemImage);
    }

    @Override
    public void onSuccess(Object response) {
        if(response instanceof ItemDetailResponse){
            aq.progress(progress).image(itemImageUrl, true, true);
            ItemDetailResponse resp = (ItemDetailResponse)response;
            String detailedDescription = "";
            if(resp.getSanitizedDescription() != null){
                detailedDescription = detailedDescription + resp.getSanitizedDescription();
            }
            if(resp.getPlaintext() != null){
                detailedDescription = detailedDescription + "\n\n" + resp.getPlaintext();
            }
            itemDescription.setText(detailedDescription);
            if(resp.getGold() != null){
                itemGold.setText(String.valueOf(resp.getGold().getTotal()) + " " + getResources().getString(R.string.gold));
            }
            if(resp.getName() != null){
                itemName.setText(resp.getName());
            }

        }

    }

    @Override
    public void onFailure(Object response) {
        String errorMessage = (String)response;
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
