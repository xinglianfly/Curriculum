package cn.edu.sdu.online.activity;

import cn.edu.sdu.online.modal.MyHomework;
import cn.edu.sdu.online.modal.SignRelease;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class HomeworkDetails extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	private void getData() {
		// TODO Auto-generated method stub
		MyHomework home = (MyHomework) getIntent().getSerializableExtra(
				"homework");
	}
	private void initView() {
		// TODO Auto-generated method stub

	}
}
