/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.recyclerviewpulldownrefresh.view.refresh;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * © 2012 amsoft.cn
 * 名称：AbPullToRefreshView.java
 * 描述：下拉刷新和加载更多的View.
 *
 */
public class AbPullToRefreshView extends LinearLayout {

    /**
     * 上下文.
     */
    private Context mContext = null;

    /**
     * 下拉刷新的开关.
     */
    private boolean mEnablePullRefresh = true;

    /**
     * 加载更多的开关.
     */
    private boolean mEnableLoadMore = true;

    /**
     * x上一次保存的.
     */
    private int mLastMotionX;

    /**
     * y上一次保存的.
     */
    private int mLastMotionY;

    /**
     * header view.
     */
    private AbListViewHeader mHeaderView;

    /**
     * footer view.
     */
    private AbListViewFooter mFooterView;

    /**
     * list or grid.
     */
    private AdapterView<?> mAdapterView;

    /**
     * Scrollview.
     */
    private ScrollView mScrollView;

    /**
     * RecyclerView.
     */
    private RecyclerView recyclerView;

    private View mTarget;

    /**
     * header view 高度.
     */
    private int mHeaderViewHeight;

    /**
     * footer view 高度.
     */
    private int mFooterViewHeight;

    /**
     * 滑动状态.
     */
    private int mPullState;

    /**
     * 上滑动作.
     */
    private static final int PULL_UP_STATE = 0;

    /**
     * 下拉动作.
     */
    private static final int PULL_DOWN_STATE = 1;

    /**
     * 正在下拉刷新.
     */
    private boolean mPullRefreshing = false;

    /**
     * 正在加载更多.
     */
    private boolean mPullLoading = false;

    /**
     * Footer加载更多监听器.
     */
    private OnFooterRefreshListener mOnFooterLoadListener;

    /**
     * Header下拉刷新监听器.
     */
    private OnHeaderRefreshListener mOnHeaderRefreshListener;

    /**
     * 构造.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public AbPullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 构造.
     *
     * @param context the context
     */
    public AbPullToRefreshView(Context context) {
        super(context);
        init(context);
    }

    /**
     * 初始化View.
     *
     * @param context the context
     */
    private void init(Context context) {
        mContext = context;
        this.setOrientation(LinearLayout.VERTICAL);
        // 增加HeaderView
        addHeaderView();
    }

    /**
     * add HeaderView.
     */
    private void addHeaderView() {
        mHeaderView = new AbListViewHeader(mContext);
        mHeaderViewHeight = mHeaderView.getHeaderHeight();
        mHeaderView.setGravity(Gravity.BOTTOM);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mHeaderViewHeight);
        // 设置topMargin的值为负的header View高度,即将其隐藏在最上方
        params.topMargin = -mHeaderViewHeight;
        addView(mHeaderView, params);
    }

    /**
     * add FooterView.
     */
    private void addFooterView() {
        mFooterView = new AbListViewFooter(mContext);
        mFooterViewHeight = mFooterView.getFooterHeight();

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mFooterViewHeight);
        addView(mFooterView, params);
    }

    /**
     * 在此添加footer view保证添加到linearlayout中的最后.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addFooterView();
        initContentAdapterView();
    }

    /**
     * init AdapterView like ListView,
     * GridView and so on;
     * or init ScrollView.
     */
    private void initContentAdapterView() {
        int count = getChildCount();
        if (count < 3) {
            throw new IllegalArgumentException("this layout must contain 3 child views,and AdapterView or ScrollView must in the second position!");
        }
        View view;
        for (int i = 0; i < count - 1; ++i) {
            view = getChildAt(i);

            if (view instanceof AdapterView<?>) {
                mAdapterView = (AdapterView<?>) view;
                mTarget = view;
            } else if (view instanceof ScrollView) {
                // finish later
                mScrollView = (ScrollView) view;
                mTarget = view;
            } else if (view instanceof RecyclerView) {
                // finish later
                recyclerView = (RecyclerView) view;
                mTarget = view;
            } else if (view instanceof WebView) {
                mTarget = view;
            }
        }
        if (mAdapterView == null && mScrollView == null && recyclerView == null) {
//            throw new IllegalArgumentException("must contain a AdapterView or ScrollView in this layout!");
        }
    }

    /* (non-Javadoc)
     * @see android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 首先拦截down事件,记录y坐标
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // deltaY > 0 是向下运动,< 0是向上运动
                int deltaX = x - mLastMotionX;
                int deltaY = y - mLastMotionY;
                //解决点击与移动的冲突
                if (Math.abs(deltaX) < Math.abs(deltaY) && Math.abs(deltaY) > 10) {
                    if (isRefreshViewScroll(deltaY)) {
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    /*
     * 如果在onInterceptTouchEvent()方法中没有拦截(即onInterceptTouchEvent()方法中 return
     * false)则由PullToRefreshView 的子View来处理;否则由下面的方法来处理(即由PullToRefreshView自己来处理)
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mLastMotionY;
                if (mPullState == PULL_DOWN_STATE) {
                    // 执行下拉
                    headerPrepareToRefresh(deltaY);
                } else if (mPullState == PULL_UP_STATE) {
                    // 执行上拉
                    footerPrepareToRefresh(deltaY);
                }
                mLastMotionY = y;
                break;
            //UP和CANCEL执行相同的方法
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int topMargin = getHeaderTopMargin();
                if (mPullState == PULL_DOWN_STATE) {
                    if (topMargin >= 0) {
                        // 开始刷新
                        headerRefreshing();
                    } else {
                        // 还没有执行刷新，重新隐藏
                        setHeaderTopMargin(-mHeaderViewHeight);
                    }
                } else if (mPullState == PULL_UP_STATE) {
                    //控制在什么时候加载更多
                    if (Math.abs(topMargin) >= mHeaderViewHeight + mFooterViewHeight) {
                        // 开始执行footer 刷新
                        footerLoading();
                    } else {
                        // 还没有执行刷新，重新隐藏
                        setHeaderTopMargin(-mHeaderViewHeight);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断滑动方向，和是否响应事件.
     *
     * @param deltaY deltaY > 0 是向下运动,< 0是向上运动
     * @return true, if is refresh view scroll
     */
    private boolean isRefreshViewScroll(int deltaY) {
        if (mPullRefreshing || mPullLoading) {
            return false;
        }

        if (null == mTarget) {
            return false;
        }

        //下拉刷新
        if (deltaY > 0) {
            boolean canScrollUp = canChildScrollUp();
            if (canScrollUp) {
                mPullState = PULL_DOWN_STATE;
            }
            return canScrollUp;
        } else if (deltaY < 0) { //上拉加载更多
            boolean canScrollDown = canChildScrollDown();
            if (canScrollDown) {
                mPullState = PULL_UP_STATE;
            }
            return canScrollDown;
        }
        return false;
    }

    /**
     * 判断是否允许下拉刷新
     *
     * @return
     */
    public boolean canChildScrollUp() {
        // 判断是否禁用下拉刷新操作
        if (!mEnablePullRefresh) {
            return false;
        }

        if (mTarget instanceof AdapterView<?>) {
            final AdapterView<?> absListView = (AdapterView<?>) mTarget;
            View child = absListView.getChildAt(0);
            if (child == null) {
                return true;
            }
            if (absListView.getFirstVisiblePosition() == 0 && child.getTop() == 0) {
                return true;
            }

            int top = child.getTop();
            int padding = absListView.getPaddingTop();
            if (absListView.getFirstVisiblePosition() == 0 && Math.abs(top - padding) <= 11) {
                return true;
            }
            return false;
        } else if (mTarget instanceof ScrollView) {
            return mScrollView.getScrollY() == 0;
        } else if (mTarget instanceof WebView) {
            WebView webView = (WebView) mTarget;
            if (webView != null) {
                return webView.getScrollY() == 0;
            }
            return false;
        } else if (mTarget instanceof RecyclerView) {
            int firstVisiblePosition = 0;

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            View childView = layoutManager.getChildAt(0);
            if (null == childView) {
                return true;
            }

            if (layoutManager instanceof LinearLayoutManager) {
                firstVisiblePosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof GridLayoutManager) {
                firstVisiblePosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] spanPosition = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(spanPosition);
                firstVisiblePosition = getFirstVisibleItemPosition(spanPosition);
            }

            if (childView.getTop() == 0 && firstVisiblePosition == 0) {
                return true;
            }
            return false;
        } else {
            return ViewCompat.canScrollVertically(mTarget, 1);
        }
    }

    /**
     * 判断是否允许上啦加载
     *
     * @return
     */
    public boolean canChildScrollDown() {
        if (!mEnableLoadMore) {
            return false;
        }
        if (mTarget instanceof AdapterView<?>) {
            AdapterView<?> absListView = (AdapterView<?>) mTarget;
            View lastChild = absListView.getChildAt(absListView.getChildCount() - 1);
            if (lastChild == null) {
                return true;
            }
            // 最后一个子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
            // 等于父View的高度说明mAdapterView已经滑动到最后
            if (lastChild.getBottom() <= getHeight() && absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                return true;
            }
        } else if (mTarget instanceof WebView) {
            WebView webview = (WebView) mTarget;
            return webview.getContentHeight() * webview.getScale() <= webview
                    .getHeight() + webview.getScrollY();
        } else if (mTarget instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) mTarget;
            View childView = scrollView.getChildAt(0);
            if (childView != null) {
                return childView.getMeasuredHeight() <= getHeight() + mScrollView.getScrollY();
            }
        } else if (mTarget instanceof RecyclerView) {
            int lastVisiblePosition = 0;
            View childView = null;

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

            if (layoutManager instanceof LinearLayoutManager) {
                lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                childView = layoutManager.findViewByPosition(lastVisiblePosition);
            } else if (layoutManager instanceof GridLayoutManager) {
                lastVisiblePosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                childView = layoutManager.findViewByPosition(lastVisiblePosition);
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] spanPosition = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(spanPosition);
                lastVisiblePosition = getLastVisibleItemPosition(spanPosition);
                childView = getLastVisibleItemBottomView(layoutManager, spanPosition, lastVisiblePosition);
            }
            if (null == childView) {
                return false;
            }

            if (lastVisiblePosition == layoutManager.getItemCount() - 1 && childView.getBottom() + layoutManager.getBottomDecorationHeight(childView) <= mTarget.getBottom()) {
                return true;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, 1);
        }
        return false;
    }

    /**
     * 针对StaggeredGridLayoutManager 瀑布流 获取最后一行显示的最大position
     *
     * @param spanPosition 最后span所有position
     * @return 最后一行显示的最大position
     */
    private int getLastVisibleItemPosition(int[] spanPosition) {
        if (null == spanPosition || null == mTarget) {
            return 0;
        }

        int maxPosition = 0;

        for (int position : spanPosition) {
            if (maxPosition < position) {
                maxPosition = position;
            }
        }
        return maxPosition;
    }

    private View getLastVisibleItemBottomView(RecyclerView.LayoutManager layoutManager, final int[] spanPosition, final int lastVisiblePosition) {
        if (null == spanPosition || null == layoutManager) {
            return null;
        }
        int maxBottom = 0;
        View lastChildView = null;

        for (int position = 0; position < spanPosition.length; position++) {
            if (lastVisiblePosition < position) {
                break;
            }

            View childView = layoutManager.findViewByPosition(lastVisiblePosition - position);
            if (null == childView) {
                continue;
            }

            if (maxBottom < childView.getBottom()) {
                lastChildView = childView;
                maxBottom = childView.getBottom();
            }
        }
        return lastChildView;
    }

    /**
     * 针对StaggeredGridLayoutManager 瀑布流 获取最上面一行显示的最小position<p/>
     * 一般是0
     *
     * @param spanPosition 最上面span所有position
     * @return 最上面一行显示的最小position
     */
    private int getFirstVisibleItemPosition(int[] spanPosition) {
        if (null == spanPosition) {
            return 0;
        }

        int maxPosition = 0;

        for (int position : spanPosition) {
            if (maxPosition > position) {
                maxPosition = position;
            }
        }
        return maxPosition;
    }

    /**
     * header 准备刷新,手指移动过程,还没有释放.
     *
     * @param deltaY 手指滑动的距离
     */
    private void headerPrepareToRefresh(int deltaY) {
        if (mPullRefreshing || mPullLoading) {
            return;
        }

        int newTopMargin = updateHeaderViewTopMargin(deltaY);
        // 当header view的topMargin>=0时，说明header view完全显示出来了 ,修改header view 的提示状态
        if (newTopMargin >= 0 && mHeaderView.getState() != AbListViewHeader.STATE_REFRESHING) {
            //提示松开刷新
            mHeaderView.setState(AbListViewHeader.STATE_READY);
        } else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {
            //提示下拉刷新
            mHeaderView.setState(AbListViewHeader.STATE_NORMAL);
        }
    }

    /**
     * footer 准备刷新,手指移动过程,还没有释放 移动footer view高度同样和移动header view
     * 高度是一样，都是通过修改header view的topmargin的值来达到.
     *
     * @param deltaY 手指滑动的距离
     */
    private void footerPrepareToRefresh(int deltaY) {
        if (mPullRefreshing || mPullLoading) {
            return;
        }
        int newTopMargin = updateHeaderViewTopMargin(deltaY);
        // 如果header view topMargin 的绝对值大于或等于header + footer 的高度
        // 说明footer view 完全显示出来了，修改footer view 的提示状态
        if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight) && mFooterView.getState() != AbListViewFooter.STATE_LOADING) {
            mFooterView.setState(AbListViewFooter.STATE_READY);
        } else if (Math.abs(newTopMargin) < (mHeaderViewHeight + mFooterViewHeight)) {
            mFooterView.setState(AbListViewFooter.STATE_NORMAL);
        }
    }

    /**
     * 修改Header view top margin的值.
     *
     * @param deltaY the delta y
     * @return the int
     */
    private int updateHeaderViewTopMargin(int deltaY) {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        float newTopMargin = params.topMargin + deltaY * 0.3f;
        // 这里对上拉做一下限制,因为当前上拉后然后不释放手指直接下拉,会把下拉刷新给触发了
        // 表示如果是在上拉后一段距离,然后直接下拉
        if (deltaY > 0 && mPullState == PULL_UP_STATE && Math.abs(params.topMargin) <= mHeaderViewHeight) {
            return params.topMargin;
        }
        // 同样地,对下拉做一下限制,避免出现跟上拉操作时一样的bug
        if (deltaY < 0 && mPullState == PULL_DOWN_STATE && Math.abs(params.topMargin) >= mHeaderViewHeight) {
            return params.topMargin;
        }
        params.topMargin = (int) newTopMargin;
        mHeaderView.setLayoutParams(params);
        return params.topMargin;
    }

    /**
     * 下拉刷新.
     */
    public void headerRefreshing() {
        mPullRefreshing = true;
        mHeaderView.setState(AbListViewHeader.STATE_REFRESHING);
        setHeaderTopMargin(0);
        if (mOnHeaderRefreshListener != null) {
            mOnHeaderRefreshListener.onHeaderRefresh(this);
        }
    }

    /**
     * 加载更多.
     */
    private void footerLoading() {
        mPullLoading = true;
        mFooterView.setState(AbListViewFooter.STATE_LOADING);
        int top = mHeaderViewHeight + mFooterViewHeight;
        setHeaderTopMargin(-top);
        if (mOnFooterLoadListener != null) {
            mOnFooterLoadListener.onFooterRefresh(this);
        }
    }

    /**
     * 设置header view 的topMargin的值.
     *
     * @param topMargin the new header top margin
     */
    private void setHeaderTopMargin(int topMargin) {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        params.topMargin = topMargin;
        mHeaderView.setLayoutParams(params);
    }

    /**
     * header view 完成更新后恢复初始状态.
     */
    public void onHeaderRefreshFinish() {
        setHeaderTopMargin(-mHeaderViewHeight);
        mHeaderView.setState(AbListViewHeader.STATE_SUCCESS);
        mPullRefreshing = false;
    }

    /**
     * footer view 完成更新后恢复初始状态.
     */
    public void onFooterLoadFinish() {
        setHeaderTopMargin(-mHeaderViewHeight);
        mHeaderView.setState(AbListViewHeader.STATE_SUCCESS);
        mPullLoading = false;
    }

    /**
     * 获取当前header view 的topMargin.
     *
     * @return the header top margin
     */
    private int getHeaderTopMargin() {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        return params.topMargin;
    }

    /**
     * 设置下拉刷新的监听器.
     *
     * @param headerRefreshListener the new on header refresh listener
     */
    public void setOnHeaderRefreshListener(OnHeaderRefreshListener headerRefreshListener) {
        mOnHeaderRefreshListener = headerRefreshListener;
    }

    /**
     * 设置加载更多的监听器.
     *
     * @param footerLoadListener the new on footer load listener
     */
    public void setOnFooterRefreshListener(OnFooterRefreshListener footerLoadListener) {
        mOnFooterLoadListener = footerLoadListener;
    }

    /**
     * 打开或者关闭下拉刷新功能.
     *
     * @param enable 开关标记
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
    }

    /**
     * 打开或者关闭加载更多功能.
     *
     * @param enable 开关标记
     */
    public void setLoadMoreEnable(boolean enable) {
        mEnableLoadMore = enable;
    }

    /**
     * 下拉刷新是打开的吗.
     *
     * @return true, if is enable pull refresh
     */
    public boolean isEnablePullRefresh() {
        return mEnablePullRefresh;
    }

    /**
     * 加载更多是打开的吗.
     *
     * @return true, if is enable load more
     */
    public boolean isEnableLoadMore() {
        return mEnableLoadMore;
    }

    /**
     * 描述：获取Header View.
     *
     * @return the header view
     */
    public AbListViewHeader getHeaderView() {
        return mHeaderView;
    }

    /**
     * 描述：获取Footer View.
     *
     * @return the footer view
     */
    public AbListViewFooter getFooterView() {
        return mFooterView;
    }

    /**
     * Interface definition for a callback to be invoked when list/grid footer
     * view should be refreshed.
     */
    public interface OnFooterRefreshListener {

        /**
         * On footer load.
         *
         * @param view the view
         */
        void onFooterRefresh(AbPullToRefreshView view);
    }

    /**
     * Interface definition for a callback to be invoked when list/grid header
     * view should be refreshed.
     */
    public interface OnHeaderRefreshListener {

        /**
         * On header refresh.
         *
         * @param view the view
         */
        void onHeaderRefresh(AbPullToRefreshView view);
    }
}
