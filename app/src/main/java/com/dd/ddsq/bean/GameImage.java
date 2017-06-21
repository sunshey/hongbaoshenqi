package com.dd.ddsq.bean;

import android.net.Uri;

/**
 * Created by wanglin  on 2017/3/20 15:04.
 */
public class GameImage {
    private Uri imgUri;
    private String title;

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
