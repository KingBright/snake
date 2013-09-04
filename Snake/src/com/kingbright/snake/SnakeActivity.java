package com.kingbright.snake;

import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SnakeActivity extends Activity {
	MediaPlayer mMediaPlayer;
	private int delay = 400;
	private SnakeView mSnakeView;
	private Runnable updateAction = new Runnable() {
		public void run() {
			mSnakeView.update();
			mSnakeView.postDelayed(updateAction, delay);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

		setContentView(R.layout.activity_snake);

		mSnakeView = (SnakeView) findViewById(R.id.snake_view);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSnakeView.removeCallbacks(updateAction);
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSnakeView.post(updateAction);
		mMediaPlayer = MediaPlayer.create(this, R.raw.music);
		try {
			mMediaPlayer.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mMediaPlayer.setLooping(true);
		mMediaPlayer.start();
	}

}
