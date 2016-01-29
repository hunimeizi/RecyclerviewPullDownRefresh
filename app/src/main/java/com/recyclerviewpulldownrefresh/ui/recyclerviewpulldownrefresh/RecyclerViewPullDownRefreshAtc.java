package com.recyclerviewpulldownrefresh.ui.recyclerviewpulldownrefresh;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import com.recyclerviewpulldownrefresh.R;
import com.recyclerviewpulldownrefresh.view.recyclerview.FullyLinearLayoutManager;
import com.recyclerviewpulldownrefresh.view.recyclerview.RefreshRecyclerView;
import com.recyclerviewpulldownrefresh.view.refresh.AbPullToRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: LYBo
 * Create at: 2016-01-29 09:36
 * Class:RecyclerView上下拉刷新
 */
public class RecyclerViewPullDownRefreshAtc extends Activity {

    private AbPullToRefreshView recycleViewPullDownRefresh_abPullToRefreshView;
    private RecyclerViewPullDownRefreshAdapter adapter;
    List<OrderBean> orderBeans = new ArrayList<>();
    RefreshRecyclerView recycleViewPullDownRefresh_refreshRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_pull_down_refresh_atc);
        initView();
    }

    private void initView() {
        recycleViewPullDownRefresh_abPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.recycleViewPullDownRefresh_abPullToRefreshView);
        recycleViewPullDownRefresh_refreshRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycleViewPullDownRefresh_refreshRecyclerView);
        //RecyclerView 横向滚动 以下三行代码
//        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
//        fullyLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        textPullLoad_recycler.setLayoutManager(fullyLinearLayoutManager);
        recycleViewPullDownRefresh_refreshRecyclerView.setLayoutManager(new FullyLinearLayoutManager(this));//默认是纵向滚动就直接这样写 要是实现GridView效果textPullLoad_recycler.setLayoutManager(new FullyGridLayoutManager(this,4));
        adapter = new RecyclerViewPullDownRefreshAdapter(this, orderBeans);
        recycleViewPullDownRefresh_refreshRecyclerView.setAdapter(adapter);
        getDataToView();
        setPullDownRefreshListener();
    }

    private boolean flage;

    private void getDataToView() {
        if (flage) {
            orderBeans.clear();
        }
        for (int dataI = 1; dataI < 30; dataI++) {
            OrderBean orderbean = new OrderBean();
            orderbean.setA("RecyclerView上下拉刷新");
            orderbean.setB("上下拉刷新");
            orderBeans.add(orderbean);
        }
        adapter.notifyDataSetChanged();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setPullToRefreshComplete();
            }
        }, 3000);
    }

    private void setPullDownRefreshListener() {
        recycleViewPullDownRefresh_abPullToRefreshView.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                flage = true;
                getDataToView();
            }
        });
        recycleViewPullDownRefresh_abPullToRefreshView.setOnFooterRefreshListener(new AbPullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(AbPullToRefreshView view) {
                flage = false;
                getDataToView();
            }
        });
        //RecycleView Item 点击事件
        recycleViewPullDownRefresh_refreshRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    /**
     * 刷新复位
     */
    private void setPullToRefreshComplete() {
        if (null == recycleViewPullDownRefresh_abPullToRefreshView) {
            return;
        }
        recycleViewPullDownRefresh_abPullToRefreshView.onHeaderRefreshFinish();
        recycleViewPullDownRefresh_abPullToRefreshView.onFooterLoadFinish();
    }
}
