package cn.edu.sdu.online.fragments_first;

import com.umeng.fb.FeedbackAgent;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.edu.sdu.online.R;

public class About extends Fragment {
	Context mContext;
//	TextView re;
	FeedbackAgent agent;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_about,
				container, false);
		agent = new FeedbackAgent(this.getActivity());
		agent.sync();
		view.findViewById(R.id.aboutcontent9)
		.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				agent.startFeedbackActivity();
			}
		});
		TextView tv=(TextView)view.findViewById(R.id.aboutcontent1);
		String version=getVersionName();
		tv.setText("当前版本号:"+version+" \n\u0020\n此版本适用于android2.2及以上固件，本软件的下载，安装完全免费，使用过程中产生的数据流量，由运营商收取。如在其他手机系统上使用本产品产生的任何问题。我们将不承担任何责任");
//		re=(TextView)view.findViewById(R.id.aboutcontent9);
		return view;
	}
	
	   private String getVersionName() 
	   {
	           // 获取packagemanager的实例
	           PackageManager packageManager = mContext.getPackageManager();
	           // getPackageName()是你当前类的包名，0代表是获取版本信息
	           PackageInfo packInfo;
			try {
				packInfo = packageManager.getPackageInfo(mContext.getPackageName(),0);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				return "1.1.0";
				//e.printStackTrace();
			}
	           String version = packInfo.versionName;
	           return version;
	   }
	
	
	
}

