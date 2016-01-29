package com.recyclerviewpulldownrefresh.view.recyclerview.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


/**
 * 重写并继承RecyclerView<p/>
 * 添加header footer emptyView
 * com.snscity.globalexchange.view.recyclerview
 */
public abstract class BaseCommonRefreshRecyclerAdapter extends RecyclerView.Adapter<BaseCommonRefreshRecyclerAdapter.BaseViewHolder> {

    public AdapterView.OnItemClickListener mOnItemClickListener;

    public AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    private static final int TYPE_HEADER = 0;

    private static final int TYPE_FOOTER = 1;

    private static final int TYPE_ITEM = 2;

    private static final int TYPE_EMPTY = 3;

    public View mHeaderView;

    public View mFooterView;

    public View mEmptyView;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER && mHeaderView != null) {
            return new SimpleViewHolder(mHeaderView);
        } else if (viewType == TYPE_FOOTER && mFooterView != null) {
            return new SimpleViewHolder(mFooterView);
        } else if (viewType == TYPE_EMPTY && mEmptyView != null) {
            return new SimpleViewHolder(mEmptyView);
        }
        return onRealCreateViewHolder(parent, viewType);
    }

    protected abstract BaseViewHolder onRealCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public int getItemCount() {
        int size = getRealItemCount();
        if (size == 0 && null != mEmptyView) {
            size = 1;
        } else {
            if (null != mHeaderView)
                size++;
            if (null != mFooterView)
                size++;
        }
        return size;
    }

    /**
     * 获取实际item count
     *
     * @return
     */
    protected abstract int getRealItemCount();

    @Override
    public int getItemViewType(int position) {
        int size = getItemCount();
        if (size == 0 && null != mEmptyView) {
            return TYPE_EMPTY;
        } else if (position < getHeadViewSize()) {
            return TYPE_HEADER;
        } else if (position >= getHeadViewSize() + size) {
            return TYPE_FOOTER;
        }

        return TYPE_ITEM;

//        DebugLog.e("getItemId = " + getItemId(position));
//        return (int) getItemId(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER)
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (null == lp || !(lp instanceof StaggeredGridLayoutManager.LayoutParams)) {
            return;
        }
        int position = holder.getLayoutPosition();
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    /**
     * 载入ViewHolder，这里仅仅处理header和footer视图的逻辑
     */
    @Override
    public void onBindViewHolder(final BaseViewHolder viewHolder, int position) {
        if (getItemCount() == 0 && getItemCount() == 1 && null != mEmptyView && position == 0) {
            //处理emptyView
        } else if (null != mHeaderView && position == 0) {
            //处理headView
        } else if (null != mFooterView && position == getItemCount() - 1) {
            //处理footView
        } else {
            if (mHeaderView != null) {
                position--;
            }

            onRealBindViewHolder(viewHolder, position);

            final int pos = position;
            // 设置点击事件
            if (mOnItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(null, viewHolder.itemView, pos, pos);
                    }
                });
            }
            // 设置长按事件
            if (mOnItemLongClickListener != null) {
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return mOnItemLongClickListener.onItemLongClick(null, viewHolder.itemView, pos, pos);
                    }
                });
            }
        }
    }

    protected abstract void onRealBindViewHolder(final BaseViewHolder viewHolder, int position);

    public int getHeadViewSize() {
        return mHeaderView == null ? 0 : 1;
    }

    public int getFooterViewSize() {
        return mFooterView == null ? 0 : 1;
    }

    //remove a header from the adapter
    public void removeHeader(View header) {
        notifyItemRemoved(0);
        mHeaderView = null;
    }

    protected abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    protected class SimpleViewHolder extends BaseViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
