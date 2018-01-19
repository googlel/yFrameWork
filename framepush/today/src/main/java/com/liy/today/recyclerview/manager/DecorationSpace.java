package com.liy.today.recyclerview.manager;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class DecorationSpace extends RecyclerView.ItemDecoration {

    int left, top, right, bottom;
    /**
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public DecorationSpace(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * @param space 传入的值，其单位视为dp
     */
    public DecorationSpace(int space) {
        this.left = space;
        this.top = space;
        this.right = space;
        this.bottom = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = left;
        outRect.right = right;
        outRect.top = top;
        outRect.bottom = bottom;
    }
}
