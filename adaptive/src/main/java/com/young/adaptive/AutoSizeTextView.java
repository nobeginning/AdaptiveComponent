package com.young.adaptive;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import static com.young.adaptive.AdaptiveComponentKt.TAG_TEXT_SIZE_AUTO_LAYOUT;

/**
 * Created by young on 2017/12/8.
 */

public final class AutoSizeTextView extends android.support.v7.widget.AppCompatTextView {
    public AutoSizeTextView(Context context) {
        super(context);
    }

    public AutoSizeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoSizeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void addAutoSizedTag(){
        setTag(R.id.text_view_auto_size, TAG_TEXT_SIZE_AUTO_LAYOUT);
    }
}
