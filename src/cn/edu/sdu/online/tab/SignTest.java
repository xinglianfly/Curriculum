package cn.edu.sdu.online.tab;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.Adapter.LoadRankAdapter;
import cn.edu.sdu.online.Listener.OnRefreshListener;
import cn.edu.sdu.online.activity.PinglunActivity;
import cn.edu.sdu.online.activity.signActivity;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.modal.SignRelease;
import cn.edu.sdu.online.utils.DefineUtil;
import cn.edu.sdu.online.utils.SignUtils;
import cn.edu.sdu.online.view.RefreshListView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

/**
 */
public class SignTest extends Fragment {
	private LoadRankAdapter mAdapter;
	private List<SignRelease> listItem;
	private RefreshListView mRefreshListView;
	View view;
	Main app;
	SignUtils signutils;
	MainActivity activity;
	static String resultserver;
	static String downresultserver;

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
		view = inflater.inflate(R.layout.frag_loadsign, null);
		initView();
		initData();
		return view;
	}

	public void initView() {
		mRefreshListView = (RefreshListView) view
				.findViewById(R.id.refresh_listview);
	}

	private void initData() {
		// listItem = new ArrayList<SignRelease>();
		listItem = app.getRankandComment("RANKANDCOMMENT");
		mAdapter = new LoadRankAdapter(getActivity(), listItem);
		mRefreshListView.setAdapter(mAdapter);
		mRefreshListView.setOnItemClickListener(new detailInfo());
		mRefreshListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// // 异步查询数据
				System.out.println("执行了刷新");
				RequestParams pa = new RequestParams();
				pa.addBodyParameter("Type", "getRankInfo");
				pa.addBodyParameter(
						"flagrankid",
						String.valueOf(app.getDataStore().getInt(
								"flagrankidlatest", 0)));
				HttpUtils http = new HttpUtils();
				http.send(HttpRequest.HttpMethod.POST, DefineUtil.PATH, pa,
						new RequestCallBack<String>() {
							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								// TODO Auto-generated method stub
								System.out.println(arg0.getMessage()+" 异常时");

							}

							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								System.out.println("cheng ong le ma"+arg0.result);
								
								// TODO Auto-generated method stub
								Message msg = new Message();
								resultserver = arg0.result;
								msg.what = 1;
								handler.sendMessage(msg);
							}

						});

			}

			public Handler handler = new Handler() {
				ArrayList<SignRelease> signlist;
				ArrayList<SignRelease> signdownlist;

				public void handleMessage(Message msg) {
					// mAdapter = new MyAdapter(getActivity(), listItem);
					// mRefreshListView.setAdapter(mAdapter);
					Gson gson = new Gson();
					switch (msg.what) {
					case 1:

						signlist = new ArrayList<SignRelease>();
						signlist = gson.fromJson(resultserver,
								new TypeToken<ArrayList<SignRelease>>() {
								}.getType());
						new AsyncTask<Void, Void, Void>() {
							protected Void doInBackground(Void... params) {
								SystemClock.sleep(20000);
								if (signlist.size() != 0) {
									listItem.clear();
									for (int i = 0; i < signlist.size(); i++) {
										listItem.add(0, signlist.get(i));
									}
									app.setRankandComment("RANKANDCOMMENT",
											listItem);
								}

								return null;
							};

							protected void onPostExecute(Void result) {
								super.onPostExecute(result);
								mAdapter.notifyDataSetChanged();
								mRefreshListView.onRefreshFinish();
								if (signlist.size() != 0) {
									System.out.println("zhi xinb if");
									Main.getApp()
											.getDataStore()
											.edit()
											.putInt("flagrankidlatest",
													signlist.get(
															signlist.size() - 1)
															.getRankid())
											.commit();
								}

							};
						}.execute(new Void[] {});
						break;
					case 2:
						signdownlist = new ArrayList<SignRelease>();
						signdownlist = gson.fromJson(downresultserver,
								new TypeToken<ArrayList<SignRelease>>() {
								}.getType());
						new AsyncTask<Void, Void, Void>() {
							@Override
							protected Void doInBackground(Void... params) {
								SystemClock.sleep(3000);
								if (signdownlist.size() != 0) {
									for (int i = 0; i < signdownlist.size(); i++) {
										listItem.add(signdownlist.get(i));
									}
								}
								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								super.onPostExecute(result);
								mAdapter.notifyDataSetChanged();
								mRefreshListView.onRefreshFinish();
							}

						}.execute(new Void[] {});
						break;
					}

				}
			};

			/**
			 * 上拉加载更多
			 */
			@Override
			public void onLoadMoring() {
				System.out.println("执行了上拉刷新");
				RequestParams pa = new RequestParams();
				pa.addBodyParameter("Type", "getRankdownInfo");
				pa.addBodyParameter(
						"flagdownrankid",
						String.valueOf(app.getDataStore().getInt(
								"flagrankidoldest", 0)));
				HttpUtils http = new HttpUtils();
				http.send(HttpRequest.HttpMethod.POST, DefineUtil.PATH, pa,
						new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								// TODO Auto-generated method stub
								Message msg = new Message();
								downresultserver = arg0.result;
								msg.what = 2;
								handler.sendMessage(msg);
							}
						});

			}
		});

	}

	
	/**
	 * 跳转到状态详细信息的界面
	 * 
	 * @author lin
	 * 
	 */
	class detailInfo implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			System.out.println(position + "positions hsi");
			SignRelease sir = listItem.get(position - 1);
			Intent intent = new Intent(activity, PinglunActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("sign", sir);
			intent.setClass(activity, PinglunActivity.class);
			intent.putExtras(bundle);
			activity.startActivityForResult(intent, 0);
		}

	}
}
