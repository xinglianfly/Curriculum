package cn.edu.sdu.online.fragments_first;

import java.util.TreeMap;

import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.modal.LeftMenuItem;
import cn.edu.sdu.online.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 左边侧栏的布局
 * 
 * @author
 * 
 */

public class Menu extends Fragment implements OnSharedPreferenceChangeListener {

	private LayoutInflater inflater;
	private View view;
	int BarHeight;
	ImageView imageview = null;

	

	MainActivity activity;
	Main app;
	private TextView nameText;
	private TextView idText;
	private SharedPreferences pre;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
		app = Main.getApp();
		pre = app.getDataStore();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		
		this.inflater = inflater;
		
	
//		menu_items =  activity.getMenuItems().values().toArray(new LeftMenuItem[0]);
		
		
		view = inflater.inflate(R.layout.frag_menu, null);
		ListView menuList = (ListView) view.findViewById(R.id.menu_list);
		
		menuList.setAdapter(activity.getMenuAdapter());
		
		menuList.setOnItemClickListener(activity.getMenuHandler());
		idText = (TextView) view.findViewById(R.id.studentid);
		nameText = (TextView) view.findViewById(R.id.name);
		
		idText.setText(pre.getString("stuid", "享有更多精彩"));
		nameText.setText(pre.getString("stuname", "登陆后"));
		return view;
	}

	
	
	public void updateInfo(String name, String id) {
		idText.setText(id);
		nameText.setText(name);
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if(idText!=null&&nameText!=null){
		idText.setText(pre.getString("stuid", "享有更多精彩"));
		nameText.setText(pre.getString("stuname", "登陆后"));
		}
		
	}


}
