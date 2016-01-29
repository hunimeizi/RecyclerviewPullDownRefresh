package com.recyclerviewpulldownrefresh.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.recyclerviewpulldownrefresh.R;
import com.recyclerviewpulldownrefresh.ui.recyclerviewandscrollview.ScrollViewAndRecyclerViewAtc;
import com.recyclerviewpulldownrefresh.ui.recyclerviewpulldownrefresh.RecyclerViewPullDownRefreshAtc;

/**
 * Author: LYBo
 * Create at: 2016-01-29 09:28
 * Class:
 */
public class MainAct extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainact);
    }

    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.mainAct_RecycleViewPullDownRefresh_btn:
                startActivity(new Intent(MainAct.this, RecyclerViewPullDownRefreshAtc.class));
                break;
            case R.id.mainAct_Qiantao_btn:
                startActivity(new Intent(MainAct.this, ScrollViewAndRecyclerViewAtc.class));
                break;
        }
    }
}
