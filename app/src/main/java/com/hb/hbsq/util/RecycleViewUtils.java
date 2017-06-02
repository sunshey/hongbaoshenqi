package com.hb.hbsq.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by Administrator on 2017/2/14.
 * 检查recycleview是否满屏
 */

public class RecycleViewUtils {


    private RecyclerView recyclerView;

    public RecycleViewUtils(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public boolean isFullScreen() {
        if (recyclerView.getChildCount() == 0) return false;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
        } else if (layoutManager instanceof LinearLayoutManager) {
            //线性管理
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int visibleItemCount = linearLayoutManager.getChildCount();
            //获取最后一个childView
            View lastChildView = recyclerView.getChildAt(visibleItemCount - 1);
            //获取第一个childView
            View firstChildView = recyclerView.getChildAt(0);
            int top = firstChildView.getTop();
            int bottom = lastChildView.getBottom();
            //recycleView显示itemView的有效区域的bottom坐标Y
            int bottomEdge = recyclerView.getHeight() - recyclerView.getPaddingBottom();
            //recycleView显示itemView的有效区域的top坐标Y
            int topEdge = recyclerView.getPaddingTop();
            //第一个view的顶部小于top边界值,说明第一个view已经部分或者完全移出了界面
            //最后一个view的底部小于bottom边界值,说明最后一个view已经完全显示在界面
            //若满足这两个条件,说明所有子view已经填充满了recycleView,recycleView可以"真正地"滑动
            if (bottom <= bottomEdge && top < topEdge) {
                //满屏的recyceView
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
        }

        return false;
    }
}
