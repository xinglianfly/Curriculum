package cn.edu.sdu.online.Adapter;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.modal.Course;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

public class LoadScoreAdapter extends BaseAdapter{
	private int compulsoryPos;
	private Course course;
	LayoutInflater inflater;
	ViewHolder holder;
	int semester;
	public LoadScoreAdapter(int semester,Context context) {
		if (Main.getApp().getCompulsoryList(semester) != null) {
	  		compulsoryPos = Main.getApp().getCompulsoryList(semester).size();
		}
		inflater = LayoutInflater.from(context);
		this.semester= semester;
	}

	@Override
	public int getCount() {
		return compulsoryPos;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TableRow row;

		if (convertView == null) {
			row = (TableRow) inflater.inflate(R.layout.list_item_row, null);
			holder = new ViewHolder();
			holder.rowCourse = (TextView) row.findViewById(R.id.row_course);
			holder.rowCredit = (TextView) row.findViewById(R.id.row_credit);
			holder.rowScore = (TextView) row.findViewById(R.id.row_score);
			holder.rowAttr = (TextView) row.findViewById(R.id.row_attr);
			row.setTag(holder);

		} else {
			row = (TableRow) convertView;
			holder = (ViewHolder) row.getTag();
		}

		course = Main.getApp().getCompulsoryList(semester).get(position);
		rowMaker(course.getClassName(),  course.getClassCredit(),
				course.getClassGrade(), course.getClassAttitude());
		return row;
	}

	private void rowMaker(String course, String credit, String score,
			String attr) {
		holder.rowCourse.setText(course);
		holder.rowCredit.setText(credit);
		holder.rowScore.setText(score);
		holder.rowAttr.setText(attr);

	}
	
	static class ViewHolder {
		TextView rowCourse;
		TextView rowCredit;
		TextView rowScore;
		TextView rowAttr;
	}
}
