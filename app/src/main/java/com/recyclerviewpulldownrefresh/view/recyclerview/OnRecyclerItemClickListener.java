package com.recyclerviewpulldownrefresh.view.recyclerview;

import android.view.View;

/**
 * Recycler不在提供ItemClick事件，重写在Adapter中
 * <p>
 * classes : com.snscity.worldexchangemodel.view.recyclerview
 */
public interface OnRecyclerItemClickListener {
    void onItemClick(View view, int position);
}
