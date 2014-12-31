package com.example.kvarnsen.footballtracker;

/**
 * Created by kvarnsen on 9/12/14.
 */
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/*
    Adapter for MainActivity's RecyclerView.

    Code adapted with alterations from https://developer.android.com/training/material/lists-cards.html
 */

public class HighlightAdapter extends RecyclerView.Adapter<HighlightAdapter.ViewHolder> {

    private ArrayList mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public ViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }
    }

    public HighlightAdapter(ArrayList myDataset) {

        mDataset = myDataset;
    }

    @Override
    public HighlightAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.highlightcard, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Highlight cur;

        CardView curCard = holder.mCardView;

        TextView curText = (TextView) curCard.findViewById(R.id.highlight_desc);
        ImageView curImg = (ImageView) curCard.findViewById(R.id.highlight_content);

        cur = (Highlight) mDataset.get(position);

        curCard.setId(position);
        curText.setText(cur.description);

        if(cur.bitmap != null)
            curImg.setImageBitmap(cur.bitmap);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}