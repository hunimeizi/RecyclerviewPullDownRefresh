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

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.recyclerviewpulldownrefresh.R;
import com.recyclerviewpulldownrefresh.utils.TimeSet;
import com.recyclerviewpulldownrefresh.utils.ViewUtils;


/**
 * © 2012 amsoft.cn 名称：AbListViewHeader.java 描述：下拉刷新的Header View类.
 */
public class AbListViewHeader extends LinearLayout {

    /**
     * 上下文.
     */
    private Context mContext;

    /**
     * 主View.
     */
    private RelativeLayout headerView;

    /**
     * 箭头图标View.
     */
    private ImageView arrowImageView;

    /**
     * 进度图标View.
     */
    private ProgressBar headerProgressBar;

    /**
     * 文本提示的View.
     */
    private TextView tipsTextview;

    /**
     * 时间的View.
     */
    private TextView headerTimeView;

    /**
     * 当前状态.
     */
    private int mState = -1;

    /**
     * 向上的动画.
     */
    private Animation mRotateUpAnim;

    /**
     * 向下的动画.
     */
    private Animation mRotateDownAnim;

    /**
     * 动画时间.
     */
    private final int ROTATE_ANIM_DURATION = 180;

    /**
     * 显示 下拉刷新.
     */
    public final static int STATE_NORMAL = 0;

    /**
     * 显示 松开刷新.
     */
    public final static int STATE_READY = 1;

    /**
     * 显示 正在刷新....
     */
    public final static int STATE_REFRESHING = 2;

    /**
     * 显示 刷新成功....
     */
    public final static int STATE_SUCCESS = 3;

    /**
     * 保存上一次的刷新时间.
     */
    private String lastRefreshTime = null;

    /**
     * Header的高度.
     */
    private int headerHeight;

    /**
     * 初始化Header.
     *
     * @param context the context
     */
    public AbListViewHeader(Context context) {
        this(context, null);
    }

    /**
     * 初始化Header.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public AbListViewHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbListViewHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AbListViewHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    /**
     * 初始化View.
     *
     * @param context the context
     */
    private void initView(Context context) {

        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.refresh_header, this, true);

        headerView = (RelativeLayout) findViewById(R.id.pull_to_refresh_header);
        arrowImageView = (ImageView) findViewById(R.id.pull_to_refresh_image);
        headerProgressBar = (ProgressBar) findViewById(R.id.pull_to_refresh_progress);
        tipsTextview = (TextView) findViewById(R.id.pull_to_refresh_text);
        headerTimeView = (TextView) findViewById(R.id.pull_to_refresh_updated_at);

        // 获取View的高度
        ViewUtils.measureView(this);
        headerHeight = this.getMeasuredHeight();

        initAnim();

        setState(STATE_NORMAL);
    }

    private void initAnim() {
        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    /**
     * 设置状态.
     *
     * @param state the new state
     */
    public void setState(int state) {
        if (state == mState)
            return;
        if (state == STATE_REFRESHING) {
            arrowImageView.clearAnimation();
            arrowImageView.setVisibility(View.INVISIBLE);
            headerProgressBar.setVisibility(View.VISIBLE);
        } else {
            arrowImageView.setVisibility(View.VISIBLE);
            headerProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) {
                    arrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
                    arrowImageView.clearAnimation();
                }

                headerView.setVisibility(View.VISIBLE);
                tipsTextview.setText(R.string.pull_to_refresh);

                if (lastRefreshTime == null) {
                    lastRefreshTime = TimeSet.getCurrentDate("HH:mm:ss");
                    headerTimeView.setText("刷新时间：" + lastRefreshTime);
                } else {
                    headerTimeView.setText("上次刷新时间：" + lastRefreshTime);
                }
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(mRotateUpAnim);

                    tipsTextview.setText(R.string.release_to_refresh);
                    headerTimeView.setText("上次刷新时间：" + lastRefreshTime);
                    lastRefreshTime = TimeSet.getCurrentDate("HH:mm:ss");
                }
                break;
            case STATE_REFRESHING:
                tipsTextview.setText(R.string.refreshing);
                headerTimeView.setText("本次刷新时间：" + lastRefreshTime);
                break;
            case STATE_SUCCESS:
                headerView.setVisibility(View.GONE);
                break;
            default:
        }

        mState = state;
    }

    /**
     * 设置header可见的高度.
     *
     * @param height the new visiable height
     */
    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams lp = (LayoutParams) headerView
                .getLayoutParams();
        lp.height = height;
        headerView.setLayoutParams(lp);
    }

    /**
     * 获取header可见的高度.
     *
     * @return the visiable height
     */
    public int getVisiableHeight() {
        LayoutParams lp = (LayoutParams) headerView
                .getLayoutParams();
        return lp.height;
    }

    /**
     * 描述：获取HeaderView.
     *
     * @return the header view
     */
    public RelativeLayout getHeaderView() {
        return headerView;
    }

    /**
     * 设置上一次刷新时间.
     *
     * @param time 时间字符串
     */
    public void setRefreshTime(String time) {
        headerTimeView.setText(time);
    }

    /**
     * 获取header的高度.
     *
     * @return 高度
     */
    public int getHeaderHeight() {
        return headerHeight;
    }

    /**
     * 描述：设置字体颜色.
     *
     * @param color the new text color
     */
    public void setTextColor(int color) {
        tipsTextview.setTextColor(color);
        headerTimeView.setTextColor(color);
    }

    /**
     * 描述：设置背景颜色.
     *
     * @param color the new background color
     */
    public void setBackgroundColor(int color) {
        headerView.setBackgroundColor(color);
    }

    /**
     * 描述：获取Header ProgressBar，用于设置自定义样式.
     *
     * @return the header progress bar
     */
    public ProgressBar getHeaderProgressBar() {
        return headerProgressBar;
    }

    /**
     * 描述：设置Header ProgressBar样式.
     *
     * @param indeterminateDrawable the new header progress bar drawable
     */
    public void setHeaderProgressBarDrawable(Drawable indeterminateDrawable) {
        headerProgressBar.setIndeterminateDrawable(indeterminateDrawable);
    }

    /**
     * 描述：得到当前状态.
     *
     * @return the state
     */
    public int getState() {
        return mState;
    }

    /**
     * 设置提示状态文字的大小.
     *
     * @param size the new state text size
     */
    public void setStateTextSize(int size) {
        tipsTextview.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * 设置提示时间文字的大小.
     *
     * @param size the new time text size
     */
    public void setTimeTextSize(int size) {
        headerTimeView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * Gets the arrow image view.
     *
     * @return the arrow image view
     */
    public ImageView getArrowImageView() {
        return arrowImageView;
    }

    /**
     * 描述：设置顶部刷新图标.
     *
     * @param resId the new arrow image
     */
    public void setArrowImage(int resId) {
        this.arrowImageView.setImageResource(resId);
    }

    public TextView getTipsTextview() {
        return tipsTextview;
    }

    public TextView getHeaderTimeView() {
        return headerTimeView;
    }
}