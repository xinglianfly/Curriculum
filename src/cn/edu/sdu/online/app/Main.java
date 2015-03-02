package cn.edu.sdu.online.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;
import cn.edu.sdu.online.R;
import cn.edu.sdu.online.fragments_first.About;
import cn.edu.sdu.online.fragments_first.AlterPersonInfo;
import cn.edu.sdu.online.fragments_first.ConsultCenter;
import cn.edu.sdu.online.fragments_first.CourseImport;
import cn.edu.sdu.online.fragments_first.Curriculum;
import cn.edu.sdu.online.fragments_first.GradePoint;
import cn.edu.sdu.online.fragments_first.Room;
import cn.edu.sdu.online.fragments_first.Score;
import cn.edu.sdu.online.modal.Course;
import cn.edu.sdu.online.modal.LeftMenuItem;
import cn.edu.sdu.online.modal.Lesson;
import cn.edu.sdu.online.modal.MyHomework;
import cn.edu.sdu.online.modal.Person;
import cn.edu.sdu.online.modal.Question;
import cn.edu.sdu.online.modal.SignRelease;
import cn.edu.sdu.online.utils.DateUtil;

/**
 * 锟芥储锟斤拷锟斤拷锟斤拷锟斤拷要锟侥课憋拷锟缴硷拷锟皆硷拷锟斤拷锟斤拷锟斤拷息
 * 
 * @author seal
 * 
 */
public class Main extends Application {

	public static final String STR_CURRICULUM = "我的课表";
	public static final int INT_CURRICULUM = 1;
	public static final String STR_SCORE = "我的成绩";
	public static final int INT_SCORE = 2;
	public static final String STR_GRADE_POINT = "我的绩点";
	public static final int INT_GRADE_POINT = 3;
	public static final String STR_ROOM = "自习室";
	public static final int INT_ROOM = 4;
	public static final String STR_IMPORT = "课程导入";
	public static final int INT_IMPORT = 5;
	public static final String STR_INFO = "个人信息";
	public static final int INT_INFO = 6;
	public static final int INT_ABOUT = 8;
	public static final String STR_ABOUT = "关于";
	public static final String STR_PASSWORD = "密码";
	public static final int INT_PASSWORD = 10;
	public static final String STR_QUIT = "退出";
	public static final int INT_QUIT = 9;
	public static final String STR_CONSULT = "咨询中心";
	public static final int INT_CONSULT = 7;
	private final String CURRICULUM = "curriculum";
	private final String COLORS = "colors";
	private static final String SDPATH = Environment
			.getExternalStorageDirectory() + "";
	private static Main app;

	// 锟轿程憋拷锟斤拷锟斤拷
	public String[] curriculumArray;
	private Person person;
	private Drawable[] colors;

	public static TreeMap<Integer, LeftMenuItem> menuItems;
	public static ArrayList<LeftMenuItem> menu_items;
	private int[] intColors;
	int[] drawable_colors = new int[] { R.drawable.color_1, R.drawable.color_2,
			R.drawable.color_3, R.drawable.color_4, R.drawable.color_5,
			R.drawable.color_6, R.drawable.color_7, R.drawable.color_8,
			R.drawable.color_9, R.drawable.color_10, R.drawable.color_11,
			R.drawable.color_12, R.drawable.color_13, R.drawable.color_14,
			R.drawable.color_15, R.drawable.color_16 };
	DateUtil date = new DateUtil();
	List<List<Integer>> all;

	public Drawable[] getColors() {
		return colors;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		colors = new Drawable[drawable_colors.length];
		for (int i = 0; i < drawable_colors.length; i++) {
			colors[i] = getResources().getDrawable(drawable_colors[i]);
		}


		menuItems = new TreeMap<Integer, LeftMenuItem>();

		menuItems.put(
				Main.INT_CURRICULUM,
				ItemMaker(Main.STR_CURRICULUM, new Curriculum(),
						R.drawable.icon_curriculum));
		menuItems.put(Main.INT_SCORE,
				ItemMaker(Main.STR_SCORE, new Score(), R.drawable.icon_score));
		menuItems.put(
				Main.INT_GRADE_POINT,
				ItemMaker(Main.STR_GRADE_POINT, new GradePoint(),
						R.drawable.icon_grade_point));
		menuItems.put(Main.INT_ROOM,
				ItemMaker(Main.STR_ROOM, new Room(), R.drawable.zixishi));
		menuItems.put(
				Main.INT_IMPORT,
				ItemMaker(Main.STR_IMPORT, new CourseImport(),
						R.drawable.icon_course_import));
		menuItems.put(
				Main.INT_INFO,
				ItemMaker(Main.STR_INFO, new AlterPersonInfo(),
						R.drawable.icon_information));
		menuItems.put(Main.INT_ABOUT,
				ItemMaker(Main.STR_ABOUT, new About(), R.drawable.icon_about));
		menuItems.put(Main.INT_QUIT,
				ItemMaker1(Main.STR_QUIT, R.drawable.icon_quit));
		menuItems.put(
				Main.INT_CONSULT,
				ItemMaker(Main.STR_CONSULT, new ConsultCenter(),
						R.drawable.icon_about));
		menu_items = new ArrayList<LeftMenuItem>();
		menu_items.addAll(menuItems.values());

		if (getDataStore().getBoolean("isLogin", false)) {
			menu_items.remove(menuItems.get(INT_IMPORT));
		} else {
			menu_items.remove(menuItems.get(INT_INFO));
		}

	}

	// public void generateRandomColors() {
	// intColors = new int[35];
	// Random ran = new Random();
	// for (int i = 0; i < 35; i++) {
	// intColors[i] = ran.nextInt(7);
	// }
	// try {
	// FileOutputStream out = openFileOutput(COLORS, MODE_PRIVATE);
	// ObjectOutputStream os = new ObjectOutputStream(out);
	// os.writeObject(intColors);
	// os.close();
	// out.close();
	// getDataStore().edit().putBoolean(COLORS, true).commit();
	// } catch (FileNotFoundException e) {
	// } catch (IOException e) {
	// }
	// }
	public void generateRandomColors() {
		int a = 0;
		intColors = new int[35];
		// Random ran = new Random();
		for (int i = 0; i < all.size(); i++) {
			for (int ii = 0; ii < all.get(i).size(); ii++) {
				intColors[all.get(i).get(ii)] = a;
			}
			a++;
		}

		// for (int i = 0; i < 35; i++) {
		// intColors[i] = ran.nextInt(7);
		// }
		try {
			FileOutputStream out = openFileOutput(COLORS, MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(intColors);
			os.close();
			out.close();
			getDataStore().edit().putBoolean(COLORS, true).commit();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	public int[] getIntColors() {
		if (intColors != null) {
			return intColors;
		}

		if (getDataStore().getBoolean(COLORS, false)) {
			try {
				FileInputStream in = openFileInput(COLORS);
				ObjectInputStream is = new ObjectInputStream(in);
				intColors = (int[]) is.readObject();
				is.close();
				in.close();
			} catch (FileNotFoundException e) {
			} catch (StreamCorruptedException e) {
			} catch (IOException e) {
			} catch (ClassNotFoundException e) {
			}
		} else {
			// generateRandomColors();
		}
		return intColors;
	}

	public void setAskAndAnswer(String filename, List<Question> list) {
		try {
			FileOutputStream out = openFileOutput(filename, MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(list);
			os.close();
			out.close();
			getDataStore().edit().putBoolean(filename, true).commit();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Question> getAskandAnswer(String filename) {
		if (getDataStore().getBoolean(filename, false)) {
			try {
				FileInputStream in = openFileInput(filename);
				ObjectInputStream is = new ObjectInputStream(in);
				List<Question> list = (List<Question>) is.readObject();
				is.close();
				in.close();
				return list;
			} catch (FileNotFoundException e) {
			} catch (StreamCorruptedException e) {
			} catch (IOException e) {
			} catch (ClassNotFoundException e) {
			}
		} else {
			return new ArrayList<Question>();
		}
		return new ArrayList<Question>();
	}

	public void setHomework(String filename, ArrayList<MyHomework> homelist) {
		try {
			FileOutputStream out = openFileOutput(filename, MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(homelist);
			os.close();
			out.close();
			getDataStore().edit().putBoolean(filename, true).commit();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<MyHomework> getHomework(String filename) {
		if (getDataStore().getBoolean(filename, false)) {
			try {
				FileInputStream in = openFileInput(filename);
				ObjectInputStream is = new ObjectInputStream(in);
				ArrayList<MyHomework> list = (ArrayList<MyHomework>) is
						.readObject();
				is.close();
				in.close();
				return list;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			return new ArrayList<MyHomework>();
		}
		return new ArrayList<MyHomework>();
	}

	public void setRankandComment(String filename, List<SignRelease> list) {

		try {
			FileOutputStream out = openFileOutput(filename, MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(list);
			os.close();
			out.close();
			getDataStore().edit().putBoolean(filename, true).commit();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<SignRelease> getRankandComment(String filename) {
		if (getDataStore().getBoolean(filename, false)) {
			try {
				FileInputStream in = openFileInput(filename);
				ObjectInputStream is = new ObjectInputStream(in);
				List<SignRelease> list = (List<SignRelease>) is.readObject();
				is.close();
				in.close();
				return list;
			} catch (FileNotFoundException e) {
			} catch (StreamCorruptedException e) {
			} catch (IOException e) {
			} catch (ClassNotFoundException e) {
			}
		} else {
			return new ArrayList<SignRelease>();
		}
		return new ArrayList<SignRelease>();
	}

	public void setAppPackageName(String filename, ArrayList<String> list) {
		try {
			FileOutputStream out = openFileOutput(filename, MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(list);
			os.close();
			out.close();
			getDataStore().edit().putBoolean(filename, true).commit();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void number(List<Lesson> lessonList) {
		String namec[] = new String[35];
		boolean ifrepeat[] = new boolean[35];
		all = new ArrayList<List<Integer>>();
		for (int ii = 0; ii < 35; ii++) {
			ifrepeat[ii] = false;
		}

		for (Lesson lesson : lessonList) {
			int position = lesson.getClassDayOfWeek() + 7
					* (lesson.getClassDayOfTime() - 1) - 1;
			if (namec[position] != null) {
				namec[position] += lesson.getClassName();
			} else {
				namec[position] = lesson.getClassName();
			}
		}
		for (int i = 0; i < 35; i++) {
			List<Integer> save = new ArrayList<Integer>();
			if (namec[i] != null && ifrepeat[i] == false) {
				ifrepeat[i] = true;
				String temp = namec[i];
				save.add(i);
				for (int m = i + 1; m < 35; m++) {
					if (namec[m] != null && ifrepeat[m] == false) {
						if (namec[m].equals(temp)) {
							ifrepeat[m] = true;
							save.add(m);
						}
					}

				}
				all.add(save);
			}

		}
	}

	public void setCurriculumArray(List<Lesson> lessonList) {

		curriculumArray = new String[35];
		for (Lesson lesson : lessonList) {

			int position = lesson.getClassDayOfWeek() + 7
					* (lesson.getClassDayOfTime() - 1) - 1;
			if (curriculumArray[position] != null)
				curriculumArray[position] += "\n" + lesson.getClassName()
						+ "\n" + lesson.getClassPlace();
			else
				curriculumArray[position] = lesson.getClassName() + "\n"
						+ lesson.getClassPlace();
		}

		number(lessonList);
		generateRandomColors();

		try {
			FileOutputStream out = openFileOutput(CURRICULUM, MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(curriculumArray);
			os.close();
			out.close();

			getDataStore().edit().putBoolean(CURRICULUM, true).commit();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	public String[] getCurriculumArray() {
		if (curriculumArray != null)
			return curriculumArray;

		if (getDataStore().getBoolean(CURRICULUM, false)) {
			try {
				FileInputStream in = openFileInput(CURRICULUM);
				ObjectInputStream is = new ObjectInputStream(in);
				curriculumArray = (String[]) is.readObject();
				is.close();
				in.close();
			} catch (FileNotFoundException e) {
			} catch (StreamCorruptedException e) {
			} catch (IOException e) {
			} catch (ClassNotFoundException e) {
			}
		} else {
			curriculumArray = new String[35];
			return curriculumArray;
		}
		return curriculumArray;
	}

	public TreeMap<Integer, List<Course>> getMap() {
		return readFromFile("COMPULSORY");
	}

	public List<Course> getCompulsoryList(int semester) {
		return getMap().get(semester);

	}

	public void setCompulsoryList(TreeMap<Integer, List<Course>> compulsoryList) {
		if (compulsoryList == null) {
			compulsoryList = new TreeMap<Integer, List<Course>>();
		} else {
			writeToFile("COMPULSORY", compulsoryList);

		}
	}

	public void setPerson(Person person) {
		this.person = person;
		// 锟斤拷锟斤拷锟剿猴拷锟斤拷锟诫！
		getDataStore().edit().putString("stuid", person.getMyStudentID())
				.putString("stuname", person.getMyName())
				.putString("academy", person.getMyAcademy())
				.putString("specialty", person.getMySpecialty())
				.putFloat("year_1", person.getFirstAveGrade())
				.putFloat("year_2", person.getSecondAveGrade())
				.putFloat("year_3", person.getThridAveGrade())
				.putFloat("year_4", person.getForthAveGrade()).commit();

	}

	public Person getPerson() {
		return person;
	}

	private void writeToFile(String filename,
			TreeMap<Integer, List<Course>> treemap) {
		try {
			FileOutputStream out = openFileOutput(filename, MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(treemap);
			os.close();
			out.close();
			getDataStore().edit().putBoolean(filename, true).commit();
		} catch (FileNotFoundException e) {
			System.out.println(e + "yi chang shi");
		} catch (IOException e) {
			System.out.println("yi chang shi" + e);
		}

	}

	private TreeMap<Integer, List<Course>> readFromFile(String filename) {
		if (getDataStore().getBoolean(filename, false)) {
			try {
				FileInputStream in = openFileInput(filename);
				ObjectInputStream is = new ObjectInputStream(in);
				TreeMap<Integer, List<Course>> map = (TreeMap<Integer, List<Course>>) is
						.readObject();
				is.close();
				in.close();
				return map;
			} catch (FileNotFoundException e) {
			} catch (StreamCorruptedException e) {
			} catch (IOException e) {
			} catch (ClassNotFoundException e) {
			}
		} else {
			return new TreeMap<Integer, List<Course>>();
		}
		return new TreeMap<Integer, List<Course>>();
	}

	// 锟斤拷值锟皆仓匡拷 锟斤拷锟斤拷锟侥硷拷锟斤拷恰锟斤拷锟斤拷锟斤拷锟较�
	public SharedPreferences getDataStore() {
		return this.getSharedPreferences("curriculum", MODE_PRIVATE);
	}

	// 锟斤拷锟斤拷唯一锟斤拷application锟斤拷锟斤拷
	public static Main getApp() {
		return app;
	}

	// 锟斤拷锟剿碉拷锟斤拷目锟斤拷锟斤拷锟斤拷
	private LeftMenuItem ItemMaker(String title, Fragment fragment, int drawable) {
		LeftMenuItem item = new LeftMenuItem(title, fragment,
				BitmapFactory.decodeResource(getResources(), drawable));
		item.title = title;
		item.fragment = fragment;
		item.icon = BitmapFactory.decodeResource(getResources(), drawable);
		return item;
	}

	private LeftMenuItem ItemMaker1(String title, int drawable) {
		LeftMenuItem item = new LeftMenuItem(title,
				BitmapFactory.decodeResource(getResources(), drawable));
		item.title = title;
		item.icon = BitmapFactory.decodeResource(getResources(), drawable);
		return item;
	}

	public void clearAll() {
		getDataStore().edit().remove("COMPULSORY").remove("username")
				.remove("password").remove("stuid").remove("stuname")
				.remove("academy").remove("specialty").remove("year_1")
				.remove("year_2").remove("year_3").remove("year_4")
				.remove("FACEIMAGE").remove("FACESTORE").remove("edittalk")
				.remove("editsex").remove("editaim").remove("editname")
				.remove("ifsign").remove("signorder").remove("signrank")
				.remove("signcontinue").remove("lock").remove("Info_name")
				.remove("Info_sex").remove("Info_talktome").remove("Info_aim")
				.remove("iflock").remove("faceimagepath").remove("whichfrag")
				.remove("RANKANDCOMMENT").remove("HOMEWORK").remove("signday")
				.remove("signmonth").remove("signall").remove("isLogin")
				.remove("signif").remove("flagaskoldest")
				.remove("APPPACKAGENAME").remove("curriculum")
				.remove("ASKANDANSWER").commit();

	}

	public ConnectivityManager getConnectivityManager() {
		return (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	}

	public InputMethodManager getInputMethodManager() {
		return (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	}

	// 写锟斤拷sdcard锟斤拷
	public void writetofile_faceimage(Bitmap bm) {
		ByteArrayOutputStream baos;
		FileOutputStream out;
		try {
			baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);// png锟斤拷锟斤拷
			out = new FileOutputStream(new File(SDPATH + "/" + "faceimage.png"));
			out.write(baos.toByteArray());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getHour() {
		return date.getHour();
	}

	public int getSecond() {
		return date.getSecond();
	}

	public int getMinute() {
		return date.getMinute();
	}

	public int caloffset() {
		int offset;
		if (getHour() < 20) {
			offset = (19 - getHour()) * 60 * 60 * 1000 + (60 - getMinute())
					* 60 * 1000 + (60 - getSecond()) * 1000;
		} else if (getHour() == 20 && getMinute() == 0 && getSecond() == 0) {
			offset = 0;
		} else {
			offset = (43 - getHour()) * 60 * 60 * 1000 + (60 - getMinute())
					* 60 * 1000 + (60 - getSecond()) * 1000;
		}
		System.out.println("offset wei" + offset);
		return offset;
	}

	public Bitmap getLoacalBitmap(String url) {
		FileInputStream fis;
		Bitmap bit = null;
		try {
			fis = new FileInputStream(url);
			bit = BitmapFactory.decodeStream(fis);
			fis.close();

			// /锟斤拷锟斤拷转锟斤拷为Bitmap图片
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bit;
	}

}
