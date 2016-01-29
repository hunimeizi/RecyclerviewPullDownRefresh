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
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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
import com.recyclerviewpulldownrefresh.utils.ViewUtils;


/**
 * © 2012 amsoft.cn
 * 名称：AbListViewFooter.java
 * 描述：加载更多Footer View类.
 */
public class AbListViewFooter extends LinearLayout {

    /**
     * The m context.
     */
    private Context mContext;

    /**
     * The m state.
     */
    private int mState = -1;

    /**
     * 显示 上拉加载.
     */
    public final static int STATE_NORMAL = 0;

    /**
     * 显示 松开加载更多.
     */
    public final static int STATE_READY = 1;

    /**
     * 显示 正在加载....
     */
    public final static int STATE_LOADING = 2;

    /**
     * 显示 加载成功....
     */
    public final static int STATE_SUCCESS = 3;

    /**
     * The footer view.
     */
    private RelativeLayout footerView;

    /**
     * The footer progress bar.
     */
    private ProgressBar footerProgressBar;

    /**
     * The footer text view.
     */
    private TextView footerTextView;

    /**
     * 箭头图标View.
     */
    private ImageView arrowImageView;

    /**
     * The footer content height.
     */
    private int footerHeight;

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
     * Instantiates a new ab list view footer.
     *
     * @param context the context
     */
    public AbListViewFooter(Context context) {
        super(context);
        initView(context);
    }

    /**
     * Instantiates a new ab list view footer.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public AbListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        setState(STATE_READY);
    }

    /**
     * Inits the view.
     *
     * @param context the context
     */
    private void initView(Context context) {
        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.refresh_footer, this, true);
        footerView = (RelativeLayout) findViewById(R.id.pull_to_refresh_footer);
        footerTextView = (TextView) findViewById(R.id.pull_to_load_text);
        footerProgressBar = (ProgressBar) findViewById(R.id.pull_to_load_progress);
        arrowImageView = (ImageView) findViewById(R.id.pull_to_load_image);

        initAnim();

        //获取View的高度
        ViewUtils.measureView(this);
        footerHeight = this.getMeasuredHeight();
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
     * 设置当前状态.
     *
     * @param state the new state
     */
    public void setState(int state) {
        if (state == mState)
            return;

        if (state == STATE_LOADING) {
            arrowImageView.clearAnimation();
            arrowImageView.setVisibility(View.INVISIBLE);
            footerProgressBar.setVisibility(View.VISIBLE);
        } else {
            arrowImageView.setVisibility(View.VISIBLE);
            footerProgressBar.setVisibility(View.INVISIBLE);
        }

        if (state == STATE_NORMAL) {
            if (mState == STATE_READY) {
                arrowImageView.startAnimation(mRotateDownAnim);
            }
            if (mState == STATE_LOADING) {
                arrowImageView.clearAnimation();
            }

            footerView.setVisibility(View.VISIBLE);
            footerProgressBar.setVisibility(View.GONE);
            footerTextView.setText(R.string.pullup_to_load);
        } else if (state == STATE_READY) {
            if (mState != STATE_READY) {
                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(mRotateUpAnim);
            }
            footerTextView.setText(R.string.release_to_load);
        } else if (state == STATE_LOADING) {
            footerProgressBar.setVisibility(View.VISIBLE);
            footerTextView.setText(R.string.loading);
        } else if (state == STATE_SUCCESS) { //加载成功
            footerView.setVisibility(View.GONE);
        }
        mState = state;
    }

    /**
     * Gets the visiable height.
     *
     * @return the visiable height
     */
    public int getVisiableHeight() {
        LayoutParams lp = (LayoutParams) footerView.getLayoutParams();
        return lp.height;
    }

    /**
     * 隐藏footerView.
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) footerView.getLayoutParams();
        lp.height = 0;
        footerView.setLayoutParams(lp);
        footerView.setVisibility(View.GONE);
    }

    /**
     * 显示footerView.
     */
    public void show() {
        footerView.setVisibility(View.VISIBLE);
        LayoutParams lp = (LayoutParams) footerView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        footerView.setLayoutParams(lp);
    }


    /**
     * 描述：设置字体颜色.
     *
     * @param color the new text color
     */
    public void setTextColor(int color) {
        footerTextView.setTextColor(color);
    }

    /**
     * 描述：设置字体大小.
     *
     * @param size the new text size
     */
    public void setTextSize(int size) {
        footerTextView.setTextSize(size);
    }

    /**
     * 描述：设置背景颜色.
     *
     * @param color the new background color
     */
    public void setBackgroundColor(int color) {
        footerView.setBackgroundColor(color);
    }

    /**
     * 描述：获取Footer ProgressBar，用于设置自定义样式.
     *
     * @return the footer progress bar
     */
    public ProgressBar getFooterProgressBar() {
        return footerProgressBar;
    }

    /**
     * 描述：设置Footer ProgressBar样式.
     *
     * @param indeterminateDrawable the new footer progress bar drawable
     */
    public void setFooterProgressBarDrawable(Drawable indeterminateDrawable) {
        footerProgressBar.setIndeterminateDrawable(indeterminateDrawable);
    }

    /**
     * 描述：获取高度.
     *
     * @return the footer height
     */
    public int getFooterHeight() {
        return footerHeight;
    }

    /**
     * 设置高度.
     *
     * @param height 新的高度
     */
    public void setVisiableHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) footerView.getLayoutParams();
        lp.height = height;
        footerView.setLayoutParams(lp);
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public int getState() {
        return mState;
    }


}
