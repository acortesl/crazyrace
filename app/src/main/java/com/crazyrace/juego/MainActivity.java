package com.crazyrace.juego;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.crazyrace.R;
import com.crazyrace.util.Constants;

public class MainActivity extends Activity {
	View mDecorView;
	private Game game;


	private String playerName;
	private float volume;
	private int level;
	private int stage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDecorView = getWindow().getDecorView();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Bundle data = getIntent().getExtras();

		if(data!= null){
			playerName = data.getString(Constants.PLAYER_NAME,getResources().getString(R.string.defaulPlayerName));
			volume = data.getFloat(Constants.VOLUME,Constants.DEFAULT_VOLUME);
			level = data.getInt(Constants.LEVEL,Constants.DEFAULT_LEVEL);
			stage = data.getInt(Constants.STAGE, Constants.DEFAULT_STAGE);
		}else{
			playerName =getResources().getString( R.string.defaulPlayerName);
			volume = Constants.DEFAULT_VOLUME;
			level = Constants.DEFAULT_LEVEL;
			stage = Constants.DEFAULT_STAGE;
		}

		game = new Game(this, playerName, volume, level, stage, MainActivity.this);
		setContentView(game);
				
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			mDecorView.setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_FULLSCREEN
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}
		
	protected void onPause(){
		super.onPause();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			game.onPause();
			game = null;
		}

	}

	protected void onResume(){
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			Log.v("TEXTUREVIEW","resume");
			if (game == null) {
				Log.v("TEXTUREVIEW","newgame");
				game = new Game(this, playerName, volume, level, stage, MainActivity.this);
				setContentView(game);
			}
		}
	}

}
