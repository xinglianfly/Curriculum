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
		tv.setText("��ǰ�汾��:"+version+" \n\u0020\n�˰汾������android2.2�����Ϲ̼�������������أ���װ��ȫ��ѣ�ʹ�ù����в�������������������Ӫ����ȡ�����������ֻ�ϵͳ��ʹ�ñ���Ʒ�������κ����⡣���ǽ����е��κ�����");
//		re=(TextView)view.findViewById(R.id.aboutcontent9);
		return view;
	}
	
	   private String getVersionName() 
	   {
	           // ��ȡpackagemanager��ʵ��
	           PackageManager packageManager = mContext.getPackageManager();
	           // getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
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

