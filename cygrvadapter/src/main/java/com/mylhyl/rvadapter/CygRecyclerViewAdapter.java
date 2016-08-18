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

package com.mylhyl.rvadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.mylhyl.rvadapter.touch.CygRecyclerViewHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class CygRecyclerViewAdapter<T> extends RecyclerView.Adapter<CygRecyclerViewHolder> {
    /**
     * 绑定数据
     *
     * @param viewHolder
     * @param item
     * @param position
     * @author hupei
     * @date 2015年8月5日 上午11:45:04
     */
    public abstract void onBindData(CygRecyclerViewHolder viewHolder, T item, int position);

    protected Context mContext;
    protected CygRecyclerViewListener mListener;
    protected List<T> mObjects;
    protected int mResource;


    public CygRecyclerViewAdapter(Context context, int resource, List<T> data) {
        this.mContext = context;
        this.mResource = resource;
        this.mObjects = data;
    }

    public void setCygRecyclerViewListener(CygRecyclerViewListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        if (mObjects == null)
            return 0;
        return mObjects.size();
    }

    @Override
    public void onBindViewHolder(CygRecyclerViewHolder viewHolder, int position) {
        onBindData(viewHolder, mObjects.get(position), position);
    }

    @Override
    public CygRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CygRecyclerViewHolder viewHolder = CygRecyclerViewHolder.get(mContext, parent, mResource, mListener);
        return viewHolder;
    }


    public synchronized void add(T object) {
        if (object != null) {
            mObjects.add(object);
            notifyItemInserted(getItemCount());
        }
    }

    public final void insert(T object, int index) {
        if (object != null) {
            mObjects.add(index, object);
            notifyItemInserted(index);
        }
    }

    public void addAll(Collection<T> collection) {
        if (collection != null) {
            mObjects.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public final void sort(Comparator<? super T> comparator) {
        Collections.sort(mObjects, comparator);
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mObjects.get(position);
    }

    public void remove(T item) {
        delete(mObjects.indexOf(item));
    }

    public void clear() {
        mObjects.clear();
        notifyDataSetChanged();
    }

    public List<T> getObjects() {
        return mObjects;
    }


    public synchronized void delete(int position) {
        mObjects.remove(position);
        notifyItemRemoved(position);
    }


    public void move(int fromPosition, int toPosition) {
        Collections.swap(mObjects, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

}
