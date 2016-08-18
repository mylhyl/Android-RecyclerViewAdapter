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

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mylhyl.rvadapter.CygRecyclerViewListener;

public final class CygRecyclerViewHolder extends RecyclerView.ViewHolder implements OnClickListener, OnLongClickListener {
    private Context mContext;
    private View mItemView;
    private CygRecyclerViewListener mListener;
    public View mSwipedOverlayView;// 滑动时显示的覆盖层

    private CygRecyclerViewHolder(Context context, View itemView, CygRecyclerViewListener listener) {
        super(itemView);
        this.mContext = context;
        this.mItemView = itemView;
        this.mListener = listener;
        if (mListener != null) {
            mItemView.setOnClickListener(this);
            mItemView.setOnLongClickListener(this);
        }
    }

    public static CygRecyclerViewHolder get(Context context, ViewGroup parent, int layoutId
            , CygRecyclerViewListener listener) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        CygRecyclerViewHolder holder = new CygRecyclerViewHolder(context, itemView, listener);
        return holder;
    }

    /**
     * 设置滑动覆盖层
     *
     * @param swipedOverlayView
     * @author hupei
     * @date 2015年8月5日 上午9:54:14
     */
    public void setSwipedOverlayView(View swipedOverlayView) {
        this.mSwipedOverlayView = swipedOverlayView;
        if (itemView instanceof ViewGroup && mSwipedOverlayView != null) {
            ViewGroup root = (ViewGroup) itemView;
            root.addView(mSwipedOverlayView);
        } else {
            Log.v("CygRecyclerViewHolder", "itemView is not layout");
        }
    }

    /**
     * 滑动选择，显示覆盖层
     *
     * @param actionState
     * @author hupei
     * @date 2015年8月5日 上午10:55:28
     */
    public void swipedSelectedChanged(int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && mSwipedOverlayView != null) {
            View overlay = mSwipedOverlayView;
            overlay.setTranslationX(itemView.getWidth());
            overlay.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 滑动选择，隐藏覆盖层
     *
     * @param recyclerView
     * @author hupei
     * @date 2015年8月5日 上午10:55:28
     */
    public void swipedClearView(RecyclerView recyclerView) {
        if (mSwipedOverlayView != null) {
            View overlay = mSwipedOverlayView;
            overlay.setVisibility(View.GONE);
        }
    }

    /**
     * 滑动覆盖层的动画
     *
     * @param c
     * @param recyclerView
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     * @return
     * @author hupei
     * @date 2015年8月5日 上午10:26:50
     */
    public void swipedChildDraw(Canvas c, RecyclerView recyclerView, float dX, float dY, int actionState,
                                boolean isCurrentlyActive) {
        if (mSwipedOverlayView == null)
            return;
        final View overlay = mSwipedOverlayView;
        final float dir = Math.signum(dX);
        if (dir == 0) {
            overlay.setTranslationX(-overlay.getWidth());
        } else {
            final float overlayOffset = dX - dir * itemView.getWidth();
            overlay.setTranslationX(overlayOffset);
        }
        float alpha = (float) (.2 + .8 * Math.abs(dX) / itemView.getWidth());
        overlay.setAlpha(alpha);
    }

    public CygRecyclerViewHolder setText(int viewId, String text) {
        View view = findViewById(viewId);
        if (view instanceof TextView) {
            final TextView textView = (TextView) view;
            textView.setText(text);
        }
        return this;
    }

    public <T extends View> T findViewById(int id) {
        return (T) itemView.findViewById(id);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null)
            mListener.onItemClickListener(v, getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        if (mListener != null)
            mListener.onItemLongClickListener(v, getAdapterPosition());
        return true;
    }
}