package jp.techinstitute.ti_noda.syllabus;

import android.app.Application;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.widget.TextView;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class CourseDetailTest extends ActivityInstrumentationTestCase2<CourseDetail> {
	public CourseDetailTest() {
		super(CourseDetail.class);
	}

	/**
	 * Intentの情報がCourseDetailの各Viewに正しく設定されているかテストする
	 */
	public void testDisplayedTest(){
		// Intentの準備
		Intent intent = new Intent();
		intent.putExtra("date", "2015/02/22");
		intent.putExtra("title", "ユーティリティによる実践");
		intent.putExtra("teacher", "野田悟志");
		intent.putExtra("detail", "この講義では一つのアプリとして仕上げることを目指します。");
		intent.putExtra("supportTeacher", "補助講師1" + "\n" + "補助講師2" + "\n" + "補助講師3");

		// Activity(CourseDetail)にIntentを設定する
		setActivityIntent(intent);

		// Activity(CourseDetail)を取得する
		CourseDetail courseDetail = getActivity();

		// 各Viewを取得する
		TextView dateText = (TextView)courseDetail.findViewById(R.id.dateText);
		TextView titleText = (TextView)courseDetail.findViewById(R.id.titleText);
		TextView teacherText = (TextView)courseDetail.findViewById(R.id.teacherText);
		TextView detailText = (TextView)courseDetail.findViewById(R.id.detailText);
		TextView supportTeacherText = (TextView)courseDetail.findViewById(R.id.supportTeacherText);

		// 各Viewからテキストを取得する
		String date = String.valueOf(dateText.getText());
		String title = String.valueOf(titleText.getText());
		String teacher = String.valueOf(teacherText.getText());
		String detail = String.valueOf(detailText.getText());
		String supportTeacher = String.valueOf(supportTeacherText.getText());

		// テキストが正しく設定されているか検証する
		assertEquals("2015/02/22", date);
		assertEquals("ユーティリティによる実践", title);
		assertEquals("野田悟志", teacher);
		assertEquals("この講義では一つのアプリとして仕上げることを目指します。", detail);
		assertEquals("補助講師1" + "\n" + "補助講師2" + "\n" + "補助講師3", supportTeacher);
	}
}