package cn.edu.sdu.online.service;

import java.io.File;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;

public class DownLoadService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		System.out.println("zhi xign le service");
		downLoad();
		return super.onStartCommand(intent, flags, startId);
	}
	
	public void downLoad(){
		final SharedPreferences settings = getSharedPreferences("curriculum",
				MODE_PRIVATE);
		String path = settings.getString("faceimagepath", "");
		String downloadPath = "http://192.168.1.136:8080/CurriculumServer/upload/"+path;
		if(!path.equals("")){
			String SDPATH = Environment.getExternalStorageDirectory()+"/"+"faceimage.png";
			System.out.println(Environment.getExternalStorageDirectory()+"/////////////////");
			HttpUtils http = new HttpUtils();
			HttpHandler<File> handler = http.download(downloadPath,
					SDPATH, false, // 如果目标文件存在，接着未完成的部分继续下载。
					true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
					new RequestCallBack<File>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub
							System.out.println("xiazai shibai"+arg0.getMessage()+"++++"+arg1);
						}

						@Override
						public void onSuccess(ResponseInfo<File> arg0) {
							// TODO Auto-generated method stub
							System.out.println("xiazai cheng gong");
							settings.edit().putBoolean("FACEIMAGE", true).commit();
						}

					});

		}
		}
		
}
