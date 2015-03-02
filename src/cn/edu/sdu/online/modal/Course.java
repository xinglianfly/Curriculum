package cn.edu.sdu.online.modal;

import java.io.Serializable;

public class Course implements Serializable{
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getClassYear() {
		return classYear;
	}

	public void setClassYear(int classYear) {
		this.classYear = classYear;
	}

	 public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public String getClassCredit() {
		return classCredit;
	}

	public 	void setClassCredit(String classCredit) {
		this.classCredit = classCredit;
	}

	public String getClassAttitude() {
		return classAttitude;
	}

	public void setClassAttitude(String classAttitude) {
		this.classAttitude = classAttitude;
	}

	public String getClassGrade() {
		return classGrade;
	}

	public void setClassGrade(String classGrade) {
		this.classGrade = classGrade;
	}

	public String toString() {
		return "[ " + className + " : " + classYear + " , " + semester + " , "
				+ classCredit + " , " + classGrade + " , " + classAttitude
				+ " ]";
	}

	private String className;// ��Ŀ����
	private int classYear;// ȷ�ϸÿ�Ŀ����һ��ѧ���
	private int semester;// ��ѧѧ��,�������ѧ��,��ֵΪ0,�������ѧ��,��ֵΪ1
	private String classCredit;// ÿ�Ƶ�ѧ��,������Ϊѧ�ֲ�һ����int
	private String classAttitude;// ��Ŀ������:����,ѡ��,��ѡ
	private String classGrade;// ÿ�Ƶĳɼ�
}
