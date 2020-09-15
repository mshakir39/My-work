package com.example.poetrious;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomText extends androidx.appcompat.widget.AppCompatTextView {


    public CustomText(Context context) {
        super(context);
        initTypeface(context);
    }

    public CustomText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeface(context);
    }

    public CustomText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeface(context);
    }

    private void initTypeface(Context context)
    {
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"Transcity.otf");
        this.setTypeface(typeface);
    }
}
