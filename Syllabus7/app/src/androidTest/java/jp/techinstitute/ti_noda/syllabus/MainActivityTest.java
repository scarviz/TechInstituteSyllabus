package jp.techinstitute.ti_noda.syllabus;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.MoreAsserts;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	public MainActivityTest() {
		super(MainActivity.class);
	}

	/**
	 * ListViewのタッチイベントで、Activity(CourseDetail)が起動することをテストする
	 * @throws Throwable runTestOnUiThreadを使用するためにはThrowableを定義する必要がある
	 */
	public void testStartActivity() throws Throwable {
		final MainActivity activity = getActivity();

		// Activityを監視するモニター
		// 遷移先のCourseDetailを監視するように設定
		Instrumentation.ActivityMonitor monitor
				= new Instrumentation.ActivityMonitor(
				CourseDetail.class.getName(), null, false);
		// instrumentation(仲介してくれるもの)にモニターを追加する
		getInstrumentation().addMonitor(monitor);

		// Viewの操作をする場合はrunTestOnUiThreadの中で行う必要がある
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				ListView listView =	(ListView)activity.findViewById(R.id.listview);

				// ListViewのアダプター
				ListAdapter listAdapter = listView.getAdapter();
				// タッチする要素として0番目(最初)を指定する
				int position = 0;
				// 最初の行をタッチ
				listView.performItemClick(
						listAdapter.getView(position, null, null),
						position,
						listAdapter.getItemId(position));
			}
		});

		// CourseDetailが起動されるのを確認するまで待つ。ただし15秒経ったらタイムアウトする
		Activity courseDetail = getInstrumentation().waitForMonitorWithTimeout(monitor, 15);
		// もう使用しないので、instrumentationからモニターを取り除く
		getInstrumentation().removeMonitor(monitor);

		// 起動したActivityがCourseDetailであっているかどうか検証する
		MoreAsserts.assertAssignableFrom(CourseDetail.class, courseDetail);
		// 1回のみ起動したことを検証する
		assertEquals(1, monitor.getHits());

		// CourseDetailを終了する
		courseDetail.finish();
	}
}
