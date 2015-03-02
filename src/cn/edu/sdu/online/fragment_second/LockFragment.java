package cn.edu.sdu.online.fragment_second;

import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.view.*;
import cn.edu.sdu.online.view.LockPatternView.Cell;
import cn.edu.sdu.online.view.LockPatternView.DisplayMode;

public class LockFragment extends Fragment implements
		LockPatternView.OnPatternListener {
	private static final String TAG = "LockActivity";

	private List<Cell> lockPattern;
	private LockPatternView lockPatternView;
	Main app;
	MainActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = Main.getApp();
		activity = (MainActivity) getActivity();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		System.out.println("zhi xingl view");
		String patternString = app.getDataStore().getString("lock", null);
		System.out.println(patternString+"lockpattern");
		lockPattern = LockPatternView.stringToPattern(patternString);
		View view = inflater.inflate(R.layout.frag_lock, null);
		lockPatternView = (LockPatternView) view
				.findViewById(R.id.lock_pattern);
		lockPatternView.setOnPatternListener(this);
		return view;
	}

	@Override
	public void onPatternStart() {
		Log.d(TAG, "onPatternStart");
	}

	@Override
	public void onPatternCleared() {
		Log.d(TAG, "onPatternCleared");
	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern) {
		Log.d(TAG, "onPatternCellAdded");
		Log.e(TAG, LockPatternView.patternToString(pattern));
		// Toast.makeText(this, LockPatternView.patternToString(pattern),
		// Toast.LENGTH_LONG).show();
	}

	@Override
	public void onPatternDetected(List<Cell> pattern) {
		Log.d(TAG, "onPatternDetected");

		if (pattern.equals(lockPattern)) {
			String whichfrag = app.getDataStore().getString("whichfrag", null);
			if (whichfrag.equals("≥…º®")) {
				back(Main.INT_SCORE);
			} else {
				back(Main.INT_GRADE_POINT);
			}
		}

		else {
			lockPatternView.setDisplayMode(DisplayMode.Wrong);
			Toast.makeText(getActivity(), " ‰»Î¥ÌŒÛ", Toast.LENGTH_LONG).show();
		}

	}

	public void back(int i) {
		activity.setAnimIn(R.anim.right_push_in);
		activity.setAnimOut(R.anim.right_push_out);
		activity.switchFragment(activity.getMenuItems().get(i).fragment);
		activity.setAnimIn(0);
		activity.setAnimOut(0);
		activity.setHomeButtonListener(null);
		activity.setBackKeyDownListener(null);
		activity.getSupportActionBar().setIcon(R.drawable.icon_topbar);
		activity.getSupportActionBar().setTitle(Main.menuItems.get(i).title);
	}
}
