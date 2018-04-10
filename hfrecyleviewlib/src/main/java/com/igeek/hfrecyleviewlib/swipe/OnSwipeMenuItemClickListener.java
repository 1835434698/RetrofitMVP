
package com.igeek.hfrecyleviewlib.swipe;



/**
 * Created by Yan Zhenjie on 2016/7/26.
 */
public interface OnSwipeMenuItemClickListener {

    /**
     *
     * @param closeable
     * @param adapterPosition position in recyclerView
     * @param menuPosition
     * @param direction  1 : left 2 : right
     */
    void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction);

}