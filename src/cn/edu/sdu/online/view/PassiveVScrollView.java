package cn.edu.sdu.online.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class PassiveVScrollView extends ScrollView {

	public PassiveVScrollView(Context context) {
		super(context);
	}

	public PassiveVScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PassiveVScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	//不让其自己滑动，要跟随mainboard滑动
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

}
