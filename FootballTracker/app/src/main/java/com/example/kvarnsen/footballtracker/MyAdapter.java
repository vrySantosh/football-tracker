package com.example.kvarnsen.footballtracker;

/**
 * Created by kvarnsen on 9/12/14.
 */
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/*
    Code adapted with alterations from https://developer.android.com/training/material/lists-cards.html
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mTextView;
        public ViewHolder(CardView v) {
            super(v);
            mTextView = v;
        }
    }

    public MyAdapter(ArrayList myDataset) {

        mDataset = myDataset;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Game cur;

        CardView curCard = holder.mTextView;

        TextView curText = (TextView) curCard.findViewById(R.id.text);
        TextView curHeading = (TextView) curCard.findViewById(R.id.date);

        cur = (Game) mDataset.get(position);

        curHeading.setText(cur.dateStr);
        curText.setText(cur.details);

    }

    @Override
    public int getItemCount() {

        return mDataset.size();
    }
}