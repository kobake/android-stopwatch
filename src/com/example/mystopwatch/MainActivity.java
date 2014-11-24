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
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Timer mTimer = null;
	private Handler mHandler = new Handler();
	private boolean mIsRunning = false;

	private long mStartTime;
	private long mStopTime;
	private long mDelta = 0l;
	
	// ウィジェット
	private TextView mTextView;
	private Button mStartButton;
	private Button mStopButton;
	private Button mResetButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTextView = (TextView)findViewById(R.id.textView1);
		mStartButton = (Button)findViewById(R.id.buttonStart);
		mStopButton = (Button)findViewById(R.id.buttonStop);
		mResetButton = (Button)findViewById(R.id.buttonReset);
		
		setButtonStates(true, false, false);
	}
	
	// ボタンの有効無効化
	private void setButtonStates(boolean start, boolean stop, boolean reset){
		mStartButton.setEnabled(start);
		mStopButton.setEnabled(stop);
		mResetButton.setEnabled(reset);
	}
	
	public void startTimer(View v){
		// start時の時刻を取得 -> startTime
		if(!mIsRunning){
			mStartTime = SystemClock.elapsedRealtime(); // デバイスが起動してからの経過ミリ秒 (※System.currentTimeMillis … 現在時刻)
			mIsRunning = true;
		}
		else{
			mDelta += SystemClock.elapsedRealtime() - mStopTime;
			//mStartTime = SystemClock.elapsedRealtime() 
		}
		
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
						String s = f.format(new Date(SystemClock.elapsedRealtime() - mStartTime - mDelta));
						mTextView.setText(s);
					}
				});
			}
		}, 0, 10);	// ※schedule: 前回のタスクが完了してからの時間を指定。
					// ※scheduleAtFixedRate: 前回のタスクの完了時間に関わらず一定の周期で実行。 
		
		// ボタンの有効無効化
		setButtonStates(false, true, false);		
	}
	
	// Stop button
	public void stopTimer(View v){
		mStopTime = SystemClock.elapsedRealtime();
		mTimer.cancel(); // Timerのインスタンスは破棄される
		setButtonStates(true, false, true);
	}
	
	// Reset button
	public void resetTimer(View v){
		mIsRunning = false;
		mDelta = 0l;
		mTextView.setText("00:00.000");
		setButtonStates(true, false, false);
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
