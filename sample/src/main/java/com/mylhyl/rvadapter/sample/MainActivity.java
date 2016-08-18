package com.mylhyl.rvadapter.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mylhyl.rvadapter.CygRecyclerViewAdapter;
import com.mylhyl.rvadapter.CygRecyclerViewListener;
import com.mylhyl.rvadapter.touch.CygItemTouchCallback;
import com.mylhyl.rvadapter.touch.CygRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Student> data = new ArrayList<>();
    private CygRecyclerViewAdapter<Student> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        data.add(new Student("Item1"));
        data.add(new Student("Item2"));
        data.add(new Student("Item3"));
        data.add(new Student("Item4"));
        data.add(new Student("Item5"));
        data.add(new Student("Item6"));
        data.add(new Student("Item7"));
        data.add(new Student("Item8"));
        data.add(new Student("Item9"));


        mAdapter = new CygRecyclerViewAdapter<Student>(this,R.layout.recycler_view_item, data) {

            @Override
            public void onBindData(CygRecyclerViewHolder viewHolder, Student item, int position) {
                TextView tvName = viewHolder.findViewById(android.R.id.text1);
                tvName.setText(item.name);
                viewHolder.setSwipedOverlayView(LayoutInflater.from(MainActivity.this).inflate(R.layout.swipe_del, null));
            }
        };
        mAdapter.setCygRecyclerViewListener(new CygRecyclerViewListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(mAdapter);

        CygItemTouchCallback itemTouchCallback = new CygItemTouchCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START) {
            @Override
            protected void move(int fromPosition, int toPosition) {
                mAdapter.move(fromPosition, toPosition);
            }

            @Override
            protected void delete(int position) {
                mAdapter.delete(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
