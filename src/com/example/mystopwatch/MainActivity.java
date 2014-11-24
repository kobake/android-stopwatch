package com.example.mystopwatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private long mStartTime;
	private Timer mTimer = null;
	private Handler mHandler = new Handler();
	private TextView mTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTextView = (TextView)findViewById(R.id.textView1);
	}
	
	public void startTimer(View v){
		// start時の時刻を取得 -> startTime
		mStartTime = SystemClock.elapsedRealtime(); // デバイスが起動してからの経過ミリ秒 (※System.currentTimeMillis … 現在時刻)
		
		// 一定時間毎に現在時刻を取得してstartTimeとの差分を表示
		/*
		Timer
		- scheduleAtFixedRate()
			- TimerTask 抽象クラス
				- run()
		 */
		mTimer = new Timer(true); // false:ユーザスレッド / true:デーモンスレッド
		mTimer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						SimpleDateFormat f = new SimpleDateFormat("mm:ss.SSS");
						String s = f.format(new Date(SystemClock.elapsedRealtime() - mStartTime));
						mTextView.setText(s);
					}
				});
			}
		}, 0, 10);	// ※schedule: 前回のタスクが完了してからの時間を指定。
					// ※scheduleAtFixedRate: 前回のタスクの完了時間に関わらず一定の周期で実行。 
		
		// ボタンの有効無効化
		
	}
	public void stopTimer(View v){
		
	}
	public void resetTimer(View v){
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}