package com.jancobh.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jancobh.MainActivity;
import com.jancobh.activities.FilterActivity;
import com.jancobh.activities.ItemDetailActivity;
import com.jancobh.adapters.ItemsAdapter;
import com.jancobh.base.BaseFragment;
import com.jancobh.commons.Commons;
import com.jancobh.data.Data;
import com.jancobh.data.Item;
import com.jancobh.listener.ResponseListener;
import com.jancobh.responseclasses.ItemsResponse;
import com.jancobh.service.ServiceRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class NewItemsFragment extends BaseFragment implements ResponseListener {

    private MainActivity mainActivity;
    private Toolbar toolbar;
    private ListView itemsLV;
    private ArrayList<Item> allItems;
    private ItemsAdapter itemsLVAdapter;
    private final String EXTRA_ITEM_DETAIL = "EXTRA_ITEM_DETAIL";
    private final int FILTER_REQUEST_CODE = 1;
    private final String EXTRA_FILTER = "EXTRA_FILTER";
    private final String EXTRA_FIRST_ITEM_STACK = "EXTRA_FIRST_ITEM_STACK";
    private ArrayList<String> filters;

    public NewItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_items, container, false);
        initUI(v);

        toolbar = (Toolbar)v.findViewById(R.id.items_toolbar);

        setupToolbar();

        if(Commons.allItemsNew == null || Commons.allItemsNew.size() <= 0) {
            ArrayList<String> pathParams = new ArrayList<>();
            pathParams.add("static-data");
            pathParams.add(Commons.getRegion());
            pathParams.add("v1.2");
            pathParams.add("item");
            HashMap<String, String> queryParams = new HashMap<>();
            queryParams.put("locale", Commons.getLocale());
            queryParams.put("version", Commons.LATEST_VERSION);
            queryParams.put("itemListData", "all");
            queryParams.put("api_key", Commons.API_KEY);

            ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.ALL_ITEMS_REQUEST, pathParams, queryParams, null, this);
        }else {
            try {
                Collections.sort(Commons.allItemsNew, new Comparator<Item>() {
                    @Override
                    public int compare(Item i1, Item i2) {
                        return i1.getData().getName().compareTo(i2.getData().getName());
                    }
                });
            }catch (Exception ignored){}
            allItems = Commons.allItemsNew;
            itemsLVAdapter = new ItemsAdapter(getContext(), R.layout.list_row_items, Commons.allItemsNew);
            itemsLV.setAdapter(itemsLVAdapter);
        }

        return v;
    }

    private void initUI(View v){

        filters = new ArrayList<>();
        itemsLV = (ListView) v.findViewById(R.id.itemsLV);
        ImageView filterIcon = (ImageView) v.findViewById(R.id.filterIcon);
        allItems = new ArrayList<>();
        itemsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(itemsLVAdapter != null) {
                    Item item = itemsLVAdapter.getItem(position);
                    if(item != null) {
                        Gson gson = new Gson();
                        String extraItemDataJSON = gson.toJson(item);
                        Intent i = new Intent(getContext(), ItemDetailActivity.class);
                        i.putExtra(EXTRA_ITEM_DETAIL, extraItemDataJSON);
                        i.putExtra(EXTRA_FIRST_ITEM_STACK, true);
                        startActivity(i);
                    }
                }
            }
        });

        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), FilterActivity.class);
                i.putExtra(EXTRA_FILTER, filters);
                startActivityForResult(i, FILTER_REQUEST_CODE);
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity.setupNavigationDrawer(toolbar);
    }

    private void setupToolbar(){
        toolbar.setTitle(getString(R.string.items_fragment_title));
        mainActivity.setSupportActionBar(toolbar);
    }

    @Override
    public void onSuccess(Object response) {
        if(response instanceof ItemsResponse){
            ItemsResponse resp = (ItemsResponse)response;
            if(allItems == null){
                allItems = new ArrayList<>();
            }
            if(Commons.allItemsNew == null){
                Commons.allItemsNew = new ArrayList<>();
            }
            allItems.clear();
            Map<String, Data> data = resp.getData();
            for(Map.Entry<String, Data> entry : data.entrySet()) {
                Item item = new Item();
                item.setData(entry.getValue());
                allItems.add(item);
                Commons.allItemsNew.add(item);
            }

            try {
                Collections.sort(allItems, new Comparator<Item>() {
                    @Override
                    public int compare(Item i1, Item i2) {
                        return i1.getData().getName().compareTo(i2.getData().getName());
                    }
                });
            }catch (Exception ignored){}

            try {
                if(getContext() != null) {
                    itemsLVAdapter = new ItemsAdapter(getContext(), R.layout.list_row_items, allItems);
                    itemsLV.setAdapter(itemsLVAdapter);
                }
            }catch (Exception ignored){}


        }
    }

    @Override
    public void onFailure(Object response) {
        try {
            String errorMessage = String.valueOf(response);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }catch (Exception ignored){}
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    public static boolean containsIgnoreCase(String src, String what) {
        if (src != null) {
            final int length = what.length();
            if (length == 0)
                return true; // Empty string is contained

            final char firstLo = Character.toLowerCase(what.charAt(0));
            final char firstUp = Character.toUpperCase(what.charAt(0));

            for (int i = src.length() - length; i >= 0; i--) {
                // Quick check before calling the more expensive regionMatches() method:
                final char ch = src.charAt(i);
                if (ch != firstLo && ch != firstUp)
                    continue;

                if (src.regionMatches(true, i, what, 0, length))
                    return true;
            }
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILTER_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                if(data != null){
                    ArrayList<Item> filteredItems = new ArrayList<>();
                    filters = data.getStringArrayListExtra(EXTRA_FILTER);
                    if(filters != null && filters.size() > 0 && allItems != null && allItems.size() > 0){
                        for (String filter : filters){
                            for(Item i : allItems){
                                Data itemData = i.getData();
                                if(itemData != null){
                                    ArrayList<String> tags = itemData.getTags();
                                    if(tags != null && tags.size() > 0){
                                        if(tags.contains(filter)){
                                            filteredItems.add(i);
                                        }
                                    }
                                }
                            }
                        }

                        itemsLVAdapter = new ItemsAdapter(getContext(), R.layout.list_row_items, filteredItems);
                        itemsLV.setAdapter(itemsLVAdapter);
                    }else {
                        itemsLVAdapter = new ItemsAdapter(getContext(), R.layout.list_row_items, Commons.allItemsNew);
                        itemsLV.setAdapter(itemsLVAdapter);
                    }
                }
            }
        }
    }

    @Override
    public void onSearch(String s) {
        if(s.length() >= 2){
            if(filters != null && filters.size() > 0){
                filters.clear();
            }
            ArrayList<Item> searchResultItems = new ArrayList<>();
            for(Item i : allItems){
                if(containsIgnoreCase(i.getData().getName(), String.valueOf(s))){
                    searchResultItems.add(i);
                }
            }
            itemsLVAdapter = new ItemsAdapter(getContext(), R.layout.list_row_items, searchResultItems);
            itemsLV.setAdapter(itemsLVAdapter);
            itemsLVAdapter.notifyDataSetChanged();
        }else{
            itemsLVAdapter = new ItemsAdapter(getContext(), R.layout.list_row_items, allItems);
            itemsLV.setAdapter(itemsLVAdapter);
            itemsLVAdapter.notifyDataSetChanged();
        }
    }
}
