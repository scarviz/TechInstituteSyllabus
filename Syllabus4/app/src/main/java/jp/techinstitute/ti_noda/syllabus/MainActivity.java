package jp.techinstitute.ti_noda.syllabus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
		Date date;	// Date型という日付、時間を扱えるようになるもの
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

	/** プログレスバーを扱うためのもの */
	private ProgressBar progressBar;

	/** 「RequestQueue」というネットワークに接続して情報を取得する処理が複数件(1件でも)ある場合、適宜アクセスするように制御してくれるもの */
	private RequestQueue reqQueue;
	/** シラバス情報を取得する先のURL ※テキストとは違うので注意 */
	private static final String syllabusUrl = "https://script.google.com/macros/s/AKfycbyibdC1ZESlWsIYRIL4XFLfx7qRVzruWMHI6YFJ4qlUam4-p-Q3/exec?room=osaka";

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

		// プログレスバーを取得
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);

		// Volleyの準備
		reqQueue = Volley.newRequestQueue(this);
		// 講義情報を取得する
		getCourseData();

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CourseItem item = (CourseItem)parent.getItemAtPosition(position);

				Intent intent = new Intent(MainActivity.this, CourseDetail.class);
				// 詳細画面用の日付フォーマット
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				// フォーマットを適用する
				intent.putExtra("date", dateFormat.format(item.date));
				intent.putExtra("title", item.title);
				intent.putExtra("teacher", item.teacher);
				intent.putExtra("detail", item.detail);

				startActivity(intent);
			}
		});
	}

	/**
	 * 講義情報を取得する
	 */
	private void getCourseData() {
		// ネットワーク接続前にプログレスバーを表示する
		progressBar.setVisibility(View.VISIBLE);

		// ネットワーク接続し、接続先(サーバ)から返答が来たら呼ばれるリスナー
		// データはJSONと呼ばれる形式で受け取る。そのため「JSONObject」と呼ばれる型を指定する
		Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					// 「course」という名前でJSONデータの配列(JSONArray)が格納されてくるので、それを取り出す
					JSONArray array = response.getJSONArray("course");
					// リスト情報(itemList)に講義情報を格納する
					setCourseArray(array);
					// 処理が完了したので、プログレスバーを非表示にする
					progressBar.setVisibility(View.INVISIBLE);
				} catch (JSONException e) {
					// 処理中に何かしらの問題(例外)が発生したら、LogCatにその問題の内容を出力する
					e.printStackTrace();
				}
			}
		};

		// 何かしら失敗した場合に呼ばれるリスナー
		Response.ErrorListener errorListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// LogCatにエラーログを出力する
				Log.e("onResponse", "error=" + error);
			}
		};

		// JSONObject(JSON形式のデータ)をリクエスト(サーバへ要求)するもの
		JsonObjectRequest jsonReq = new JsonObjectRequest(syllabusUrl, null, listener, errorListener);
		// RequestQueueに追加する
		reqQueue.add(jsonReq);
	}

	/**
	 * リスト情報(itemList)に講義情報を格納する
	 * @param array JSONデータの配列(JSONArray)
	 * @throws JSONException throwsは、処理中に問題が発生(例外)した場合に、「setCourseArray」を呼んだところに、そのまま問題の内容を渡す
	 * 　　　　　　　　　　　そうすることで、呼び出し側で、発生した問題に対する処理を行えるようになる
	 */
	private void setCourseArray(JSONArray array) throws JSONException {
		// 配列の要素数(入れ物の個数)
		int num = array.length();

		// 日付フォーマット
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// 配列の要素数(入れ物の個数)分、繰り返し処理を行う
		// こうすればJSONデータの配列のすべての要素を見ることが出来る(第4回を参照)
		for(int i = 0; i < num; i++) {
			CourseItem item = new CourseItem();
			// JSONデータの配列の中から、JSONデータ(JSONObject)を取り出す
			JSONObject obj = array.getJSONObject(i);
			// JSONデータから「date」という名前のついたデータを取得する
			String dateStr = obj.getString("date");
			Date date = null;
			try {
				// 「date」という名前のついたデータを、日付型(Date)に変換する
				date = inputDateFormat.parse(dateStr);
				// 各データを格納していく
				item.date = date;
				item.title = obj.getString("title");
				item.teacher = obj.getString("teacher");
				item.detail = obj.getString("detail");
				itemList.add(item);
			} catch (ParseException e) {	// 「import Class」では「java.text.ParseException」を選択する
				// 処理中に発生した問題の内容をLogCatに出力する
				e.printStackTrace();
			}
		}

		// アダプターにリスト情報を変更したことを伝える
		adapter.notifyDataSetChanged();
	}

	/**
	 * リストに情報を設定するためのアダプター
	 * これは「クラス」というもの
	 */
	private class ItemAdapter extends ArrayAdapter<CourseItem> {
		private LayoutInflater inflater;

		// 日付のフォーマット
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");

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
			ViewHolder holder;
			if(convertView == null) {
				convertView = inflater.inflate(R.layout.lecture_row, null, false);

				holder = new ViewHolder();
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.title = (TextView) convertView.findViewById(R.id.title);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}

			CourseItem item = getItem(position);

			// フォーマットを適用する
			holder.date.setText(dateFormat.format(item.date));
			holder.title.setText(item.title);

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
