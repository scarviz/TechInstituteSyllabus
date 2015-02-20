package jp.techinstitute.ti_noda.syllabus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

		itemList = new ArrayList<CourseItem>();
		adapter = new ItemAdapter(getApplicationContext(), 0, itemList);

		ListView listView =	(ListView)findViewById(R.id.listview);
		listView.setAdapter(adapter);

		setCourseData();

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CourseItem item = (CourseItem)parent.getItemAtPosition(position);

				Intent intent = new Intent(MainActivity.this, CourseDetail.class);
				intent.putExtra("date", item.date);
				intent.putExtra("title", item.title);
				intent.putExtra("teacher", item.teacher);
				intent.putExtra("detail", item.detail);

				startActivity(intent);
			}
		});
	}

	/**
	 * リスト情報を設定する
	 */
	private void setCourseData() {
		CourseItem item = new CourseItem();
		item.date = "2/22";
		item.title = "ユーティリティによる実践(1)";
		item.teacher = "野田悟志";
		item.detail = "この講義では一つのアプリとして仕上げることを目指します。";
		itemList.add(item);

		item = new CourseItem();
		item.date = "2/22";
		item.title = "ユーティリティによる実践(2)";
		item.teacher = "野田悟志";
		item.detail = "一つのアプリを仕上げることを目指す２回目。";
		itemList.add(item);
	}

	/**
	 * リストに情報を設定するためのアダプター
	 * これは「クラス」というもの
	 */
	private class ItemAdapter extends ArrayAdapter<CourseItem> {
		private LayoutInflater inflater;

		/**
		 * このアダプターで最初に呼ばれるもの
		 * 「コンストラクタ」というもの
		 */
		public ItemAdapter(Context context, int resource, List<CourseItem> objects) {
			super(context, resource, objects);
			inflater = LayoutInflater.from(context);
			// 上の一行は内部で下の一行(教科書で書かれている分)の処理を行ってるので、どっちでも構わない
			// inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		/**
		 * 行のViewをまとめて保持するもの
		 */
		private class ViewHolder {
			/** 日付 */
			TextView date;
			/** 講義タイトル */
			TextView title;
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
			// ViewHolderを用意する(初期化はしていないので、後で値を格納する必要がある事に注意。第4回を参照)
			ViewHolder holder;
			// 以前に描画されていない場合
			if(convertView == null) {
				// ViewをLayoutInflaterで作成しておく
				convertView = inflater.inflate(R.layout.lecture_row, null, false);

				// ViewHolderの入れ物を用意する
				holder = new ViewHolder();
				// 「lecture_row.xml」で定義したTextViewを取得してViewHolderのViewに格納する
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.title = (TextView) convertView.findViewById(R.id.title);

				// 「setTag」でViewHolderを保持する
				convertView.setTag(holder);
			} else {
				// 以前に描画されているので、「getTag」で保持しているViewHolderを取得する
				holder = (ViewHolder)convertView.getTag();
			}

			// getItemを使うと、アダプターに設定したリスト情報(List<CourseItem>)から、
			// 指定した位置(position)の情報を取得することが出来る
			CourseItem item = getItem(position);

			// 取得した情報から、ViewHolderのViewに値を設定する
			holder.date.setText(item.date);
			holder.title.setText(item.title);

			// 描画するViewを返す
			return convertView;
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
