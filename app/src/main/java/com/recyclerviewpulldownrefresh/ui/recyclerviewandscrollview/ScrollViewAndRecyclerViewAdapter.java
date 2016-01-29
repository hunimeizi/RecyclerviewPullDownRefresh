package com.recyclerviewpulldownrefresh.ui.recyclerviewandscrollview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.recyclerviewpulldownrefresh.R;
import com.recyclerviewpulldownrefresh.ui.recyclerviewpulldownrefresh.OrderBean;
import com.recyclerviewpulldownrefresh.view.recyclerview.adapter.BaseCommonRefreshRecyclerAdapter;

import java.util.List;

/**
 * Author: LYBo
 * Create at: 2016-01-29 09:36
 * Class:
 */
public class ScrollViewAndRecyclerViewAdapter extends BaseCommonRefreshRecyclerAdapter {
    private Context mContext;
    private List<OrderBean> dataList;

    public ScrollViewAndRecyclerViewAdapter(Context context, List<OrderBean> dataList) {
        this.dataList = dataList;
        mContext = context;
    }

    @Override
    protected BaseViewHolder onRealCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scroll_recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    protected int getRealItemCount() {
        return dataList.size();
    }

    @Override
    protected void onRealBindViewHolder(BaseViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if (null == holder) {
            return;
        }
        holder.title.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.image));
    }

    class ViewHolder extends BaseViewHolder {
        public ImageView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (ImageView) itemView.findViewById(R.id.title);
        }
    }
}