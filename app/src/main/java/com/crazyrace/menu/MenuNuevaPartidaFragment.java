package com.crazyrace.menu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.crazyrace.R;
import com.crazyrace.util.Constants;

public class MenuNuevaPartidaFragment extends Fragment {

	public interface NewGameMenuInterface{
		public int laststage= 0;
		public void onLevelChange(int level);
		public void onStageChange(int stage);
		public int getNextStage();
	}
	
	private SeekBar seekLevel;
	private TextView stageField;
	private NewGameMenuInterface newGameMenu;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_nueva_partida_fragment, container, false);
		Log.v("ciclo","oncreate");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.v("ciclo","onactivitycreated");
		Bundle args = getArguments();
			
		//seekbar level
		seekLevel = (SeekBar) getView().findViewById(R.id.seekBarLevel);
		if(seekLevel!= null){
			seekLevel.setOnSeekBarChangeListener(new Level(seekLevel.getProgress()));
			int level = args.getInt("level");
			seekLevel.setProgress(level);			
		}	
		
		stageField = (TextView) getView().findViewById(R.id.stageField);
		if(stageField != null){
			//seteando valor stage por primera vez
			int nextStage = args.getInt(Constants.STAGE) + 1;
			Log.v("params" ,"menunuevafra onactcrea next(+1): " + nextStage);
			newGameMenu.onStageChange(nextStage);
			stageField.setText("" + nextStage);

		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v("ciclo","onattach");
		try { //cada vez que creamos instancias del framento comprobamos que implementa la interfaz
			newGameMenu = (NewGameMenuInterface) activity;
		} catch (ClassCastException e) {}
	}


	@Override
	public void onResume(){
		super.onResume();
		Log.v("ciclo","onresume");
		SharedPreferences prefs = this.getActivity().getSharedPreferences("crazyRacePrefs", Context.MODE_PRIVATE);
		int nextStage = prefs.getInt(Constants.STAGE,0);
		Log.v("params" ,"menunuevafra onres : " + nextStage);
		nextStage++;
		newGameMenu.onStageChange(nextStage);
		stageField.setText("" + nextStage);

	}
	//seekbar volumen
	private class Level implements OnSeekBarChangeListener{
		private TextView levelText;
		private Integer[] levelDesc = new Integer[]{
			R.string.levelEasy, 
			R.string.levelMedium, 
			R.string.levelHard
		};

		public Level(int progress){
			levelText = (TextView) getView().findViewById(R.id.level);
			setLevel(progress);
		}
		
		private void setLevel(int level){
			newGameMenu.onLevelChange(level);
			levelText.setText(levelDesc[level]);
		}
		
		@Override
		public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
			setLevel(progress);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekbar) {}

		@Override
		public void onStopTrackingTouch(SeekBar seekbar) {}	
	}
}
