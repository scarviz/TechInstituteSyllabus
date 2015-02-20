package jp.techinstitute.ti_noda.syllabus;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * 詳細画面のActivity
 */
public class CourseDetail extends ActionBarActivity {

	/**
	 * Activityが作成されたときに最初に呼ばれるもの(第2回の講義参照)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_detail);

		// CourseDetailを呼び出したMainActivityから渡されたIntentを受け取る
		Intent intent = getIntent();
		// 「date」という名前(キー)をつけた文字列を取り出す(第2回の講義参照)
		String dateStr = intent.getStringExtra("date");
		// 「title」という名前(キー)をつけた文字列を取り出す
		String titleStr = intent.getStringExtra("title");
		// 「teacher」という名前(キー)をつけた文字列を取り出す
		String teacherStr = intent.getStringExtra("teacher");
		// 「detail」という名前(キー)をつけた文字列を取り出す
		String detailStr = intent.getStringExtra("detail");

		// 「dateText」というIDをつけたTextView
		TextView dateText = (TextView)findViewById(R.id.dateText);
		dateText.setText(dateStr);
		// 「titleText」というIDをつけたTextView
		TextView titleText = (TextView)findViewById(R.id.titleText);
		titleText.setText(titleStr);
		// 「teacherText」というIDをつけたTextView
		TextView teacherText = (TextView)findViewById(R.id.teacherText);
		teacherText.setText(teacherStr);
		// 「detailText」というIDをつけたTextView
		TextView detailText = (TextView)findViewById(R.id.detailText);
		detailText.setText(detailStr);
	}


	/***********************************************************
	 * ここから下はメニュー項目関連(今回の講義では使用しない)
	 **********************************************************/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_course_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
