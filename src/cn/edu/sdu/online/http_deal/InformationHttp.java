package cn.edu.sdu.online.http_deal;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.modal.LeftMenuItem;
import cn.edu.sdu.online.modal.Lesson;
import cn.edu.sdu.online.modal.MyHomework;
import cn.edu.sdu.online.modal.MyInfo;
import cn.edu.sdu.online.modal.Person;
import cn.edu.sdu.online.modal.Course;
import cn.edu.sdu.online.modal.SignRelease;
import cn.edu.sdu.online.utils.SignUtils;
import cn.edu.sdu.online.view.LockPatternView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.exception.DbException;

/**
 * <p>
 * �������ύ���� ����ȡ�������ķ���ֵ
 * </p>
 * 
 * @author Javer seal
 */
@SuppressWarnings("unused")
public class InformationHttp {
	/**
	 * ������緵��ֵ��0 - ��ȷ 1 - �û����� �������
	 * 
	 * @param inputStream
	 * @param encode
	 * @return
	 */
	static Main app;
	

	public InformationHttp() {
		app = Main.getApp();
		
	}

	public static void changeInputStream(String getInfo) {
		int len;
		TreeMap<Integer, List<Course>> semList = new TreeMap<Integer, List<Course>>();

		String[] strarray = getInfo.split("ѧ�����߿γ̸���");

		Gson gson = new Gson();
		for (int i = 1; i < strarray.length; i++) {
			// ͨ��switchѡ���Է�û����ʱ��ȡ����
			switch (i) {
			case 1:
				Person person = new Person();// ��ȡ�û�������Ϣ
				person = gson.fromJson(strarray[1], Person.class);
				Main.getApp().setPerson(person);
				// System.out.println(person);
				break;
			case 2:
				List<Lesson> lessonList = new ArrayList<Lesson>(); // ��ȡ�γ̱����
				lessonList = gson.fromJson(strarray[2],
						new TypeToken<List<Lesson>>() {
						}.getType());
				Main.getApp().setCurriculumArray(lessonList);

				break;
			case 3:
				List<Course> semesterlist1 = new ArrayList<Course>(); // ��ȡ��һѧ�ڳɼ����б����
				semesterlist1 = gson.fromJson(strarray[3],
						new TypeToken<List<Course>>() {
						}.getType());
				// System.out.println(semesterlist1);
				semList.put(1, semesterlist1);

				break;
			case 4:
				List<Course> semesterlist2 = new ArrayList<Course>(); // ��ȡ�ڶ�ѧ�ڿγɼ����б�
				semesterlist2 = gson.fromJson(strarray[4],
						new TypeToken<List<Course>>() {
						}.getType());
				semList.put(2, semesterlist2);

				break;
			case 5:
				List<Course> semesterlist3 = new ArrayList<Course>(); // ��ȡ����ѧ�ڿγɼ����б�
				semesterlist3 = gson.fromJson(strarray[5],
						new TypeToken<List<Course>>() {
						}.getType());
				semList.put(3, semesterlist3);
				break;
			case 6:
				List<Course> semesterlist4 = new ArrayList<Course>(); // ��ȡ����ѧ�ڿγɼ����б�
				semesterlist4 = gson.fromJson(strarray[6],
						new TypeToken<List<Course>>() {
						}.getType());
				semList.put(4, semesterlist4);
				break;
			case 7:
				List<Course> semesterlist5 = new ArrayList<Course>(); // ��ȡ����ѧ�ڿγɼ����б�
				semesterlist5 = gson.fromJson(strarray[7],
						new TypeToken<List<Course>>() {
						}.getType());
				semList.put(5, semesterlist5);
				System.out.println(semesterlist5);
				break;
			case 8:
				List<Course> semesterlist6 = new ArrayList<Course>(); // ��ȡ����ѧ�ڿγɼ����б�
				semesterlist6 = gson.fromJson(strarray[8],
						new TypeToken<List<Course>>() {
						}.getType());
				semList.put(6, semesterlist6);
				System.out.println(semesterlist6);
				break;
			case 9:
				List<Course> semesterlist7 = new ArrayList<Course>(); // ��ȡ����ѧ�ڿγɼ����б�
				semesterlist7 = gson.fromJson(strarray[9],
						new TypeToken<List<Course>>() {
						}.getType());
				semList.put(7, semesterlist7);
				System.out.println(semesterlist7);
				break;
			case 10:
				List<Course> semesterlist8 = new ArrayList<Course>(); // ��ȡ�ڰ�ѧ�ڿγɼ����б�
				semesterlist8 = gson.fromJson(strarray[10],
						new TypeToken<List<Course>>() {
						}.getType());
				semList.put(8, semesterlist8);
				System.out.println(semesterlist8);
				break;
			case 11:
				MyInfo info = new MyInfo();
				info = gson.fromJson(strarray[11], MyInfo.class);
				if (info != null) {
					if (info.getName() != null) {
						Main.getApp().getDataStore().edit()
								.putString("Info_name", info.getName())
								.commit();
					}
					if (info.getAim() != null) {
						Main.getApp().getDataStore().edit()
								.putString("Info_aim", info.getAim()).commit();
					}
					if (info.getSex() != null) {
						Main.getApp().getDataStore().edit()
								.putString("Info_sex", info.getSex()).commit();
					}
					if (info.getTalktome() != null) {
						Main.getApp().getDataStore().edit()
								.putString("Info_talktome", info.getTalktome())
								.commit();
					}
					if (info.getImagepath() != null) {
						Main.getApp()
								.getDataStore()
								.edit()
								.putString("faceimagepath", info.getImagepath())
								.commit();
					}
					if (info.getPassword() != null) {
						Main.getApp().getDataStore().edit()
								.putString("lock", info.getPassword()).commit();
						Main.getApp().getDataStore().edit()
								.putBoolean("iflock", true).commit();
					}
					if (info.getIfsign() != null) {
						Main.getApp().getDataStore().edit()
								.putString("signif", info.getIfsign()).commit();
					}
					Main.getApp().getDataStore().edit()
							.putInt("signrank", info.getSignrank()).commit();
					Main.getApp().getDataStore().edit()
							.putInt("signcontinue", info.getSigncontinue())
							.commit();
					Main.getApp().getDataStore().edit()
							.putInt("signall", info.getSignall()).commit();
					Main.getApp().getDataStore().edit()
							.putInt("signday", info.getDay()).commit();
				}
				break;
			case 12:
				ArrayList<MyHomework> home = new ArrayList<MyHomework>();
				home = gson.fromJson(strarray[12],
						new TypeToken<List<MyHomework>>() {
						}.getType());
				Main.getApp().setHomework("HOMEWORK", home);
				break;
			default:
				break;
			}

		}

		Main.getApp().setCompulsoryList(semList);

	}
}
