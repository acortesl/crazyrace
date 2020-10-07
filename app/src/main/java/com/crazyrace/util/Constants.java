package com.crazyrace.util;

public class Constants {

	public static final int PUERTO = 9876;
	public static final String IP = "localhost";

	//default values
	public static final float DEFAULT_VOLUME = 0.5f;
	public final static String DEFAULT_LANG = "es";
	public final static String LANG_ES = "es";
	public final static String LANG_EN = "en";

	public static final int DEFAULT_STAGE = 1; // nivel por defecto
	public static final int DEFAULT_LEVEL = 1; // dificultad por defecto
	public static final int DEFAULT_LASTSTAGE = 0; // ultimo nivel superado por defecto


	public static final float STAGE_LENGTH = 5000f; //longitud de la partida
	public static final int STAGE_INCREMENT = 200; // incremento de longitud por nivel
	public static final int VIRTUAL_STAGE_HEIGH = 500; //altura de pantalla virtual

	public static final int SCREEN_MARGIN_X = 100; //margen para no generar objetos
	public static final int SCREEN_MARGIN_Y = 100;

	public static final int COINS_PER_STAGE = 10;//250
	public static final int OBS_PER_STAGE = 5;
	public static final int BOMBAS_PER_STAGE = 1;

	//constant for bundle params
	public static final String VOLUME = "volume";
	public static final String PLAYER_NAME = "playerName";
	public static final String STAGE = "stage";
	public static final String LEVEL = "level";

	//constants for random values
	public static final int K_OBSTACULOS = 3;
	public static final int K_MONEDAS = 5;
	public static final int K_BOMBAS = 7;
	public static final int K_AURAS = 9;

	//sonidos
	public static final String SOUND_MONEDA = "MONEDA";
	public static final String SOUND_ESCUDO = "ESCUDO";
	public static final String SOUND_BOMBA = "BOMBA";
	public static final String SOUND_REBOTE = "REBOTE";
	public static final String SOUND_GANAR = "GANAR";
	public static final String SOUND_PERDER = "PERDER";
}
