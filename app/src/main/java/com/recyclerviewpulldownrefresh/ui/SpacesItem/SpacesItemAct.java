package com.recyclerviewpulldownrefresh.ui.SpacesItem;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.recyclerviewpulldownrefresh.R;
import com.recyclerviewpulldownrefresh.view.recyclerview.RefreshRecyclerView;
import com.recyclerviewpulldownrefresh.view.recyclerview.SpacesItemDecoration;
import com.recyclerviewpulldownrefresh.view.refresh.AbPullToRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: LYBo
 * Create at: 2016-02-17 14:47
 * Class: 瀑布流
 */
public class SpacesItemAct extends Activity {

    RefreshRecyclerView home_refreshRecyclerView_list;
    private AbPullToRefreshView pullToRefreshView;
    private List<String> listSource = new ArrayList<>();
    SpacesItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spacesitem);

        home_refreshRecyclerView_list = (RefreshRecyclerView) findViewById(R.id.home_refreshRecyclerView_list);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        home_refreshRecyclerView_list.setLayoutManager(staggeredGridLayoutManager);
        home_refreshRecyclerView_list.setItemAnimator(new DefaultItemAnimator());
        home_refreshRecyclerView_list.setHasFixedSize(true);

        SpacesItemDecoration decoration = new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen._1dp));
        decoration.setSpanCount(staggeredGridLayoutManager.getSpanCount());
        home_refreshRecyclerView_list.addItemDecoration(decoration);

        View headerView = LayoutInflater.from(this).inflate(R.layout.activity_scrollandrecyclerview_item, null);
        home_refreshRecyclerView_list.addHeaderView(headerView);

        pullToRefreshView = (AbPullToRefreshView) findViewById(R.id.home_pull_refresh_view);
        adapter = new SpacesItemAdapter(this, listSource);
        home_refreshRecyclerView_list.setAdapter(adapter);
        setListener();
        getDataToView();
    }

    private void setListener() {
        pullToRefreshView.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                flage = true;
                getDataToView();
            }
        });

        pullToRefreshView.setOnFooterRefreshListener(new AbPullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(AbPullToRefreshView view) {
                flage = false;
                getDataToView();
            }
        });
    }

    private boolean flage;

    private void getDataToView() {

        if (flage) {
            listSource.clear();
        }
        for (int dataI = 1; dataI < 10; dataI++) {
            listSource.add("http://h.hiphotos.baidu.com/image/h%3D200/sign=7237860f952397ddc9799f046983b216/dc54564e9258d109a4d1165ad558ccbf6c814d23.jpg");
            listSource.add("http://t12.baidu.com/it/u=3961208459,1008498728&fm=56");
            listSource.add("http://www.qqya.com/qqyaimg/allimg/140412/1214412Y9-0.jpg");
        }
        adapter.notifyDataSetChanged();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setPullToRefreshComplete();
            }
        }, 3000);
    }

    /**
     * 刷新复位
     */
    private void setPullToRefreshComplete() {
        if (null == pullToRefreshView) {
            return;
        }

        pullToRefreshView.onHeaderRefreshFinish();
        pullToRefreshView.onFooterLoadFinish();
    }
}
