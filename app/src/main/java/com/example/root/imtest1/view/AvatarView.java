package com.example.root.imtest1.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.root.imtest1.R;
import com.example.root.imtest1.tools.ApiLevelHelper;

/**
 * Created by root on 16-4-28.
 */
public class AvatarView extends ImageView {

    private boolean mChecked;

    public AvatarView(Context context) {
        this(context, null);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Set the image for this avatar. Will be used to create a round version of this avatar.
     *
     * @param resId The image's resource id.
     */
    @SuppressLint("NewApi")
    public void setAvatar(@DrawableRes int resId) {
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            //可以使用View.setClipToOutline方法去剪切一个视图的outline区域。
            setClipToOutline(true);
            setImageResource(resId);
        } else {
            //5.0以下的版本使用自行实现的方法来裁剪
            setAvatarPreLollipop(resId);
        }
    }

    //裁剪圆形
    private void setAvatarPreLollipop(@DrawableRes int resId) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), resId,
                getContext().getTheme());
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        @SuppressWarnings("ConstantConditions")
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(),
                bitmapDrawable.getBitmap());
        roundedDrawable.setCircular(true);
        setImageDrawable(bitmapDrawable);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (mChecked) {
            Drawable border = ContextCompat.getDrawable(getContext(), R.drawable.selector_avater);
            border.setBounds(0, 0, getWidth(), getHeight());
            border.draw(canvas);
        }
    }

    @Override
    @SuppressLint("NewApi")
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w > 0 && h > 0) {
            setOutlineProvider(new RoundOutlineProvider(Math.min(w, h)));
        }
    }


}

