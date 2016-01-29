package com.recyclerviewpulldownrefresh.ui.recyclerviewandscrollview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.recyclerviewpulldownrefresh.R;
import com.recyclerviewpulldownrefresh.ui.recyclerviewpulldownrefresh.OrderBean;
import com.recyclerviewpulldownrefresh.view.recyclerview.FullyLinearLayoutManager;
import com.recyclerviewpulldownrefresh.view.recyclerview.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: LYBo
 * Create at: 2016-01-29 10:54
 * Class:ScrollView 嵌套 RecyclerView
 */
public class ScrollViewAndRecyclerViewAtc extends Activity{
    private ScrollViewAndRecyclerViewAdapter adapter;
    List<OrderBean> orderBeans = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollandrecyclerview);
        initView();
    }

    private void initView() {
        RefreshRecyclerView scrollView_refreshRecyclerView=(RefreshRecyclerView)findViewById(R.id.scrollView_refreshRecyclerView);
        FullyLinearLayoutManager fullyLinearLayoutManager=new FullyLinearLayoutManager(this);
        fullyLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        scrollView_refreshRecyclerView.setLayoutManager(fullyLinearLayoutManager);
        adapter = new ScrollViewAndRecyclerViewAdapter(this, orderBeans);
        scrollView_refreshRecyclerView.setAdapter(adapter);
        getDataToView();
    }
    private void getDataToView() {
            orderBeans.clear();
        for (int dataI = 1; dataI < 30; dataI++) {
            OrderBean orderbean = new OrderBean();
            orderbean.setA("RecyclerView上下拉刷新");
            orderbean.setB("上下拉刷新");
            orderBeans.add(orderbean);
        }
        adapter.notifyDataSetChanged();
    }
}
