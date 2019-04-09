package com.wy.remote.remotemodel.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wy.remote.remotemodel.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * 版本：V1.2.5
 * 时间： 2018/5/23 16:42
 * 创建人：laoqb
 * 作用：
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    /**
     * 展示数据
     */
    private ArrayList<Map<String, Object>> mData;

    /**
     * 事件回调监听
     */
    private RecyclerViewAdapter.OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(ArrayList<Map<String, Object>> data) {
        this.mData = data;
    }

    public void updateData(ArrayList<Map<String, Object>> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    /**
     * 添加新的Item
     */
    public void addNewItem() {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        notifyItemInserted(0);
    }

    /**
     * 删除Item
     */
    public void deleteItem() {
        if (mData == null || mData.isEmpty()) {
            return;
        }
        mData.remove(0);
        notifyItemRemoved(0);
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(RecyclerViewAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        // 实例化viewholder
        RecyclerViewAdapter.MyViewHolder viewHolder = new RecyclerViewAdapter.MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.MyViewHolder holder, int position) {
        // 绑定数据
        holder.name_tv.setText(mData.get(position).get("name")+"");
        holder.pass_tv.setText(mData.get(position).get("pass")+"");
        holder.phone_tv.setText(mData.get(position).get("phone")+"");
        holder.name_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });

        holder.pass_tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                }
                //表示此事件已经消费，不会触发单击事件
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name_tv;
        TextView pass_tv;
        TextView phone_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            name_tv = itemView.findViewById(R.id.name_tv);
            pass_tv = itemView.findViewById(R.id.pass_tv);
            phone_tv = itemView.findViewById(R.id.phone_tv);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}