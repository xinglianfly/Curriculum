package cn.edu.sdu.online.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class PassiveHScrollView extends HorizontalScrollView {

	public PassiveHScrollView(Context context) {
		super(context);
	}

	public PassiveHScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PassiveHScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	//不让其自己随便滑动，要跟随mainboard滑动
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return false;
	}

}
