//package net.onest.timestoryprj.adapter.problem;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.ItemTouchHelper;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
//
//    private  OptionLianAdapter mAdapter;
//    private boolean sort = false;
//
//    public SimpleItemTouchHelperCallback(OptionLianAdapter adapter) {
//        mAdapter = adapter;
//    }
//
//    /*
//     * 用于返回可以滑动的方向
//     * */
//    @Override
//    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//
//        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT                | ItemTouchHelper.LEFT;        //允许上下左右的拖动
//        int swipeFlags = 0;   //不允许侧滑
//        return makeMovementFlags(dragFlags, swipeFlags);
//
//    }
//
//    /*
//     * 当用户拖动一个Item进行上下移动从旧的位置到新的位置的时候会调用该方法
//     * */
//    @Override
//    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//
//        mAdapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
//
//        return true;
//
//    }
//
//    /*
//     * 当用户左右滑动Item达到删除条件时，会调用该方法
//     * */
//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//    }
//
//    //设置item是否可以拖动
//    public void setSort(boolean sort) {
//        this.sort = sort;
//    }
//
//    /*
//     * 该方法返回true时，表示支持长按拖动，即长按ItemView后才可以拖动  默认返回true
//     * */
//    @Override
//    public boolean isLongPressDragEnabled() {
//        return sort;
//    }
//
//    /*
//     * 该方法返回true时，表示如果用户触摸并左右滑动了View，
//     * 那么可以执行滑动删除操作，即可以调用到onSwiped()方法。默认是返回true
//     * */
//    @Override
//    public boolean isItemViewSwipeEnabled() {
//        return false;
//    }
//
//
//}