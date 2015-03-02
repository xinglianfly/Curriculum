package cn.edu.sdu.online.fragment_second;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.Adapter.LoadScoreAdapter;
import cn.edu.sdu.online.Listener.BackPressedListener;
import cn.edu.sdu.online.Listener.HomeButtonListener;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.modal.Course;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class SelectedSemesterScore extends Fragment implements
		HomeButtonListener, BackPressedListener {

	private ListView scoreList;
	private LayoutInflater inflater;
	String score[] = { "课程名", "学分", "成绩", "课程属性" };

	MainActivity activity;
	Main app;
	int semester;
	LoadScoreAdapter adapter;

	ViewHolder holder;

	@SuppressLint("ValidFragment")
	public SelectedSemesterScore(int semester) {
		this.semester = semester;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
		app = Main.getApp();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity.getSupportActionBar().setIcon(R.drawable.icon_back);
		activity.setHomeButtonListener(this);
		activity.setBackKeyDownListener(this);

		this.inflater = inflater;
		View view = inflater.inflate(R.layout.frag_selected_score, null);
		scoreList = (ListView) view.findViewById(R.id.score_list);
		adapter = new LoadScoreAdapter(semester, getActivity());
		// scoreList.setAdapter(new ScoreAdapter());
		scoreList.setAdapter(adapter);
		scoreList.setClickable(false);

		return view;
	}

	@Override
	public void onHomeButtonClick() {
		back();
	}

	@Override
	public void onBackPressed() {
		back();
	}

	private void back() {
		activity.setAnimIn(R.anim.right_push_in);
		activity.setAnimOut(R.anim.right_push_out);
		activity.switchFragment(activity.getMenuItems().get(Main.INT_SCORE).fragment);
		activity.setAnimIn(0);
		activity.setAnimOut(0);
		activity.setHomeButtonListener(null);
		activity.setBackKeyDownListener(null);
		activity.getSupportActionBar().setIcon(R.drawable.icon_topbar);
	}

	static class ViewHolder {
		TextView rowCourse;
		TextView rowCredit;
		TextView rowScore;
		TextView rowAttr;
	}

}
