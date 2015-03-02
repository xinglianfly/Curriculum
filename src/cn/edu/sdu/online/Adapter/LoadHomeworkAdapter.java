package cn.edu.sdu.online.Adapter;

import java.util.ArrayList;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.modal.MyHomework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LoadHomeworkAdapter extends BaseAdapter {

	LayoutInflater inflater;
	ArrayList<MyHomework> list;

	public LoadHomeworkAdapter(Context context, ArrayList<MyHomework> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = inflater.inflate(R.layout.listitem_releasehomework, null);
		TextView subject = (TextView) convertView
				.findViewById(R.id.subject_textview); // 用户头像
		TextView creatdate = (TextView) convertView
				.findViewById(R.id.creatdate);// 时间
		TextView content = (TextView) convertView
				.findViewById(R.id.homework_content_textview);// 心情内容
		MyHomework homework = list.get(position);
		subject.setText(homework.getSubject());
		content.setText(homework.getHomework());
		creatdate.setText(homework.getDeadline());
		return convertView;
	}

}
