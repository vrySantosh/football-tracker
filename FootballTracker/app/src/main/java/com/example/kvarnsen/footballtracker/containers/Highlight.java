package com.example.kvarnsen.footballtracker.containers;

import android.graphics.Bitmap;

/**
 * Created by joshuapancho on 24/12/14.
 */

/*
    Holds info about each gfycat highlight
 */

public class Highlight {

    public String description;
    public String url;
    public Bitmap bitmap;

    public Highlight(String new_desc, String new_url, Bitmap new_bitmap) {
        description = new_desc;
        url = new_url;
        bitmap = new_bitmap;
    }

}
