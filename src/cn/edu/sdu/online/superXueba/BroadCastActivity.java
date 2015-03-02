package cn.edu.sdu.online.superXueba;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.app.MainActivity;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BroadCastActivity extends Activity {
	MainActivity activity;
	Vibrator vib;
	Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stopservice);
		back = (Button) findViewById(R.id.back_study_success);
		back.setOnClickListener(new back());
		Intent intent = new Intent(BroadCastActivity.this, XuebaService.class);
		stopService(intent);// ¹Ø±Õservice
		InterceptActivity.instance.finish();
		vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		;
		vib.vibrate(2000);
	}

	class back implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}

	}
}
