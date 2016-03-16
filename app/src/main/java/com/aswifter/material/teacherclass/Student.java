package com.aswifter.material.teacherclass;

import android.graphics.Bitmap;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/12/14
 */
public class Student {
    String text;

    Bitmap bitmap;
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
