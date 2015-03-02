package cn.edu.sdu.online.modal;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

public class LeftMenuItem {
	public String title;
	public Fragment fragment;
	public Bitmap icon;
	
	public LeftMenuItem(String title,Fragment fragment,Bitmap icon){
		this.title=title;
		this.fragment=fragment;
		this.icon = icon;
	}
	public LeftMenuItem(String title,Bitmap icon){
		this.title=title;
		this.icon = icon;
	}
}


