package com.recyclerviewpulldownrefresh.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import com.recyclerviewpulldownrefresh.view.recyclerview.adapter.BaseCommonRefreshRecyclerAdapter;


/**
 * 重写并继承RecyclerView<p/>
 * 添加header footer emptyView
 * com.snscity.globalexchange.view.recyclerview
 *
 */
public class RefreshRecyclerView extends RecyclerView {

    private View mHeaderView;

    private View mFooterView;

    private View mEmptyView;

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addHeaderView(View headerView) {
        mHeaderView = headerView;
    }

    /**
     * @return recycle的头部视图
     */
    public View getHeaderView() {
        return mHeaderView;
    }


    /**
     * 设置底部的视图
     */
    public void addFooterView(View footerView) {
        mFooterView = footerView;
    }

    /**
     * 得到底部的视图
     */
    public View getFooterView() {
        return mFooterView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(final View emptyView) {
        mEmptyView = emptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof BaseCommonRefreshRecyclerAdapter) {
            ((BaseCommonRefreshRecyclerAdapter) adapter).mOnItemClickListener = mOnItemClickListener;
            ((BaseCommonRefreshRecyclerAdapter) adapter).mOnItemLongClickListener = mOnItemLongClickListener;
            ((BaseCommonRefreshRecyclerAdapter) adapter).mHeaderView = mHeaderView;
            ((BaseCommonRefreshRecyclerAdapter) adapter).mFooterView = mFooterView;
            ((BaseCommonRefreshRecyclerAdapter) adapter).mEmptyView = mEmptyView;
        } else {
            throw new IllegalArgumentException("adapter must extends BaseCommonRefreshRecyclerAdapter!");
        }
    }

    /**
     * 平滑滚动到某个位置
     *
     * @param isAbsolute position是否是绝对的，如果是绝对的，那么header的位置就是0
     *                   如果是相对的，那么position就是相对内容的list的位置
     */
    public void smoothScrollToPosition(int position, boolean isAbsolute) {
        if (!isAbsolute && mHeaderView != null) {
            position++;
        }
        smoothScrollToPosition(position);
    }

    /**
     * 设置item的点击事件
     */
    private static AdapterView.OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 设置item的长按事件
     */
    public static AdapterView.OnItemLongClickListener mOnItemLongClickListener = null;

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }


}
