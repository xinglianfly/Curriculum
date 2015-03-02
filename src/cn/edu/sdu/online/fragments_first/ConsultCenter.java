package cn.edu.sdu.online.fragments_first;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.Listener.ActionBarListener;
import cn.edu.sdu.online.activity.PublishHomework;
import cn.edu.sdu.online.activity.ReleaseQuestion;
import cn.edu.sdu.online.activity.signActivity;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.tab.ReleaseHomework;
import cn.edu.sdu.online.tab.SignTest;
import cn.edu.sdu.online.tab.loadQuestion;
import cn.edu.sdu.online.utils.DateUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

public class ConsultCenter extends Fragment {

	TabHost mTabHost;
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	List<View> views;
	static MainActivity mactivity;
	static Main app;
	static DateUtil utils;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		utils = new DateUtil();

	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		mactivity = (MainActivity) getActivity();
		app = Main.getApp();
		View view = inflater.inflate(R.layout.sub_fragment_tab_pager, null);
		mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mViewPager = (ViewPager) view.findViewById(R.id.sub_pager);

		mTabsAdapter = new TabsAdapter(ConsultCenter.this, mTabHost, mViewPager);

		mTabsAdapter.addTab(mTabHost.newTabSpec("rank").setIndicator("签到"),
				SignTest.class, null);
		mTabsAdapter.addTab(mTabHost.newTabSpec("homework").setIndicator("问答"),
				loadQuestion.class, null);
		mTabsAdapter.addTab(mTabHost.newTabSpec("release").setIndicator("作业"),
				ReleaseHomework.class, null);

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
		return view;
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost. It relies on a
	 * trick. Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show. This is not sufficient for switching
	 * between pages. So instead we make the content part of the tab host 0dp
	 * high (it is not shown) and the TabsAdapter supplies its own dummy view to
	 * show as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct paged in the ViewPager whenever the selected tab
	 * changes.
	 */

	public static class TabsAdapter extends FragmentPagerAdapter implements
			TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener,
			ActionBarListener {
		private final Context mContext;
		private final TabHost mTabHost;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
		String[] title = new String[] { "签到", "问答", "作业" };
		int[] icon = new int[] { R.drawable.icon_sign,
				R.drawable.icon_question, R.drawable.icon_homework };

		static String singletitle;
		static int singleicon;

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabsAdapter(Fragment activity, TabHost tabHost, ViewPager pager) {
			super(activity.getChildFragmentManager());
			mContext = activity.getActivity();
			mTabHost = tabHost;
			mViewPager = pager;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);

		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		@SuppressLint("NewApi")
		@Override
		public void onTabChanged(String tabId) {
			int position = mTabHost.getCurrentTab();
			mViewPager.setCurrentItem(position);
			singletitle = title[position];
			singleicon = icon[position];
			mactivity.setActionBarListener(this);
			mactivity.invalidateOptionsMenu();

		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			// Unfortunately when TabHost changes the current tab, it kindly
			// also takes care of putting focus on it when not in touch mode.
			// The jerk.
			// This hack tries to prevent this from pulling focus out of our
			// ViewPager.
			TabWidget widget = mTabHost.getTabWidget();
			int oldFocusability = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			mTabHost.setCurrentTab(position);
			widget.setDescendantFocusability(oldFocusability);

		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onPrepare(Menu menu) {
			// TODO Auto-generated method stub
			MenuItem menuitem = menu.add(singletitle);
			menuitem.setIcon(singleicon);
			menuitem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			if (singletitle.equals("签到")) {
				menuitem.setOnMenuItemClickListener(new SignLister());
			} else if (singletitle.equals("问答")) {
				menuitem.setOnMenuItemClickListener(new releaseQuestion());
			} else {
				menuitem.setOnMenuItemClickListener(new releseHomeworkListener());
			}
		}

		class SignLister implements OnMenuItemClickListener {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				// if (ifSign()) {
				if (app.getDataStore().getBoolean("isLogin", false)) {
					Intent intent = new Intent();
					intent.setClass(mactivity, signActivity.class);
					mactivity.startActivity(intent);
				} else {
					Toast.makeText(mactivity, "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
				app.getDataStore().edit().putString("signif", "1")
						.putInt("signday", utils.getDay()).commit();
				// } else {
				// Toast.makeText(mactivity, "今天已签到", Toast.LENGTH_SHORT)
				// .show();
				// }
				return true;
			}

		}

		public boolean ifSign() {
			boolean ifenable = true;
			int lastday = app.getDataStore().getInt("signday", 0);
			int today = utils.getDay();
			System.out.println(today + "::::" + lastday);
			if (lastday != today) {
				ifenable = true;
			} else {
				ifenable = false;
			}
			return ifenable;
		}

		class releseHomeworkListener implements OnMenuItemClickListener {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				if (app.getDataStore().getBoolean("isLogin", false)) {
					Intent intent = new Intent();
					intent.setClass(mactivity, PublishHomework.class);
					mactivity.startActivity(intent);

				} else {
					Toast.makeText(mactivity, "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
				return true;
			}

		}

		class releaseQuestion implements OnMenuItemClickListener {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (app.getDataStore().getBoolean("isLogin", false)) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(mactivity, ReleaseQuestion.class);
					mactivity.startActivity(intent);
				} else {
					Toast.makeText(mactivity, "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
				return true;
			}

		}

	}

	@SuppressLint("NewApi")
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		mactivity.setActionBarListener(null);
		mactivity.invalidateOptionsMenu();
		super.onDestroyView();
	}

}
