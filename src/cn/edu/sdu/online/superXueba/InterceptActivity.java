package cn.edu.sdu.online.superXueba;

import cn.edu.sdu.online.R;
import android.app.Activity;
import android.os.Bundle;

public class InterceptActivity extends Activity {
	public static InterceptActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bazinga);
		instance = this;
	}

}
