package com.stenleone.innakozak.recycler;

import com.stenleone.innakozak.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<CardScript> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.card_text_1);
            mTextView2 = itemView.findViewById(R.id.card_text_2);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public RecyclerAdapter(ArrayList<CardScript> exampleList) {
        mExampleList = exampleList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int lay;
        if(parent.getContext().toString().contains("Activity1"))
            lay = R.layout.card_1;
        else
            lay = R.layout.card_2;
        View v = LayoutInflater.from(parent.getContext()).inflate(lay, parent, false);
        ViewHolder evh = new ViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardScript currentItem = mExampleList.get(position);

        holder.mTextView1.setText(currentItem.getText1());
        if(currentItem.getText2() != null) {
            holder.mTextView2.setText(currentItem.getText2());
            holder.mTextView2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}