package com.example.pakaianbagus.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.FrameLayout;

@SuppressLint("ViewConstructor")
public class MyFrameLayout extends FrameLayout {

    public MyFrameLayout(Context context) {
        super(context);
    }

    public float getXFraction() {
        return getX() / getWidth(); // TODO: guard divide-by-zero
    }

    public void setXFraction(float xFraction) {
        // TODO: cache width
        final int width = getWidth();
        setX((width > 0) ? (xFraction * width) : -9999);
    }
}