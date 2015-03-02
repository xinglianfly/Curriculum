package cn.edu.sdu.online.fragments_first;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.Listener.ActionBarListener;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.fragment_second.LockSetup;
import cn.edu.sdu.online.fragments_first.Score.cancelpassword;
import cn.edu.sdu.online.fragments_first.Score.setPassword;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 成绩绩点的监听
 * 
 * @author lin
 * 
 */
public class GradePoint extends Fragment implements ActionBarListener {

	private Main app;
	MainActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = Main.getApp();
		activity = (MainActivity) getActivity();
		app.getDataStore().edit().putString("whichfrag", "绩点").commit();
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_grade_point, null);
		activity.setActionBarListener(this);
		activity.invalidateOptionsMenu();
		return view;
	}

	@SuppressLint("NewApi")
	@Override
	public void onDestroyView() {
		activity.setChangeToListener(null);
		activity.setActionBarListener(null);
		activity.invalidateOptionsMenu();
		super.onDestroyView();
	}

	public void ButtonClick(View view) {
		GradeMessage message = new GradeMessage();
		switch (view.getId()) {
		case R.id.year_1:
			message.setMessage(app.getDataStore().getFloat("year_1", 0) + "");
			break;
		case R.id.year_2:
			message.setMessage(app.getDataStore().getFloat("year_2", 0) + "");
			break;
		case R.id.year_3:
			message.setMessage(app.getDataStore().getFloat("year_3", 0) + "");
			break;
		case R.id.year_4:
			message.setMessage(app.getDataStore().getFloat("year_4", 0) + "");
			break;
		default:
			break;
		}
		message.show(getActivity().getSupportFragmentManager(), "gradepoint");
	}

	public static class GradeMessage extends DialogFragment {
		private CharSequence message = "Hello Seal";

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setStyle(STYLE_NO_TITLE, 0);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.dialog_grade_point,
					container, false);
			TextView textView = ((TextView) view
					.findViewById(R.id.alert_message));
			textView.setText("这一年你的绩点是：\n" + message);
			return view;
		}

		public void setMessage(CharSequence message) {
			this.message = message;
		}
	}

	String cancel = "取消密码";
	String setpass = "设置密码";
	MenuItem itemalter;
	MenuItem itemset;

	@Override
	public void onPrepare(Menu menu) {
		// TODO Auto-generated method stub
		boolean iflock = app.getDataStore().getBoolean("iflock", false);
		if (iflock) {
			itemalter = menu.add(cancel);
			itemalter.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			itemalter.setOnMenuItemClickListener(new cancelpassword());
		} else {
			itemset = menu.add(setpass);
			itemset.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			itemset.setOnMenuItemClickListener(new setPassword());
		}
	}

	class setPassword implements OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			activity.setAnimIn(R.anim.left_push_in);
			activity.setAnimOut(R.anim.left_push_out);
			LockSetup lock = new LockSetup();
			activity.switchFragment(lock);
			activity.getSupportActionBar().setTitle("设置密码");
			activity.setAnimIn(0);
			activity.setAnimOut(0);

			return true;
		}

	}

	class cancelpassword implements OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			app.getDataStore().edit().remove("iflock").remove("lock").commit();
			itemalter.setTitle("设置密码");
			itemalter.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			itemalter.setOnMenuItemClickListener(new setPassword());
			return true;
		}

	}

}
