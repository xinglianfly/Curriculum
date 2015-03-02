package cn.edu.sdu.online.app;

import java.util.TreeMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.Listener.ActionBarListener;
import cn.edu.sdu.online.Listener.BackPressedListener;
import cn.edu.sdu.online.Listener.ChangeToListener;
import cn.edu.sdu.online.Listener.HomeButtonListener;
import cn.edu.sdu.online.fragment_second.LockFragment;
import cn.edu.sdu.online.fragments_first.Curriculum;
import cn.edu.sdu.online.fragments_first.GradePoint;
import cn.edu.sdu.online.fragments_first.Menu;
import cn.edu.sdu.online.modal.LeftMenuItem;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import static cn.edu.sdu.online.app.Main.menuItems;
import static cn.edu.sdu.online.app.Main.menu_items;
/**
 * 主要的Activity，决定了整个程序的框架
 * 
 * @author seal
 */
public class MainActivity extends SlidingFragmentActivity {
	private int animIn, animOut;
	private Menu mFrag;
	private Fragment mContent;

	private HomeButtonListener homeButtonListener;
	private BackPressedListener backKeyDownListener;
	private ChangeToListener changeToListener;
	private Main app;
	private MenuAdapter menuAdapter;
	private MenuHandler menuHandler;
	private ActionBarListener actionBarListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UmengUpdateAgent.update(this);
		app = Main.getApp();

		// 设置侧拉菜单，每一项对应 （标题：fragment：图标）
		// 添加新的主页面的时候，直接在这边增加

		// 设置主界面
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new Curriculum();
		setContentView(R.layout.empty_container);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		// 设置侧拉菜单
		setBehindContentView(R.layout.empty_menu);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			mFrag = new Menu();
			t.replace(R.id.menu_frame, getMenu());
			t.commit();
		} else {
			mFrag = ((Menu) getSupportFragmentManager().findFragmentById(
					R.id.menu_frame));
		}

		// 自定义侧拉菜单
		getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
		getSlidingMenu().setShadowDrawable(R.drawable.shadow);

		// 设置actionbar的标题，图标
		getSupportActionBar().setTitle("课程表");
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_topbar);

		app.getDataStore().registerOnSharedPreferenceChangeListener(mFrag);

		menuAdapter = new MenuAdapter();
		menuHandler = new MenuHandler();

		getSlidingMenu().setOnOpenListener(new OnOpenListener() {

			@Override
			public void onOpen() {
				app.getInputMethodManager().hideSoftInputFromWindow(
						mFrag.getView().getWindowToken(), 0);
			}
		});

	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		if (actionBarListener != null) 
			actionBarListener.onPrepare(menu);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
			if (homeButtonListener == null)
				toggle();
			else {
				homeButtonListener.onHomeButtonClick();
			}
		return super.onOptionsItemSelected(item);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this.getBaseContext());
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this.getBaseContext());
	}

	// 使其按menu键时打开侧拉菜单
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			toggle();
		}
		return super.onKeyDown(keyCode, event);
	}

	// 切换fragment，可以在切换前设置animIn，animOut参数，使其产生切换效果
	public void switchFragment(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.setCustomAnimations(animIn, animOut)
				.replace(R.id.content_frame, mContent).attach(fragment).addToBackStack(null).commit();
	}

	public void switchFragment(Fragment fragment, int animIn, int animOut,
			String title) {
		if (title != null)
			getSupportActionBar().setTitle(title);
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.setCustomAnimations(animIn, animOut)
				.replace(R.id.content_frame, mContent).attach(fragment).addToBackStack(null).commit();
	}

	/**
	 * @return 侧栏菜单的清单
	 */
	public TreeMap<Integer, LeftMenuItem> getMenuItems() {
		return menuItems;
	}

	/**
	 * @return 得到侧拉菜单
	 */
	public Menu getMenu() {
		return (Menu) mFrag;
	}

	// 二级fragment切换
	public void changeTo(View view) {
		if (changeToListener != null)
			changeToListener.changTo(view);

	}

	public void GradePointButtonClick(View view) {
		((GradePoint) menuItems.get(Main.INT_GRADE_POINT).fragment)
				.ButtonClick(view);
	}

	// 按两次后退键，确认后退
	// 当有实现backKeyDownListener接口时，调用接口定义的onBackPressed方法
	@Override
	public void onBackPressed() {
		System.out.println(getSlidingMenu().isMenuShowing());
		System.out.println(getSlidingMenu().isSecondaryMenuShowing());
		if (backKeyDownListener != null) {
			backKeyDownListener.onBackPressed();
		} else {
			toggle();
		}

	}

	public void importCourses(View view) {
		if (app.getDataStore().getBoolean("isLogin", false)) {
			switchFragment(menuItems.get(Main.INT_INFO).fragment);
			getSupportActionBar().setTitle("个人信息");

		} else {
			switchFragment(menuItems.get(Main.INT_IMPORT).fragment);
			getSupportActionBar().setTitle("课程导入");
		}
		toggle();
	}

	public void exitApplication(View view) {
		finish();
	}

	public void setAnimIn(int animIn) {
		this.animIn = animIn;
	}

	public void setAnimOut(int animOut) {
		this.animOut = animOut;
	}

	public void setBackKeyDownListener(BackPressedListener backKeyDownListener) {
		this.backKeyDownListener = backKeyDownListener;
	}

	public void setHomeButtonListener(HomeButtonListener homeButtonListener) {
		this.homeButtonListener = homeButtonListener;
	}

	public void setChangeToListener(ChangeToListener changeToListener) {
		this.changeToListener = changeToListener;
	}

	public MenuAdapter getMenuAdapter() {
		return menuAdapter;
	}

	public void setMenuAdapter(MenuAdapter menuAdapter) {
		this.menuAdapter = menuAdapter;
	}

	public MenuHandler getMenuHandler() {
		return menuHandler;
	}

	public void setMenuHandler(MenuHandler menuHandler) {
		this.menuHandler = menuHandler;
	}

	public class MenuHandler implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position != menu_items.size() - 1) {

				if ((app.getDataStore().getBoolean("iflock", false) && position == menu_items
						.size() - 7)
						|| (app.getDataStore().getBoolean("iflock", false) && position == menu_items
								.size() - 6)) {
					if (position == menu_items.size() - 7) {
						app.getDataStore().edit().putString("whichfrag", "成绩")
								.commit();
					} else {
						app.getDataStore().edit().putString("whichfrag", "绩点")
								.commit();
					}
					LockFragment lockfragment = new LockFragment();
					switchFragment(lockfragment);
					getSupportActionBar().setTitle("输入手势");
					toggle();
					setAnimIn(0);
					setAnimOut(0);
				} else {
					switchFragment(menu_items.get(position).fragment);
					getSupportActionBar().setTitle(
							menu_items.get(position).title);
					toggle();
				}
			} else {
				exitApplication(view);
			}

		}
	}

	public class MenuAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return menu_items.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup parent) {
			contentView = getLayoutInflater().inflate(
					R.layout.list_item_icon_text, null);
			TextView textView = (TextView) contentView.findViewById(R.id.text);
			ImageView imageView = (ImageView) contentView
					.findViewById(R.id.icon);
			textView.setText(menu_items.get(position).title);
			imageView.setImageBitmap(menu_items.get(position).icon);
			return contentView;
		}

	}

	public void changeToLogin() {
		app.getDataStore().edit().putBoolean("isLogin", true).commit();
		menu_items.remove(menuItems.get(Main.INT_IMPORT));
		menu_items.add(menu_items.size() - 3, menuItems.get(Main.INT_INFO));
		menuAdapter.notifyDataSetChanged();
	}

	public void changeToLogout() {

		app.clearAll();// 清除成绩

		app.getDataStore().edit().putBoolean("isLogin", false).commit();
		menu_items.remove(menuItems.get(Main.INT_INFO));
		menu_items.add(menu_items.size() - 2, menuItems.get(Main.INT_IMPORT));
		menuAdapter.notifyDataSetChanged();// 当数据改变后，对View刷新数据

	}

	public ActionBarListener getActionBarListener() {
		return actionBarListener;
	}

	public void setActionBarListener(ActionBarListener actionBarListener) {
		this.actionBarListener = actionBarListener;
	}

}
