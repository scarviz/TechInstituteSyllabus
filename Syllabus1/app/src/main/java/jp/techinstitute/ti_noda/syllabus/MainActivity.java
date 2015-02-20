package jp.techinstitute.ti_noda.syllabus;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 一覧画面のActivity
 */
public class MainActivity extends ActionBarActivity {
	/**
	 * リストの一行当たりの情報を格納するためのもの
	 */
	private class CourseItem {
		/** 日付 */
		String date;
		/** 講義タイトル */
		String title;
		/** 講師名 */
		String teacher;
		/** 詳細内容 */
		String detail;
	}

	/** 一行当たりの情報を、複数束ねて保持するもの。リスト用の情報 */
	private List<CourseItem> itemList;
	/** リストに情報を設定するためのアダプターになる */
	private ItemAdapter adapter;

	/**
	 * Activityが作成されたときに最初に呼ばれるもの(第2回の講義参照)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 「new」でリスト用の情報の入れ物を用意する
		itemList = new ArrayList<CourseItem>();

		// 「new」でアダプターを用意する
		// 「getApplicationContext()」では、「Context」と呼ばれるアプリの状態(Activityの情報など)を
		// 受け渡しするものを取ってきている。ItemAdapterの引数に設定することで、ItemAdapterに渡している
		adapter = new ItemAdapter(getApplicationContext(), 0, itemList);

		// 「activity_main.xml」で定義したListViewを取得している(第2回、第3回の講義参照)
		ListView listView =	(ListView)findViewById(R.id.listview);
		// 「setAdapter」でアダプターをリストに設定する
		listView.setAdapter(adapter);

		// リスト情報(itemList)に、実際に表示する情報を設定する
		setCourseData();
	}

	/**
	 * リスト情報を設定する
	 */
	private void setCourseData() {
		// 「new」で新しく情報を格納する入れ物を用意する
		CourseItem item = new CourseItem();
		// 各情報を設定する
		item.date = "2/22";
		item.title = " ユーティリティによる実践(1)";
		item.teacher = " 野田悟志";
		item.detail = " この講義では一つのアプリとして仕上げることを目指します。";
		// 「add」でリスト(itemList)に情報を追加で格納する
		itemList.add(item);

		// 新しい情報の入れ物を用意する
		item = new CourseItem();
		// 各情報を設定
		item.date = "2/22";
		item.title = " ユーティリティによる実践(2)";
		item.teacher = " 野田悟志";
		item.detail = " 一つのアプリを仕上げることを目指す２回目。";
		// リストに追加
		itemList.add(item);
	}

	/**
	 * リストに情報を設定するためのアダプター
	 * これは「クラス」というもの
	 */
	private class ItemAdapter extends ArrayAdapter<CourseItem> {
		// レイアウトを拡張するためのもの
		private LayoutInflater inflater;

		/**
		 * このアダプターで最初に呼ばれるもの
		 * 「コンストラクタ」というもの
		 */
		public ItemAdapter(Context context, int resource, List<CourseItem> objects) {
			super(context, resource, objects);
			// レイアウトを拡張する「LayoutInflater」を用意する
			inflater = LayoutInflater.from(context);
			// 上の一行は内部で下の一行(教科書で書かれている分)の処理を行ってるので、どっちでも構わない
			// inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		/**
		 * リストに情報を描画するもの
		 * これは「メソッド」というもの
		 *
		 * @param position 描画する行の位置(何行目か)が格納されている
		 * @param convertView 以前に描画していると、そのView情報が格納されている(値は再度入れなおすこと)
		 * @param parent ViewGroupというものが格納されている。今回は使用しないので気にしなくてOK
		 * @return 最後に描画するViewを返す
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 「lecture_row.xml」を拡張するためにLayoutInflaterを使っている
			// 実際には、拡張しているというよりは「lecture_row.xml」の子Viewを扱えるようにしている
			View view = inflater.inflate(R.layout.lecture_row, null, false);

			// 「lecture_row.xml」で定義したTextViewを取得している(第2回、第3回の講義参照)
			TextView dateView = (TextView) view.findViewById(R.id.date);
			TextView titleView = (TextView) view.findViewById(R.id.title);

			// getItemを使うと、アダプターに設定したリスト情報(List<CourseItem>)から、
			// 指定した位置(position)の情報を取得することが出来る
			CourseItem item = getItem(position);

			// 取得した情報から、各TextViewに値を設定する
			dateView.setText(item.date);
			titleView.setText(item.title);

			// 描画するViewを返す
			return view;
		}
	}


	/***********************************************************
	 * ここから下はメニュー項目関連(今回の講義では使用しない)
	 **********************************************************/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
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
