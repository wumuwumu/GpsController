package com.sciento.wumu.gpscontroller.View;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.sciento.wumu.gpscontroller.R;

/**
 * 清理edittext中的值
 */
public class CleanEditText extends AppCompatEditText {
	private static final int DRAWABLE_LEFT = 0;
	private static final int DRAWABLE_TOP = 1;
	private static final int DRAWABLE_RIGHT = 2;
	private static final int DRAWABLE_BOTTOM = 3;
	private Drawable mClearDrawable;

	public CleanEditText(Context context) {
		super(context);
		init();
	}

	public CleanEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CleanEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mClearDrawable = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_clear_black_24dp,null);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		setClearIconVisible(hasFocus() && text.length() > 0);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		setClearIconVisible(focused && length() > 0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				Drawable drawable = getCompoundDrawables()[DRAWABLE_RIGHT];
				if (drawable != null && event.getX() <= (getWidth() - getPaddingRight())
						&& event.getX() >= (getWidth() - getPaddingRight() - drawable.getBounds().width())) {
					setText("");
				}
				break;
		}
		return super.onTouchEvent(event);
	}

	private void setClearIconVisible(boolean visible) {
		setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[DRAWABLE_LEFT], getCompoundDrawables()[DRAWABLE_TOP],
				visible ? mClearDrawable : null, getCompoundDrawables()[DRAWABLE_BOTTOM]);
	}
}


