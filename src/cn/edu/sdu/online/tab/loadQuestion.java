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
import cn.edu.sdu.online.Adapter.LoadQuestionAdapter;
import cn.edu.sdu.online.Listener.OnRefreshListener;
import cn.edu.sdu.online.activity.AnswerQuestion;
import cn.edu.sdu.online.activity.QuestionDetails;
import cn.edu.sdu.online.activity.ReleaseQuestion;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.modal.Question;
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
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.AdapterView.OnItemClickListener;

/**
 */
public class loadQuestion extends Fragment {
	private LoadQuestionAdapter mAdapter;
	private List<Question> listItem;
	private RefreshListView mRefreshListView;
	View view;
	Main app;
	MainActivity activity;
	static String resultserver;
	static String upresultserver;
	PopupMenu popup = null;
	Button releaseHomework_button;
	ArrayList<Question> queslist;

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
		view = inflater.inflate(R.layout.frag_loadquestion, null);
		initView();
		initData();
		return view;
	}

	public void initView() {
		mRefreshListView = (RefreshListView) view
				.findViewById(R.id.questionlist);
	}

	private void initData() {
		listItem = app.getAskandAnswer("ASKANDANSWER");
		mAdapter = new LoadQuestionAdapter(getActivity(), listItem);
		mRefreshListView.setAdapter(mAdapter);
		mRefreshListView.setOnItemClickListener(new listClick());
		mRefreshListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				RequestParams params = new RequestParams();
				params.addBodyParameter("Type", "getaskdownrefresh");
				HttpUtils http = new HttpUtils();
				http.send(HttpRequest.HttpMethod.POST, DefineUtil.PATH, params,
						new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								// TODO Auto-generated method stub
								resultserver = arg0.result;
								Message msg = new Message();
								msg.what = 1;
								handler.sendMessage(msg);

							}
						});

			}

			@Override
			public void onLoadMoring() {
				// TODO Auto-generated method stub
				System.out.println("执行了上拉刷新");
				RequestParams pa = new RequestParams();
				pa.addBodyParameter("Type", "getanswerupload");
				pa.addBodyParameter(
						"flagupquesid",
						String.valueOf(app.getDataStore().getInt(
								"flagaskoldest", 0)));
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
								upresultserver = arg0.result;
								msg.what = 2;
								handler.sendMessage(msg);
							}
						});

			}

		});
	}

	class listClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			Question ques = listItem.get(position - 1);
			Intent intent = new Intent(activity, AnswerQuestion.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("question", ques);
			intent.setClass(activity, QuestionDetails.class);
			intent.putExtras(bundle);
			activity.startActivity(intent);
		}

	}

	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Gson gson = new Gson();

			switch (msg.what) {
			case 1:
				queslist = new ArrayList<Question>();
				queslist = gson.fromJson(resultserver,
						new TypeToken<ArrayList<Question>>() {
						}.getType());
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						SystemClock.sleep(3000);
						if (queslist.size() != 0) {
							listItem.clear();
							for (int i = 0; i < queslist.size(); i++) {
								listItem.add(0, queslist.get(i));
							}
							app.setAskAndAnswer("ASKANDANSWER", listItem);
						}
						return null;
					}

					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						mAdapter.notifyDataSetChanged();
						mRefreshListView.onRefreshFinish();
					}
				}.execute(new Void[] {});
				break;
			case 2:
				queslist = new ArrayList<Question>();
				queslist = gson.fromJson(upresultserver,
						new TypeToken<ArrayList<Question>>() {
						}.getType());
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						SystemClock.sleep(3000);
						if (queslist.size() != 0) {
							for (int i = 0; i < queslist.size(); i++) {
								listItem.add(queslist.get(i));
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

			}
		}
	};
}
