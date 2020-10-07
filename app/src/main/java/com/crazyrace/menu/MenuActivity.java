package com.crazyrace.menu;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.crazyrace.db.DBCrazyRace;
import com.crazyrace.juego.MainActivity;
import com.crazyrace.objetos.Ranking;
import com.crazyrace.menu.MenuNuevaPartidaFragment.NewGameMenuInterface;
import com.crazyrace.menu.MenuOpcionesFragment.OptionsMenuInterface;
import com.crazyrace.menu.MenuRankingFragment.MenuRankingFragmentInterface;
import com.crazyrace.R;
import com.crazyrace.util.Constants;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


public class MenuActivity extends Activity implements NewGameMenuInterface,OptionsMenuInterface, MenuRankingFragmentInterface {

//	private final static float DEFAULT_VOL = 0.5f;
//	private final static String DEFAULT_LANG = "es";
//	private final static String DEFAULT_NAME = "Rebotable 1";
//	private final static int DEFAULT_LEVEL = 1;
//	private final static int DEFAULT_LASTSTAGE = 0;
//	private final static String LANG_ES = "es";
//	private final static String LANG_EN = "en";
	View mDecorView;
	final Fragment menuPrincipalFragment = new MenuMenuPrincipalFragment();
	final Fragment menuNuevaPartidaFragment = new MenuNuevaPartidaFragment();
	final Fragment menuRankingFragment = new MenuRankingFragment();
	final Fragment menuOpcionesFragment = new MenuOpcionesFragment();

	private float volumen;
	MediaPlayer mediaPlayer;
	MediaPlayer mediaPlayer2;
	private String currentLang;
	private String playerName;
	
	private int level; //dificultad de la nueva partida
	private int stage; //nivel de la nueva partida
	private int lastStage; //ultimo nivel superado

	private DBCrazyRace db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDecorView = getWindow().getDecorView();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.menu_activity);

		//preferencias

		SharedPreferences prefs = getSharedPreferences("crazyRacePrefs",Context.MODE_PRIVATE);
		currentLang = prefs.getString("language", Constants.DEFAULT_LANG);
		volumen = prefs.getFloat(Constants.VOLUME, Constants.DEFAULT_VOLUME);
		playerName = prefs.getString(Constants.PLAYER_NAME , getResources().getString(R.string.defaulPlayerName));
		level = prefs.getInt("level", Constants.DEFAULT_LEVEL);
		stage = prefs.getInt(Constants.STAGE,Constants.DEFAULT_LASTSTAGE);
		Log.v("params" ,"menuact oncreat: " + stage);
		Bundle args = new Bundle();
		args.putFloat(Constants.VOLUME, volumen);
		args.putString(Constants.PLAYER_NAME, playerName);
		args.putInt(Constants.LEVEL, level);
		args.putInt(Constants.STAGE, stage);

	    menuNuevaPartidaFragment.setArguments(args);
	    menuOpcionesFragment.setArguments(args);
	    menuRankingFragment.setArguments(args);
	    
		setLanguage();

		//animacion 1
		ImageView image = (ImageView) findViewById(R.id.backgImg);
		Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.anim2);
		image.startAnimation(hyperspaceJump);

		//animacion 2
		ImageView image2 = (ImageView) findViewById(R.id.backgImg2);
		Animation hyperspaceJump2 = AnimationUtils.loadAnimation(this, R.anim.animation);
		image2.startAnimation(hyperspaceJump2);

		//fragment menu principal
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.contenedor, menuPrincipalFragment);		
		ft.commit();

		//sonido de los botones
		//configureSound();


		db = new DBCrazyRace(this, DBCrazyRace.TipoDB.JUGAR);



		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int densityDpi = (int)(metrics.density * 160f);
		Log.v("DENSIDAD",densityDpi+"");
		Log.v("xdpi",metrics.xdpi+"");
		Log.v("ydpi",metrics.ydpi+"");
		Log.v("heightPixels",metrics.heightPixels+"");
		Log.v("widthPixels",metrics.widthPixels+"");
		Log.v("scaledDensity",metrics.scaledDensity+"");
		Log.v("density",metrics.density+"");



	}

	private void configureSound(){
		if(mediaPlayer==null){
			mediaPlayer = MediaPlayer.create(this, R.raw.boton);
		}
		if(mediaPlayer2==null){
			mediaPlayer2 = MediaPlayer.create(this, R.raw.fondo_menu);
			mediaPlayer2.setLooping(true);
			mediaPlayer2.setVolume(volumen, volumen);
			mediaPlayer2.start();
		}
	}

	private void stopSound(){
		if(mediaPlayer!=null){
			mediaPlayer.release();
			mediaPlayer = null;
		}
		if(mediaPlayer2!=null){
			mediaPlayer2.release();
			mediaPlayer2=null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences prefs = getSharedPreferences("crazyRacePrefs",Context.MODE_PRIVATE);
		currentLang = prefs.getString("language", Constants.DEFAULT_LANG);
		volumen = prefs.getFloat(Constants.VOLUME, Constants.DEFAULT_VOLUME);
		playerName = prefs.getString(Constants.PLAYER_NAME , getResources().getString(R.string.defaulPlayerName));
		level = prefs.getInt("level", Constants.DEFAULT_LEVEL);
		stage = prefs.getInt(Constants.STAGE,Constants.DEFAULT_LASTSTAGE);
		Log.v("params" ,"menuact onres: " + stage);
		configureSound();
	}
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences prefs = getSharedPreferences("crazyRacePrefs",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("language", currentLang);
		editor.putFloat(Constants.VOLUME, volumen);
		editor.putString(Constants.PLAYER_NAME, playerName);
		editor.putInt("level", level);
		editor.commit();
		stopSound();
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

	private void sound(){
		if(mediaPlayer != null){
			mediaPlayer.setVolume(volumen, volumen);
			mediaPlayer.start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
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

	

	/**
	 * New game
	 */
	@SuppressLint("ResourceType")
	public void nueva(View button){
		sound();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_transition_left_in,R.anim.fragment_transition_left_out,
                R.anim.fragment_transition_left_in,R.anim.fragment_transition_left_out);
		ft.replace(R.id.contenedor, menuNuevaPartidaFragment);	
		ft.addToBackStack(null);
		ft.commit();
	}

	/**
	 *  Join
	 */
	@SuppressLint("ResourceType")
	public void unirse(View button){
		sound();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_transition_left_in,R.anim.fragment_transition_left_out,
                R.anim.fragment_transition_left_in,R.anim.fragment_transition_left_out);
		ft.replace(R.id.contenedor, menuRankingFragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	/**
	 * Options
	 */
	@SuppressLint("ResourceType")
	public void opciones(View button){
		sound();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_transition_left_in,R.anim.fragment_transition_left_out,
                R.anim.fragment_transition_left_in,R.anim.fragment_transition_left_out);
		ft.replace(R.id.contenedor, menuOpcionesFragment);	
		ft.addToBackStack(null);
		ft.commit();
		
	}

	/**
	 * Back
	 */
	public void atras(View button){
		sound();
		getFragmentManager().popBackStackImmediate();
	}

	/**
	 * change language to English
	 */
	public void langEnglish(View button){
		sound();
		//change lang
		currentLang = Constants.LANG_EN;
		setLanguage();
		//force fragment refresh
		refreshFragment();
	}

	/**
	 * change language to Spanish
	 */
	public void langSpanish(View button){
		sound();
		//change lang
		currentLang = Constants.LANG_ES;
		setLanguage();
		//force fragment refresh
		refreshFragment();
	}

	//Refresh option fragment after change language to show new descriptions
	private void refreshFragment(){
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if (Build.VERSION.SDK_INT >= 26) {
			ft.setReorderingAllowed(false);
		}
		ft.detach(menuOpcionesFragment);
		ft.attach(menuOpcionesFragment);	
		ft.commit();
	}

	//change language
	private void setLanguage(){
		Locale myLocale = new Locale(currentLang);
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
	}

	/**
	 * set volume
	 */
	@Override
	public void onVolumeChange(float volumen) {
		this.volumen = volumen;
		if(mediaPlayer2 != null) {
			mediaPlayer2.setVolume(volumen, volumen);
		}
	}

	/**
	 * set rebotable name
	 */
	@Override
	public void onPlayerNameChange(String playerName) {
		this.playerName = playerName;		
	}

	
	/**
	 * Set level of the new game
	 */
	@Override
	public void onLevelChange(int level) {
		this.level = level;
	}
	
	/**
	 * set stage OF THE NEW GAME
	 */
	@Override
	public void onStageChange(int stage) {
		this.stage=stage;
	}
	
	/**
	 * returns the next stage with no score
	 */
	@Override
	public int getNextStage(){
		return lastStage + 1;
	} // ojo esto no va
	
	/**
	 * start game
	 * @param button
	 */
	public void startGame(View button){
		SeekBar level = (SeekBar) findViewById(R.id.seekBarLevel);
		int lvl = level.getProgress();
		TextView stage = (TextView) findViewById(R.id.stageField) ;
		int stg = Integer.parseInt(stage.getText().toString());

		Bundle data = new Bundle();
		data.putFloat(Constants.VOLUME, volumen);
		data.putString(Constants.PLAYER_NAME, playerName);
		data.putInt(Constants.LEVEL,lvl);
		data.putInt(Constants.STAGE,stg);

		Intent juego = new Intent(MenuActivity.this, MainActivity.class);
		juego.putExtras(data);
		startActivity(juego);
	}

	private TextView createTextView(String text){
		TextView t = new TextView(this);
		t.setText(text);
		t.setTextAppearance(this,R.style.RankingFontHeader);
		t.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams p = new  LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 32);
		t.setLayoutParams(p);
		return t;
	}
	private LinearLayout createRankingEntity(Ranking r){
		LinearLayout container = new LinearLayout(this);
		container.addView(createTextView("" + r.getStage()));
		container.addView(createTextView("" + r.getCoins()));
		container.addView(createTextView("" + r.getTimeStr()));
		return container;
	}

	private LinearLayout createEmptyRankingEntity(int stage){
		LinearLayout container = new LinearLayout(this);
		container.addView(createTextView("" + stage));
		container.addView(createTextView("-" ));
		container.addView(createTextView("--:--"));
		return container;
	}

	@Override
	public void loadRanking(int level){
		LinearLayout l = (LinearLayout) findViewById(R.id.scrollPartidas);
		int lastLevel = db.getMaxStage();
		Ranking[] ranking = db.getRanking(level);
		int lastStageWrited = 0;
		if(ranking != null && ranking.length >0) {
			for (Ranking r : ranking) {
				while(lastStageWrited < r.getStage() - 1){
					lastStageWrited++;
					l.addView(createEmptyRankingEntity(lastStageWrited));
				}
				l.addView(createRankingEntity(r));
				lastStageWrited = r.getStage();
			}
		}
		while(lastStageWrited < lastLevel){
			lastStageWrited++;
			l.addView(createEmptyRankingEntity(lastStageWrited));
		}
	}

	private void changeStyle(TextView tv, int color){
		tv.setTextColor(color);
	}

	public void onChangeRanking(View textView){
		LinearLayout container = (LinearLayout) findViewById(R.id.scrollPartidas);
		container.removeAllViews();
		TextView easy = (TextView) findViewById(R.id.easy);
		TextView medium = (TextView) findViewById(R.id.medium);
		TextView hard = (TextView) findViewById(R.id.hard);
		changeStyle(easy, Color.WHITE);
		changeStyle(medium, Color.WHITE);
		changeStyle(hard, Color.WHITE);
		int id = textView.getId();
		switch (id){
			case R.id.easy:
				changeStyle(easy, Color.RED);
				loadRanking(0);
				break;
			case R.id.medium:
				changeStyle(medium, Color.RED);
				loadRanking(1);
				break;
			case R.id.hard:
				changeStyle(hard, Color.RED);
				loadRanking(2);
				break;
			default:
				loadRanking(0);
		}
	}


	public void nextStage(View button){
		TextView stage = (TextView) findViewById(R.id.stageField) ;
		int stg = Integer.parseInt(stage.getText().toString());
		stg++;
		stage.setText(stg+"");
	}
	public void prevStage(View button){
		TextView stage = (TextView) findViewById(R.id.stageField) ;
		int stg = Integer.parseInt(stage.getText().toString());
		if(stg == 0) return;
		stg--;
		stage.setText(stg+"");
	}
}
