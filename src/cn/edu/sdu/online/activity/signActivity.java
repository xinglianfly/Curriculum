package cn.edu.sdu.online.activity;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.utils.DateUtil;
import cn.edu.sdu.online.utils.DefineUtil;
import cn.edu.sdu.online.utils.SignUtils;
import cn.edu.sdu.online.utils.SignUtils.DataListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class signActivity extends Activity implements DataListener {
	View view;
	EditText releasetask;
	Button releaseButton;
	Main app;
	SignUtils signutils;
	DateUtil date;
	int signcontinu;
	int signall;
	int signrank;
	String signif;
	boolean ifGetRank;
	String rankserver;
	ImageView image;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app = Main.getApp();
		signutils = new SignUtils();
		date = new DateUtil();
		setContentView(R.layout.activity_sign);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		releasetask = (EditText) findViewById(R.id.releaserank);
		releaseButton = (Button) findViewById(R.id.releasebutton);
		releaseButton.setOnClickListener(new releaseButton());
		image = (ImageView) findViewById(R.id.back_sign_release);
		image.setOnClickListener(new backListener());
		getsignData();
	}

	class backListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}

	}

	class releaseButton implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (releasetask.getText().toString().equals("")) {
				Toast.makeText(signActivity.this, "请输入内容", Toast.LENGTH_SHORT)
						.show();
			} else {
				getsignRank(app.getDataStore().getString("stuid", ""), app
						.getDataStore().getString("stuname", "未知"),
						signActivity.this, releasetask.getText().toString());

				finish();
			}
		}

	}

	private void getsignData() {
		// TODO Auto-generated method stub
		signcontinu = app.getDataStore().getInt("signcontinue", 0);
		signall = app.getDataStore().getInt("signall", 0);
		signrank = app.getDataStore().getInt("signrank", 0);
		signif = app.getDataStore().getString("signif", "0");
	}

	private void storeData() {
		// TODO Auto-generated method stub

		int rank = Integer.parseInt(rankserver);

		app.getDataStore().edit().putString("signif", "1").commit();
		app.getDataStore().edit().putInt("signrank", rank).commit();
		app.getDataStore().edit().putInt("signday", date.SecondDay()).commit();
		app.getDataStore().edit().putInt("signmonth", date.getMonth()).commit();
		if (signutils.continuSign()) {
			app.getDataStore().edit().putInt("signcontinue", ++signcontinu)
					.commit();
		} else {
			app.getDataStore().edit().putInt("signcontinue", 1).commit();
		}
		app.getDataStore().edit().putInt("signall", ++signall).commit();
	}

	@Override
	public void onFinished() {
		// TODO Auto-generated method stub
		storeData();
	}

	public void getsignRank(String id, String name,
			final DataListener listener, String release) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("Type", "getRank");
		params.addBodyParameter("id", id);
		params.addBodyParameter("name", name);
		params.addBodyParameter("content", release);
		System.out.println("qian dao" + name);
		if (signutils.continuSign()) {
			params.addBodyParameter("ifcontinusign", "是");
		} else {
			params.addBodyParameter("ifcontinusign", "否");
		}
		System.out.println("pai mimg zhi ximg");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, DefineUtil.PATH, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						System.out.println(arg0+"异常");
						ifGetRank = false;
						Toast.makeText(signActivity.this, "签到失败，请检查网络设置",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						rankserver = arg0.result;
						ifGetRank = true;
						listener.onFinished();
						Toast.makeText(signActivity.this, "签到成功，看看自己的排名吧！",
								Toast.LENGTH_SHORT).show();
						finish();
					}
				});
	}

}
