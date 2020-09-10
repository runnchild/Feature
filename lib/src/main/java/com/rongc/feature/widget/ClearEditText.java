package com.rongc.feature.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.SizeUtils;
import com.rongc.feature.R;

/**
 * Desc: 带清除功能的EditText
 * <p>
 * Date: 2018-07-03
 * Copyright: Copyright (c) 2013-2018
 * Company: @米冠网络
 * Update Comments:
 */
public class ClearEditText extends AppCompatEditText implements OnFocusChangeListener {

    private Drawable mClearDrawable;
    private int touchAreaAddition;
    private onClearClickListener mOnClearClickListener;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    private void init() {
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = ContextCompat.getDrawable(getContext(), R.mipmap.base_ic_edit_text_clear);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setClearIconVisible(s.length() > 0);
                if (mOnClearClickListener != null && s.length() == 0) {
                    mOnClearClickListener.onCancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        touchAreaAddition = SizeUtils.dp2px(10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null && event.getAction() == MotionEvent.ACTION_UP) {
            boolean touchable = event.getX() > (getWidth() - touchAreaAddition - getPaddingRight()
                    - mClearDrawable.getIntrinsicWidth()) && (event.getX() < getWidth());
            if (touchable) {
                setText("");
            }
            if (mOnClearClickListener != null) {
                mOnClearClickListener.onCancel();
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawables(drawables[0],
                drawables[1], right, drawables[3]);
    }

    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    public void setClearIconVisible() {
        mClearDrawable = null;
        setClearIconVisible(false);
    }

    public void setOnClearClickListener(onClearClickListener mOnClearClickListener) {
        this.mOnClearClickListener = mOnClearClickListener;
    }

    public interface onClearClickListener {
        void onCancel();
    }
}