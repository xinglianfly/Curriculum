package cn.edu.sdu.online.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class updatesignService extends Service{

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		updatefile();
		return super.onStartCommand(intent, flags, startId);
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updatefile(){
		System.out.println("kaishizhixing update");
		SharedPreferences settings = getSharedPreferences("curriculum",
				MODE_PRIVATE);
		settings.edit().putBoolean("ifsign", false).commit();
	}
	
}
