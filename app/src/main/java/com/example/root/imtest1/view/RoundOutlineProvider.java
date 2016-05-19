package com.example.root.imtest1.view;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * Created by root on 16-4-28.
 */
public class RoundOutlineProvider extends ViewOutlineProvider {

    private final int mSize;

    public RoundOutlineProvider(int size) {
        if (0 > size) {
            throw new IllegalArgumentException("size needs to be > 0. Actually was " + size);
        }
        mSize = size;
    }

    @Override
    public final void getOutline(View view, Outline outline) {
        outline.setOval(0, 0, mSize, mSize);
    }

}