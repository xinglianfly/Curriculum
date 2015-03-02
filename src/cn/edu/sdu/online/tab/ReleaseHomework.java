package cn.edu.sdu.online.tab;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.Adapter.LoadHomeworkAdapter;
import cn.edu.sdu.online.Listener.OnRefreshListener;
import cn.edu.sdu.online.activity.PinglunActivity;
import cn.edu.sdu.online.activity.PublishHomework;
import cn.edu.sdu.online.activity.WriteCommentActivity;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.modal.MyHomework;
import cn.edu.sdu.online.modal.SignRelease;
import cn.edu.sdu.online.utils.DefineUtil;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ReleaseHomework extends Fragment {
	Main app;
	MainActivity activity;
	Button releaseHomework;
	static RefreshListView mrefrelistview;
	static LoadHomeworkAdapter madapter;
	View view;
	ArrayList<MyHomework> homelist;
	static String homeworkserver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app = Main.getApp();
		activity = (MainActivity) getActivity();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.frag_loadhomework, null);
		initView();
		initData();
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		mrefrelistview = (RefreshListView) view.findViewById(R.id.homeworklist);
	}

	private void initData() {
		// TODO Auto-generated method stub
		// list = new ArrayList<MyHomework>();
		homelist = app.getHomework("HOMEWORK");
		madapter = new LoadHomeworkAdapter(getActivity(), homelist);
		mrefrelistview.setAdapter(madapter);
		mrefrelistview.setOnItemLongClickListener(new listLongListener());
		mrefrelistview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				RequestParams params = new RequestParams();
				params.addBodyParameter("Type", "loadhomework");
				params.addBodyParameter("id",
						app.getDataStore().getString("username", ""));
				HttpUtils http = new HttpUtils();
				http.send(HttpRequest.HttpMethod.POST, DefineUtil.PATH, params,
						new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {

							}

							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								homeworkserver = arg0.result;
								System.out.println(homeworkserver);
								Message msg = new Message();
								msg.what = 1;
								handler.sendMessage(msg);
							}
						});
			}

			@Override
			public void onLoadMoring() {
				// TODO Auto-generated method stub
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						SystemClock.sleep(0);
						return null;
					};

					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						madapter.notifyDataSetChanged();
						mrefrelistview.onRefreshFinish();

					};
				}.execute(new Void[] {});
			}
		});
	}

	ArrayList<MyHomework> listserver;
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			Gson gson = new Gson();
			switch (msg.what) {
			case 1:

				listserver = new ArrayList<MyHomework>();
				listserver = gson.fromJson(homeworkserver,
						new TypeToken<ArrayList<MyHomework>>() {
						}.getType());
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						SystemClock.sleep(3000);
						if (listserver.size() != 0) {
							homelist.clear();
							for (int i = 0; i < listserver.size(); i++) {
								homelist.add(listserver.get(i));
							}
						}

						app.setHomework("HOMEWORK", homelist);

						return null;
					};

					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						System.out.println("zhi xingle onpostEXECUTE");
						madapter.notifyDataSetChanged();
						mrefrelistview.onRefreshFinish();

					};
				}.execute(new Void[] {});
				break;
			}
		};
	};

	class listLongListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			uploadDelete(homelist.get(arg2 - 1).getHomeworkid(),arg2);
			

			app.setHomework("HOMEWORK", homelist);
			return true;
		}

	}

	public void uploadDelete(int homeworkid,final int arg2) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("Type", "deletehomework");
		params.addBodyParameter("stuid",
				app.getDataStore().getString("username", ""));
		params.addBodyParameter("homeworkid", String.valueOf(homeworkid));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, DefineUtil.PATH, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "网络或其他异常",
								Toast.LENGTH_SHORT).show();

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "删除成功",
								Toast.LENGTH_SHORT).show();
						homelist.remove(arg2 - 1);
						madapter.notifyDataSetChanged();
					}
				});
	}
}
