package cn.edu.sdu.online.fragments_first;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.Listener.ActionBarListener;
import cn.edu.sdu.online.Listener.ChangeToListener;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.fragment_second.LockSetup;
import cn.edu.sdu.online.fragment_second.SelectedSemesterScore;
import cn.edu.sdu.online.fragments_first.GradePoint.setPassword;
import cn.edu.sdu.online.http_deal.InformationHttp;
import cn.edu.sdu.online.utils.DefineUtil;
import android.animation.AnimatorSet.Builder;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 显示成绩的相关布局
 * 
 * @author seal
 * 
 */
public class Score extends Fragment implements ChangeToListener,
		ActionBarListener {

	MainActivity activity;
	Main app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
		app = Main.getApp();
		app.getDataStore().edit().putString("whichfrag", "成绩").commit();

	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_score, null);
		activity.setChangeToListener(this);
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

	@Override
	public void changTo(View view) {

		// 设置动画为===========
		activity.setAnimIn(R.anim.left_push_in);
		activity.setAnimOut(R.anim.left_push_out);

		SelectedSemesterScore fragment = null;
		switch (view.getId()) {

		case R.id.se_1:

			app.getCompulsoryList(1);
			fragment = new SelectedSemesterScore(1);
			break;
		case R.id.se_2:

			app.getCompulsoryList(2);
			fragment = new SelectedSemesterScore(2);
			break;
		case R.id.se_3:
			app.getCompulsoryList(3);
			fragment = new SelectedSemesterScore(3);
			break;
		case R.id.se_4:
			app.getCompulsoryList(4);
			fragment = new SelectedSemesterScore(4);
			break;
		case R.id.se_5:
			app.getCompulsoryList(5);
			fragment = new SelectedSemesterScore(5);
			break;
		case R.id.se_6:
			app.getCompulsoryList(6);
			fragment = new SelectedSemesterScore(6);
			break;
		case R.id.se_7:
			app.getCompulsoryList(7);
			fragment = new SelectedSemesterScore(7);
			break;
		case R.id.se_8:
			app.getCompulsoryList(8);
			fragment = new SelectedSemesterScore(8);
			break;
		default:
			break;
		}
		activity.switchFragment(fragment);

		// 还原动画为无
		activity.setAnimIn(0);
		activity.setAnimOut(0);

	}

	MenuItem itemalter;
	MenuItem itemset;

	@Override
	public void onPrepare(Menu menu) {
		// TODO Auto-generated method stub
		System.out.println("cheng ji zhi xinng le menu");
		boolean iflock = app.getDataStore().getBoolean("iflock", false);
		if (iflock) {
			itemalter = menu.add("取消密码");
			itemalter.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			itemalter.setOnMenuItemClickListener(new cancelpassword());
		} else {
			itemset = menu.add("设置密码");
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
			tellSercan(app.getDataStore().getString("stuid", ""));
			return true;
		}

	}

	public void tellSercan(String id) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("Type", "cancelpassword");
		params.addBodyParameter("id", id);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, DefineUtil.PATH, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "取消密码失败，请检查网络连接", 2000)
								.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "取消密码成功", 2000).show();
					}
				});

	}
}
