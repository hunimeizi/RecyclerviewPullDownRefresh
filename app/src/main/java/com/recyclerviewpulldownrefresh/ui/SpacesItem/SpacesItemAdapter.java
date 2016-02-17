package com.recyclerviewpulldownrefresh.ui.SpacesItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.recyclerviewpulldownrefresh.R;
import com.recyclerviewpulldownrefresh.view.recyclerview.adapter.BaseCommonRefreshRecyclerAdapter;

import java.util.List;

/**
 * Author: LYBo
 * Create at: 2016-02-17 14:58
 * Class:
 */
public class SpacesItemAdapter extends BaseCommonRefreshRecyclerAdapter {

    private Context context;
    private List<String> listSource;

    SpacesItemAdapter(Context context, List<String> listSource) {
        this.context = context;
        this.listSource = listSource;
    }

    @Override
    protected BaseViewHolder onRealCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, null);
        return new ViewHolder(view);
    }

    @Override
    protected int getRealItemCount() {
        return (null == listSource || listSource.isEmpty()) ? 0 : listSource.size();
    }

    @Override
    protected void onRealBindViewHolder(BaseViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        Glide.with(context)
                .load(listSource.get(position)).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .centerCrop()
                .crossFade()
                .into(holder.item_home_product_image);
    }

    class ViewHolder extends BaseViewHolder {

        private ImageView item_home_product_image;

        public ViewHolder(View itemView) {
            super(itemView);
            item_home_product_image = (ImageView) itemView.findViewById(R.id.item_home_product_image);
        }
    }
}
