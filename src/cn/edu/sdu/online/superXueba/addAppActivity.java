package cn.edu.sdu.online.superXueba;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.superXueba.BrowseApplicationInfoAdapter.ViewHolder;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class addAppActivity extends Activity implements OnItemClickListener {

	private ListView listview = null;
	Button confirm, cancel;
	private List<AppInfo> mlistAppInfo = null;
	ArrayList<AppInfo> list = new ArrayList<AppInfo>();
	List<AppInfo> appInfos; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_app_list);

		listview = (ListView) findViewById(R.id.listviewApp);
		appInfos = new ArrayList<AppInfo>(); // 保存过滤查到的AppInfo
		mlistAppInfo = new ArrayList<AppInfo>();
		queryAppInfo(); // 查询所有应用程序信息
		BrowseApplicationInfoAdapter browseAppAdapter = new BrowseApplicationInfoAdapter(
				this, mlistAppInfo);
		listview.setAdapter(browseAppAdapter);
		listview.setOnItemClickListener(this);
		confirm = (Button) findViewById(R.id.confirm_button);
		cancel = (Button) findViewById(R.id.cancel_button);
		confirm.setOnClickListener(new confirmListener());
		cancel.setOnClickListener(new concelListener());
	}

	class confirmListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(addAppActivity.this, StartActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("list", list);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish(); // 这句意思是关闭当前Activity，也就是说点返回键返回不到这个Activity了.
		}

	}

	class concelListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		ViewHolder holder = (ViewHolder) arg1.getTag();
		// 改变CheckBox的状态
		holder.cb.toggle();
		// 将CheckBox的选中状况记录下来
		BrowseApplicationInfoAdapter.getIsSelected().put(arg2,
				holder.cb.isChecked());
		// 调整选定条目
		if (holder.cb.isChecked() == true) {
			list.add(mlistAppInfo.get(arg2));
			// checkNum++;
		} else {
			// checkNum--;
			list.remove(mlistAppInfo.get(arg2));
		}
		// 用TextView显示

	}

	PackageManager pm;

	// 获得所有启动Activity的信息，类似于Launch界面
	public void queryAppInfo() {
//		PackageManager pm = this.getPackageManager(); // 获得PackageManager对象
//		// 查询所有已经安装的应用程序
//		List<ApplicationInfo> listAppcations = pm
//				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
//		Collections.sort(listAppcations,
//				new ApplicationInfo.DisplayNameComparator(pm));// 排序
//		
//		// 根据条件来过滤
//		for (ApplicationInfo app : listAppcations) {
//			appInfos.clear();
//			appInfos.add(getAppInfo(app));
//		}
//
//	}

	 PackageManager pm = this.getPackageManager(); // 获得PackageManager对象
	 Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
	 mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	 // 通过查询，获得所有ResolveInfo对象.
	 List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
			 PackageManager.GET_UNINSTALLED_PACKAGES);
	 // 调用系统排序 ， 根据name排序
	 // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
	 Collections.sort(resolveInfos,
	 new ResolveInfo.DisplayNameComparator(pm));
	 if (mlistAppInfo != null) {
	 mlistAppInfo.clear();
	 for (ResolveInfo reInfo : resolveInfos) {
	 String activityName = reInfo.activityInfo.name; //
	 String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
	 String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
	 BitmapDrawable icon = (BitmapDrawable) reInfo.loadIcon(pm); // 获得应用程序图标
	 // 为应用程序的启动Activity 准备Intent
	 Intent launchIntent = new Intent();
	 launchIntent.setComponent(new ComponentName(pkgName,
	 activityName));
	 // 创建一个AppInfo对象，并赋值
	 AppInfo appInfo = new AppInfo();
	 appInfo.setAppLabel(appLabel);
	 appInfo.setPkgName(pkgName);
	 appInfo.setAppIcon(icon);
	 // appInfo.setIntent(launchIntent);
	 mlistAppInfo.add(appInfo); // 添加至列表中
	 }
	 }
	 }
	// 构造一个AppInfo对象 ，并赋值
//	private AppInfo getAppInfo(ApplicationInfo app) {
//		AppInfo appInfo = new AppInfo();
//		appInfo.setAppLabel((String) app.loadLabel(pm));
//		BitmapDrawable icon = (BitmapDrawable) app.loadIcon(pm);
//		appInfo.setAppIcon(icon);
//		appInfo.setPkgName(app.packageName);
//		return appInfo;
//	}
}