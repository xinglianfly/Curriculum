package cn.edu.sdu.online.fragments_first;

import com.actionbarsherlock.view.Menu;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.Listener.ActionBarListener;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.fragment_second.EditInformation;
import cn.edu.sdu.online.utils.DefineUtil;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AlterPersonInfo extends Fragment implements ActionBarListener {
	View view;
	MainActivity activity;
	TextView infoname, infoid, infoacademy, infosigneveryday, infosignorder,
			infosigncontinue, infosignall, infoaim, infotalktome;
	ImageView infosex, infofaceimage;
	Main app;
	Button logOut;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
		app = Main.getApp();
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.frag_alterpersoninfo, null);
		initeView();
		setInfomation();
		activity.setActionBarListener(this);
		activity.invalidateOptionsMenu();
		return view;
	}

	private void initeView() {
		infosex = (ImageView) view.findViewById(R.id.info_sex);
		infofaceimage = (ImageView) view.findViewById(R.id.info_faceimage);
		infoname = (TextView) view.findViewById(R.id.info_name);
		infoid = (TextView) view.findViewById(R.id.info_stuid);
		infoacademy = (TextView) view.findViewById(R.id.info_academy);
		infosigneveryday = (TextView) view
				.findViewById(R.id.info_signevetydayup);
		infosignorder = (TextView) view.findViewById(R.id.info_signorderup);
		infosigncontinue = (TextView) view
				.findViewById(R.id.info_signcontinueup);
		infosignall = (TextView) view.findViewById(R.id.info_signallup);
		infoaim = (TextView) view.findViewById(R.id.info_aim);
		infotalktome = (TextView) view.findViewById(R.id.info_talktome);
		logOut = (Button) view.findViewById(R.id.info_logout);
		logOut.setOnClickListener(new LogoutListener());
	}

	public void setInfomation() {
		String info_name = app.getDataStore().getString("Info_name",
				app.getDataStore().getString("stuname", "未知"));
		String info_id = app.getDataStore().getString("stuid", "未知");
		String info_academy = app.getDataStore().getString("academy", "未知");
		String info_signeveryday = app.getDataStore().getString("signif", "0");
		String info_signorder = String.valueOf(app.getDataStore().getInt(
				"signrank", 0));
		String info_signconti = String.valueOf(app.getDataStore().getInt(
				"signcontinue", 0));
		String info_signall = String.valueOf(app.getDataStore().getInt(
				"signall", 0));
		String info_aim = app.getDataStore().getString("Info_aim",
				"近期目标，砥砺自己前行");
		String info_talktome = app.getDataStore().getString("Info_talktome",
				"总有些话是想对自己说的吧");
		infoname.setText(info_name);
		infoid.setText(info_id);
		infoacademy.setText(info_academy);
		infosigneveryday.setText(info_signeveryday);
		infosignorder.setText(info_signorder);
		infosigncontinue.setText(info_signconti);
		infosignall.setText(info_signall);
		infoaim.setText(info_aim);
		infotalktome.setText(info_talktome);
		if (app.getDataStore().getString("Info_sex", "未知").equals("女")) {
			infosex.setBackgroundResource(R.drawable.edit_female);
		}
		if (app.getDataStore().getString("Info_sex", "未知").equals("男")) {
			infosex.setBackgroundResource(R.drawable.man_sex);
		}
		if (app.getDataStore().getBoolean("FACEIMAGE", false)) {
			Bitmap bitmap = app.getLoacalBitmap(DefineUtil.SDPATH + "/"
					+ "faceimage.png");
			infofaceimage.setImageBitmap(bitmap);
		} else {
			infofaceimage.setBackgroundResource(R.drawable.faceimage);
		}
	}

	@Override
	public void onPrepare(Menu menu) {
		
		MenuItem edit = menu.add("编辑");
		edit.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		edit.setIcon(R.drawable.editactionbar);
		edit.setOnMenuItemClickListener(new editinfoListener());
	}

	class LogoutListener implements OnClickListener {

		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			activity.changeToLogout();
			activity.switchFragment(
					Main.menuItems.get(Main.INT_IMPORT).fragment,
					R.anim.left_push_in, R.anim.left_push_out, "课程导入");
			activity.setActionBarListener(null);
			activity.invalidateOptionsMenu();
		}

	}

	class editinfoListener implements OnMenuItemClickListener {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			activity.setAnimIn(R.anim.left_push_in);
			activity.setAnimOut(R.anim.left_push_out);
			EditInformation editinfo = new EditInformation();
			activity.switchFragment(editinfo);
			activity.setAnimIn(0);
			activity.setAnimOut(0);
			return true;
		}
	}

}
