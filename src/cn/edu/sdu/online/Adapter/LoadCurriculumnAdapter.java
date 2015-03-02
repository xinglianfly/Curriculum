package cn.edu.sdu.online.Adapter;

import cn.edu.sdu.online.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadCurriculumnAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private String[] classNameList;
	private Drawable[] colors;
	private int[] intColors;
	private int gridheight;
	private int gridwidth;

	public LoadCurriculumnAdapter(Context context, String[] classNameList,
			Drawable[] colors, int[] intColors, int gridheight, int gridwidth) {

		this.inflater = LayoutInflater.from(context);
		this.classNameList = classNameList;
		this.colors = colors;
		this.intColors = intColors;
		this.gridheight = gridheight;
		this.gridwidth = gridwidth;
	}

	@Override
	public int getCount() {
		return 35;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout view = (RelativeLayout) inflater.inflate(
				R.layout.grid_item, null);
		TextView aView = (TextView) view.findViewById(R.id.course_name);
		if (classNameList[position] != null) {
			aView.setText(classNameList[position]);

			// TODO 这部分可以提取到一个专门处理API差异的类中
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
				view.setBackgroundDrawable(colors[intColors[position]]);
			else {
				view.setBackground(colors[intColors[position]]);
			}

		} else {
			aView.setText(" ");
		}
		view.setLayoutParams(new GridView.LayoutParams(gridwidth, gridheight));
		aView.setGravity(Gravity.CENTER);
		return view;
	}
}
