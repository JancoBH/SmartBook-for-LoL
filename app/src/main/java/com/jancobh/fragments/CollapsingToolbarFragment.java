package com.jancobh.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jancobh.MainActivity;
import com.jancobh.adapters.RecyclerAdapter;
import com.jancobh.models.CardItemModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CollapsingToolbarFragment extends Fragment {

    private List<CardItemModel> cardItems = new ArrayList<>(30);
    private MainActivity appCompatActivity;
    private Toolbar toolbar;
    private RecyclerView recyclerView;


    public CollapsingToolbarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        appCompatActivity = (MainActivity)activity;
    }

    @Override // Hide Search Icon with setHasOptionsMenu(true);
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collapsing_toolbar, container, false);

        toolbar = (Toolbar)view.findViewById(R.id.toolbar);

        setupToolbar();

        ((CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar)).setTitle(
                getString(R.string.collapsing_toolbar_fragment_title));

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        setupRecyclerView();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        appCompatActivity.setupNavigationDrawer(toolbar);
    }

    private void setupToolbar(){
        toolbar.setTitle("");
        appCompatActivity.setSupportActionBar(toolbar);
    }

    private void setupRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(appCompatActivity));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerAdapter(cardItems));
    }

}
