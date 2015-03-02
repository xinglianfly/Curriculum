package cn.edu.sdu.online.fragments_first;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.Adapter.LoadCurriculumnAdapter;
import cn.edu.sdu.online.Listener.ActionBarListener;
import cn.edu.sdu.online.Listener.OmniScrollListener;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.http_deal.InformationHttp;
import cn.edu.sdu.online.service.NotificationService;
import cn.edu.sdu.online.superXueba.StartActivity;
import cn.edu.sdu.online.view.OmniScrollView;
import cn.edu.sdu.online.view.PassiveHScrollView;
import cn.edu.sdu.online.view.PassiveVScrollView;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 锟轿憋拷牟锟斤拷锟�
 * 
 * @author
 * 
 * 
 */
public class Curriculum extends Fragment implements OmniScrollListener,
		OnMenuItemClickListener, ActionBarListener {
	InformationHttp httputil;
	private GridView gridView;
	private PassiveHScrollView upBoard;// 锟斤拷锟矫课憋拷锟斤拷锟斤拷锟斤拷遣锟斤拷植锟斤拷锟斤拷锟姐滑锟斤拷
	private PassiveVScrollView leftBoard;// 锟斤拷锟矫课憋拷锟斤拷锟斤拷锟角诧拷锟街诧拷锟斤拷锟斤拷慊拷锟�
	private OmniScrollView mainBoard;// 锟轿憋拷锟斤拷屑锟侥诧拷锟街匡拷锟斤拷锟斤拷慊拷锟�

	private int gridheight;
	private int gridwidth;
	private Main app;
	private Drawable[] colors;
	private String[] classNameList;
	private int[] intColors;
	private BaseAdapter adapter;
	private int numClasses;
	int offset;
	MainActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = Main.getApp();
		colors = app.getColors();
		intColors = app.getIntColors();
		app.getDataStore().edit().remove("flagrankidlatest")
				.remove("flagrankidoldest").commit();
		classNameList = Main.getApp().getCurriculumArray();
		gridwidth = (int) (getActivity().getResources().getDisplayMetrics().density * 80 + 0.5f);
		gridheight = (int) (getActivity().getResources().getDisplayMetrics().density * 120 + 0.5f);
		activity = (MainActivity) getActivity();
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_curriculum, null);
		upBoard = (PassiveHScrollView) view.findViewById(R.id.up_board);
		leftBoard = (PassiveVScrollView) view.findViewById(R.id.left_board);
		mainBoard = (OmniScrollView) view.findViewById(R.id.main_board);
		upBoard.setHorizontalScrollBarEnabled(false);// 取锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷示
		leftBoard.setVerticalScrollBarEnabled(false);
		mainBoard.setScrollViewListener(this);// 锟斤拷锟矫硷拷锟斤拷
		// 锟轿憋拷锟叫硷拷锟斤拷锟斤拷锟斤拷癫季锟�
		gridView = (GridView) view.findViewById(R.id.dataGrid);
		gridView.setOnItemClickListener(new gridListener());
		adapter = new LoadCurriculumnAdapter(getActivity(), classNameList,
				colors, intColors, gridheight, gridwidth);
		gridView.setAdapter(adapter);
		storeclassNum();
		activity.setActionBarListener(this);
		activity.invalidateOptionsMenu();
		return view;
	}

	@Override
	public void onScrollChanged(OmniScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		upBoard.scrollTo(x, 0);
		leftBoard.scrollTo(0, y);

	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), StartActivity.class);
		getActivity().startActivity(intent);
		return true;
	}

	@Override
	public void onPrepare(Menu menu) {
		MenuItem changeColors = menu.add("超级学霸模式");
		changeColors.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		changeColors.setOnMenuItemClickListener(this);
	}

	public class gridListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (classNameList[position] != null) {
				CurriculumMessage message = new CurriculumMessage(position);
				message.show(getFragmentManager(), "curriculummessage");
			} else {
				return;
			}
		}
	}

	@SuppressLint("ValidFragment")
	public class CurriculumMessage extends DialogFragment {
		int position;

		@SuppressLint("ValidFragment")
		public CurriculumMessage(int position) {
			this.position = position;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setStyle(STYLE_NO_TITLE, 0);
		}

		@SuppressLint("NewApi")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.dialog_curriculum, container,
					false);
			TextView textView = ((TextView) view
					.findViewById(R.id.curriculum_dialog));
			textView.setText(classNameList[position]);
			textView.setBackground(colors[intColors[position]]);
			return view;
		}

	}

	public int numClasses(int iWay) {
		for (int i = 0; i < 5; i++) {
			if ((app.curriculumArray[i * 7 + (iWay) - 1]) != null) {
				numClasses++;

			}
		}
		return numClasses;
	}

	@SuppressLint("SimpleDateFormat")
	public void storeclassNum() {
		String we[] = new String[] { "", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday", "Sunday" };
		int iWay = 0;
		if (app.getDataStore().getBoolean("isLogin", false)) {
			for (iWay = 1; iWay <= 7; iWay++) {
				numClasses = 0;
				app.getDataStore().edit().putInt(we[iWay], numClasses(iWay))
						.commit();
			}
			// app.caldate();

			AlarmManager am = (AlarmManager) getActivity().getSystemService(
					Context.ALARM_SERVICE);
			Intent intent = new Intent(getActivity(), NotificationService.class);
			intent.setAction("ALARM_ACTION");
			PendingIntent pendingIntent = PendingIntent.getService(
					getActivity(), 0, intent, 0);
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
					+ app.caloffset(), 24 * 60 * 60 * 1000, pendingIntent);

		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		activity.setActionBarListener(null);
		activity.invalidateOptionsMenu();
		super.onDestroyView();
	}

}
