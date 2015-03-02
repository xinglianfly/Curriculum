package cn.edu.sdu.online.service;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.app.MainActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.support.v4.app.TaskStackBuilder;

public class NotificationService extends Service {
	int hour;
	int minute;
	int second;
	int numClasses;
	NotificationManager mNotificationManager;
	NotificationCompat.Builder mNotifyBuilder;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		showNotification();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void showNotification() {
		int classnum = getnumClasses();
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// Sets an ID for the notification, so it can be updated
		int notifyID = 1;
		mNotifyBuilder = new NotificationCompat.Builder(this)
				.setContentTitle("通知")
				.setContentText("您明天有" + classnum + "节课要上")
				.setSmallIcon(R.drawable.ic_launcher);
		Intent intent = new Intent(NotificationService.this, MainActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder
				.create(NotificationService.this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mNotifyBuilder.setContentIntent(resultPendingIntent);
		mNotificationManager.notify(notifyID, mNotifyBuilder.build());
	}

	public int getnumClasses() {
		SharedPreferences settings = getSharedPreferences("curriculum",
				MODE_PRIVATE);

		Time time = new Time();
		time.setToNow(); // 取得系统时间。
		int weekDay = time.weekDay;
		System.out.println("weekday为" + weekDay);
		switch (weekDay) {
		case 0:
			numClasses = settings.getInt("Monday", 0);
			break;
		case 1:
			numClasses = settings.getInt("Tuesday", 0);
			break;
		case 2:
			numClasses = settings.getInt("Wednesday", 0);
			break;
		case 3:
			numClasses = settings.getInt("Thursday", 0);
			break;
		case 4:
			numClasses = settings.getInt("Friday", 0);
			break;
		case 5:
			numClasses = settings.getInt("Saturday", 0);
			break;
		case 6:
			numClasses = settings.getInt("Sunday", 0);
			break;
		}
		return numClasses;
	}
}
