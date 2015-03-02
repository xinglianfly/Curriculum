package cn.edu.sdu.online.fragments_first;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.http_deal.InformationHttp;
import cn.edu.sdu.online.service.DownLoadService;
import cn.edu.sdu.online.utils.DefineUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 导入课程界面 （登录）
 * 
 * @author
 * 
 */
public class CourseImport extends Fragment {

	private MainActivity activity;
	private Main app;
	private WaittingMessage message;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = Main.getApp();
		activity = (MainActivity) getActivity();
		message = new WaittingMessage();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_login, null);
		button_login = (Button) view.findViewById(R.id.login);
		user_name = (EditText) view.findViewById(R.id.LoginID);
		password = (EditText) view.findViewById(R.id.PassWord);
		button_login.setOnClickListener(loginTest);
		user_name.setText(app.getDataStore().getString("username", ""));
		password.setText(app.getDataStore().getString("password", ""));

		return view;
	}

	private Button.OnClickListener loginTest = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			String userName = user_name.getText().toString();
			String passWord = password.getText().toString();
			// 判断用户是否输入的用户名或密码
			if (TextUtils.isEmpty(userName)) {
				if (TextUtils.isEmpty(passWord)) {
					Toast.makeText(getActivity(), "请输入用户名和密码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(activity, "请输入用户名！", Toast.LENGTH_SHORT).show();
				return;
			} else if (TextUtils.isEmpty(passWord)) {
				Toast.makeText(activity, "请输入密码", Toast.LENGTH_SHORT).show();
				return;
			}
			System.out.println("获取的用户名：" + userName);
			System.out.println("获取的密码为：" + passWord);
			// 弹出等待圆圈
			message.show(activity.getSupportFragmentManager(), "waitting");
			if (passWord.length() > 10)
				passWord = passWord.substring(0, 10);

			testPost(userName, passWord);
		}

	};

	public synchronized void testPost(final String name, final String password) {

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("Type", "getInformation");
		params.addQueryStringParameter("username", name);
		params.addQueryStringParameter("password", password);
		HttpUtils http = new HttpUtils();
		// 请求超时设置
		http.configTimeout(20000);
		
		http.send(HttpRequest.HttpMethod.POST, DefineUtil.PATH, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						System.out.println("conn.....");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

						System.out.println("current......");
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Message msg = new Message();
						System.out.println("upload response"
								+ responseInfo.result);
						char codechar = responseInfo.result.charAt(0);
						String code = String.valueOf(codechar);
						if (code.equals("0")) {
							InformationHttp
									.changeInputStream(responseInfo.result);
							System.out.println("equal o ");
							msg.what = 0;
							handler.sendMessage(msg);
							// 保存账号密码！
							app.getDataStore().edit()
									.putString("username", name)
									.putString("password", password).commit();
						}

						else if (code.equals("1")) {
							msg.what = 1;
							handler.sendMessage(msg);
						} else {
							msg.what = 2;
							handler.sendMessage(msg);
						}
						message.dismiss();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(app, "网络或其他异常!", Toast.LENGTH_SHORT)
								.show();
						message.dismiss();
					}
				});
	}

	// 定义Handler对象
	private Handler handler = new Handler() {
		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				Toast.makeText(app, "登录成功", Toast.LENGTH_SHORT).show();

				user_name.setText("");
				password.setText("");
				activity.getMenu().updateInfo(app.getPerson().getMyStudentID(),
						app.getPerson().getMyName());
				// 跳转到课程表页面
				activity.switchFragment(
						Main.menuItems.get(Main.INT_CURRICULUM).fragment,
						R.anim.left_push_in, R.anim.left_push_out, "我的课表");

				activity.changeToLogin();
				startService();
				break;
			case 1:
				Toast.makeText(app, "用户名或 密码错误!", Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(app, "网络或其他异常!", Toast.LENGTH_SHORT).show();

			}
			message.dismiss();
			super.handleMessage(msg);
		}
	};

	private Button button_login;
	private EditText user_name;
	private EditText password;
	LinearLayout layout;

	public static class WaittingMessage extends DialogFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setStyle(STYLE_NO_FRAME, 0);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.dialog_waitting, container,
					false);
			return view;
		}
	}

	public void startService() {
		Intent intent = new Intent(getActivity(), DownLoadService.class);
		getActivity().startService(intent);
	}

}
