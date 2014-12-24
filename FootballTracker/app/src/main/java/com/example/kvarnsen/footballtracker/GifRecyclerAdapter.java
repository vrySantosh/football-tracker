package com.example.kvarnsen.footballtracker;

/**
 * Created by kvarnsen on 9/12/14.
 */
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/*
    Adapter for MainActivity's RecyclerView.

    Code adapted with alterations from https://developer.android.com/training/material/lists-cards.html
 */

public class GifRecyclerAdapter extends RecyclerView.Adapter<GifRecyclerAdapter.ViewHolder> {

    private ArrayList mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public ViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }
    }

    public GifRecyclerAdapter(ArrayList myDataset) {

        mDataset = myDataset;
    }

    @Override
    public GifRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.videocard, parent, false);

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

        curImg.setImageBitmap(cur.bitmap);

        /*
        curText.setClickable(true);
        curText.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='" + cur.url + "'>" + cur.description + "</a>";
        curText.setText(Html.fromHtml(text));
        */

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}