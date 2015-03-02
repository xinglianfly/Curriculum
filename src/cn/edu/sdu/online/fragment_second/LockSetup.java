package cn.edu.sdu.online.fragment_second;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.Listener.ActionBarListener;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.utils.DefineUtil;
import cn.edu.sdu.online.view.LockPatternView;
import cn.edu.sdu.online.view.LockPatternView.Cell;
import cn.edu.sdu.online.view.LockPatternView.DisplayMode;
import cn.edu.sdu.online.view.LockPatternView.OnPatternListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class LockSetup extends Fragment implements OnPatternListener,
		ActionBarListener {

	private cn.edu.sdu.online.view.LockPatternView lockPatternView;
	// private Button leftButton;
	// private Button rightButton;

	private static final int STEP_1 = 1; //
	private static final int STEP_2 = 2; //
	private static final int STEP_3 = 3; //
	private static final int STEP_4 = 4; //
	private int step;

	private List<Cell> choosePattern;

	private boolean confirm = false;
	Main app;
	MainActivity activity;
	String can = "取消";
	String go = "";
	MenuItem item1;
	MenuItem item2;
	Menu menu;

	public LockSetup() {

	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
		app = Main.getApp();

	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.frag_locksetup, null);
		lockPatternView = (LockPatternView) view
				.findViewById(R.id.lock_pattern);
		lockPatternView.setOnPatternListener(this);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			activity.setActionBarListener(this);
			activity.invalidateOptionsMenu();
		}
		step = STEP_1;
		updateView();

		return view;
	}

	private void updateView() {
		switch (step) {
		case STEP_1:
			choosePattern = null;
			confirm = false;
			lockPatternView.clearPattern();
			lockPatternView.enableInput();
			break;
		case STEP_2:
			item1.setTitle("重试");
			item2.setTitle("继续");
			item2.setEnabled(true);
			lockPatternView.disableInput();
			break;
		case STEP_3:
			item1.setTitle("取消");
			item2.setTitle("");
			item2.setEnabled(false);
			lockPatternView.clearPattern();
			lockPatternView.enableInput();
			break;
		case STEP_4:
			item1.setTitle("取消");
			if (confirm) {
				item2.setTitle("确认");
				item2.setEnabled(true);
				lockPatternView.disableInput();
			} else {
				item2.setTitle("");
				lockPatternView.setDisplayMode(DisplayMode.Wrong);
				lockPatternView.enableInput();
				item2.setEnabled(false);
			}

			break;

		default:
			break;
		}
	}

	class leftButtonListener implements OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			if (step == STEP_1 || step == STEP_3 || step == STEP_4) {
				back();
			} else if (step == STEP_2) {
				step = STEP_1;
				updateView();
			}
			return true;
		}

	}

	class rightButtonListener implements OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			if (step == STEP_2) {
				step = STEP_3;
				updateView();
			} else if (step == STEP_4) {
				app.getDataStore()
						.edit()
						.putString("lock",
								LockPatternView.patternToString(choosePattern))
						.commit();
				app.getDataStore().edit().putBoolean("iflock", true).commit();
				uploadpassWord();
				activity.setAnimIn(R.anim.left_push_in);
				activity.setAnimOut(R.anim.left_push_out);
				LockFragment lockfragment = new LockFragment();
				activity.switchFragment(lockfragment);
				activity.getSupportActionBar().setTitle("输入手势");
				activity.setAnimIn(0);
				activity.setAnimOut(0);
			}
			return true;
		}

	}

	@Override
	public void onPatternStart() {
		System.out.println("onPatternStart");
	}

	@Override
	public void onPatternCleared() {
		System.out.println("onPatternCleared");
	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern) {
		System.out.println("onPatternCellAdded");
	}

	@Override
	public void onPatternDetected(List<Cell> pattern) {
		System.out.println("onPatternDETECTED");

		if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
			Toast.makeText(getActivity(), "至少4个", Toast.LENGTH_LONG).show();
			lockPatternView.setDisplayMode(DisplayMode.Wrong);
			return;
		}

		if (choosePattern == null) {
			choosePattern = new ArrayList<Cell>(pattern);
			System.out.println("choosePattern = "
					+ Arrays.toString(choosePattern.toArray()));
			step = STEP_2;
			updateView();
			return;
		}
		for (int i = 0; i < choosePattern.size(); i++) {
			System.out.println(choosePattern.get(i).getColumn() + "~~~~~~~~~"
					+ choosePattern.get(i).getRow());
		}
		System.out.println("choosePattern = "
				+ Arrays.toString(choosePattern.toArray()));
		System.out.println("pattern = " + Arrays.toString(pattern.toArray()));
		if (choosePattern.equals(pattern)) {
			System.out.println("pattern = "
					+ Arrays.toString(pattern.toArray()));
			confirm = true;
		}
		// if ((choosePattern.toString()).equals(pattern.toString())) {
		// System.out.println("pattern = "+Arrays.toString(pattern.toArray()));
		// confirm = true;
		// }
		else {
			confirm = false;
		}

		step = STEP_4;
		updateView();
	}

	public void back() {
		activity.setAnimIn(R.anim.right_push_in);
		activity.setAnimOut(R.anim.right_push_out);
		activity.switchFragment(activity.getMenuItems().get(Main.INT_SCORE).fragment);
		activity.setAnimIn(0);
		activity.setAnimOut(0);
		activity.setHomeButtonListener(null);
		activity.setBackKeyDownListener(null);
		activity.getSupportActionBar().setIcon(R.drawable.icon_topbar);
	}

	@Override
	public void onPrepare(Menu menu) {
		// TODO Auto-generated method stub
		System.out.println("zhixingle ");
		item2 = menu.add(go);
		item1 = menu.add(can);
		item2.setEnabled(false);
		item2.setOnMenuItemClickListener(new rightButtonListener());
		item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		item1.setOnMenuItemClickListener(new leftButtonListener());
		item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

	}

	@SuppressLint("NewApi")
	@Override
	public void onDestroyView() {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			activity.setActionBarListener(null);
			activity.invalidateOptionsMenu();
		}

		super.onDestroyView();
	}

	public void uploadpassWord() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("Type", "uploadGesture");
		params.addBodyParameter("id", app.getDataStore().getString("username", ""));
		params.addBodyParameter("gesture", LockPatternView.patternToString(choosePattern));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, DefineUtil.PATH, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(app, "保存手势密码失败,请检查网络设置", 2000).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(app, "保存密码成功", 2000).show();
					}
				});
	}
}
