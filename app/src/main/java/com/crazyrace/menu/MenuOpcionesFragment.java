package com.crazyrace.menu;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.crazyrace.R;
import com.crazyrace.util.Constants;

public class MenuOpcionesFragment extends Fragment{

	public interface OptionsMenuInterface{
		void onVolumeChange(float volumen);
		void onPlayerNameChange(String playerName);
	}
	
	private SeekBar seekVolume;
	
	private OptionsMenuInterface optionsMenu;
	
	private EditText nameField;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_opciones_fragment, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);	
		//seekbar volumen
		seekVolume = (SeekBar) getView().findViewById(R.id.seekBarVolumen);
		Bundle args = getArguments();
		if(seekVolume!= null){
			seekVolume.setOnSeekBarChangeListener(new Volumen(seekVolume.getProgress()));			
			float volumen = args.getFloat(Constants.VOLUME);
			seekVolume.setProgress((int)(volumen * 100));
		}	
		
		nameField = (EditText) getView().findViewById(R.id.username);
		if(nameField != null){
			nameField.setText(args.getString(Constants.PLAYER_NAME));
			nameField.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable s) {
					optionsMenu.onPlayerNameChange(s.toString());
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}

			});
		}
	}

	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    try { //cada vez que creamos instancias del framento comprobamos que implementa OnVolumeChange
	    	optionsMenu = (OptionsMenuInterface) activity;
	    } catch (ClassCastException e) {}
	}
	
	//seekbar volumen
	private class Volumen implements OnSeekBarChangeListener{
		private TextView volumenText;

		public Volumen(int progress){
			volumenText = (TextView) getView().findViewById(R.id.volumen);
			setVolume(progress);
		}
		
		private void setVolume(int volume){
			volumenText.setText(volume + "%");
			float v = (float)volume/100;
			optionsMenu.onVolumeChange(v);
		}
		
		@Override
		public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
			setVolume(progress);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekbar) {}

		@Override
		public void onStopTrackingTouch(SeekBar seekbar) {}	
	}
}
