package cn.edu.sdu.online.fragments_first;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class Room extends Fragment {

	MainActivity activity;
	private WebView webView;
	private LinearLayout layout;
	private Main app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
		app = Main.getApp();

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setAllow(WebSettings settings) {
		settings.setAllowUniversalAccessFromFileURLs(true);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_room, null);
		layout = (LinearLayout) view.findViewById(R.id.room);

		webView = new WebView(activity);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
			setAllow(webSettings);
		webView.loadUrl("file:///android_asset/shoujizixishi/index2.html");
		layout.addView(webView);
		NetworkInfo info = app.getConnectivityManager().getActiveNetworkInfo();

		
		if (info == null || !info.isAvailable()) {
			new AlertDialog.Builder(activity).setTitle("没有网络的情况下无法查询，抱歉！").setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					activity.toggle();
				}
			}).show();
			
		} else {

		}

		return view;
	}

	@Override
	public void onDestroyView() {
		layout.removeView(webView);
		super.onDestroyView();

	}

}
