package com.crazyrace.menu;


import com.crazyrace.R;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


public class MenuRankingFragment extends Fragment {

	private MenuRankingFragmentInterface rankingMenu;

	public interface MenuRankingFragmentInterface {
		void loadRanking(int level);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_ranking_fragment, container, false);
		return view;
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.v("UNIRSE", "activitycreated");

		TextView easy = (TextView) getActivity().findViewById(R.id.easy);
		easy.setTextColor(Color.RED);
		rankingMenu.loadRanking(0);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try { //cada vez que creamos instancias del framento comprobamos que implementa OnVolumeChange
			rankingMenu = (MenuRankingFragmentInterface) activity;
		} catch (ClassCastException e) {}
	}

}
