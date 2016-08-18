/*
 * Copyright (c) 2014. hupei (hupei132@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mylhyl.rvadapter.touch;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;

public abstract class CygItemTouchCallback extends ItemTouchHelper.Callback {
    protected abstract void move(int fromPosition, int toPosition);

    protected abstract void delete(int position);

    /**
     * 移动方式，true=swipe；false=drag
     */
    private boolean mActionTouch;
    private int mDefaultDragDirs;
    private int mDefaultSwipeDirs;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public CygItemTouchCallback(int dragDirs, int swipeDirs) {
        this(dragDirs, swipeDirs, null);
    }

    public CygItemTouchCallback(int dragDirs, int swipeDirs, SwipeRefreshLayout swipeRefreshLayout) {
        mDefaultDragDirs = dragDirs;
        mDefaultSwipeDirs = swipeDirs;
        mSwipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (mActionTouch) {
            if (viewHolder instanceof CygRecyclerViewHolder) {
                CygRecyclerViewHolder cygViewHolder = (CygRecyclerViewHolder) viewHolder;
                cygViewHolder.swipedClearView(recyclerView);
            }
        } else {
            if (background != null) viewHolder.itemView.setBackgroundDrawable(background);
        }
    }

    private Drawable background;

    @Override
    public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        // 拖拽或滑动时禁用下拉刷新，解决冲突
        setEnabledSwipeRefreshLayout(actionState != ItemTouchHelper.ACTION_STATE_IDLE);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (viewHolder instanceof CygRecyclerViewHolder) {
                CygRecyclerViewHolder cygViewHolder = (CygRecyclerViewHolder) viewHolder;
                cygViewHolder.swipedSelectedChanged(actionState);
            }
        } else if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (background == null) background = viewHolder.itemView.getBackground();
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
    }

    private void setEnabledSwipeRefreshLayout(boolean enabled) {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setEnabled(!enabled);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
        return ItemTouchHelper.Callback.makeMovementFlags(mDefaultDragDirs, mDefaultSwipeDirs);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (viewHolder instanceof CygRecyclerViewHolder) {
                CygRecyclerViewHolder cygViewHolder = (CygRecyclerViewHolder) viewHolder;
                cygViewHolder.swipedChildDraw(c, recyclerView, dX, dY, actionState, isCurrentlyActive);
            }
        } else
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
        mActionTouch = false;
        // 如果不是同一个视图，不允许移动
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(ViewHolder viewHolder, int direction) {
        mActionTouch = true;
        delete(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return mDefaultSwipeDirs > 0;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
}
