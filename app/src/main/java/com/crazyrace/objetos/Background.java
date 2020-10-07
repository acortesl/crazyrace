package com.crazyrace.objetos;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;

import com.crazyrace.R;
import com.crazyrace.util.Vector;

public class Background {

	private Context c;
	private int stage;


	private Bitmap[] tiles;
	private int tileWidth;
	private int tileHeight;

	private int screenWidth;
	private int screenHeight;
	private float raceLength;

	private int columns;
	private int rows;
	private boolean isSetup;
	private Random random;
	private ArrayList<ArrayList<BackgroundTile>> background;
	private ArrayList<BackgroundTile> aux;

	private Planet planet;
	private int[] planetBmpId = {
			R.drawable.planet_1_0,
			R.drawable.planet_2_0,
			R.drawable.planet_3_0
	};

	Paint paintLine = new Paint();
	private static class Planet{
		private Bitmap img;
		private Vector pos;
		Planet(Bitmap b, Vector v){
			img = b;
			pos = v;
		}
	}

	private static class BackgroundTile{
		private int x0;
		private int x1;
		private int y0;
		private int y1;
		private int indexTile;
		public static int id;
		private int myId;

		private synchronized int getId(){
			return ++id;
		}
		public BackgroundTile(int x0, int x1, int y0, int y1, int indexTile){
			this.x0 = x0;
			this.x1 = x1;
			this.y0 = y0;
			this.y1 = y1;
			this.indexTile = indexTile;
			this.myId = getId();
		}

		public int getX0() {
			return x0;
		}

		public void setX0(int x0) {
			this.x0 = x0;
		}

		public int getX1() {
			return x1;
		}

		public void setX1(int x1) {
			this.x1 = x1;
		}

		public int getY0() {
			return y0;
		}

		public void setY0(int y0) {
			this.y0 = y0;
		}

		public int getY1() {
			return y1;
		}

		public void setY1(int y1) {
			this.y1 = y1;
		}

		public int getIndexTile() {
			return indexTile;
		}

		public void setIndexTile(int indexTile) {
			this.indexTile = indexTile;
		}
		public int getMyId() {
			return myId;
		}
		public String toString(){
			return "x0: " + x0 +
					"x1: " + x1 +
					"y0: " + y0 + 
					"y1: " + y1 +
					"index: " + indexTile;
		}

	}



	public Background(Context c, int stage){
		this.c = c;
		this.stage = stage;
		Bitmap[] fondos = {
				BitmapFactory.decodeResource(c.getResources(), R.drawable.background),
				BitmapFactory.decodeResource(c.getResources(), R.drawable.background2),
				BitmapFactory.decodeResource(c.getResources(), R.drawable.background3),
				BitmapFactory.decodeResource(c.getResources(), R.drawable.background4)
		};
		this.tiles = fondos;
		this.tileHeight = tiles[0].getHeight();
		this.tileWidth = tiles[0].getWidth();
		this.isSetup = false;
		this.random = new Random(stage);
	}



	public void setScreenSize(int width, int height,float raceLength){
		this.raceLength = raceLength;
		this.screenWidth = width;
		this.screenHeight = height;
		setup();
	}

	private int getRandom(int length){
		if(random != null) return random.nextInt(length);
		return 0;
	}
	
	private int calcNTiles(int screen, int tileSize) {
		int nTiles = screen / tileSize;
		int rem = screen % tileSize;
		if(rem > 0) nTiles++;
		return nTiles;
	}

	private void setup(){
		//Ramdom se redefine porque se llama aqui en cada cambio de tamaño de pantalla
		//sino cuando se oculta la barra de navegacion se genera un fondo totalmente diferente
		random = new Random(stage);
		columns = calcNTiles(screenWidth,tiles[0].getWidth()) +1;
		rows = calcNTiles(screenHeight, tiles[0].getHeight());

		background = new ArrayList<ArrayList<BackgroundTile>>();

		for(int c = 0, x = 0; c < columns; c++, x+=tileWidth){
		//	Log.v("BACKGROUND", "NEW COLUMN: " + c);
			ArrayList<BackgroundTile> column = new ArrayList<BackgroundTile>();
			for(int r = 0, y= 0; r < rows; r++,y+=tileHeight){
				BackgroundTile tile = new BackgroundTile(x,(x+tileWidth),y,(y+tileHeight),getRandom(tiles.length));
				column.add(tile);
				//Log.v("BACKGROUND", "NEW ROW: " + r);
				//Log.v("BACKGROUND", "tile: " + tile.toString());
	
			}
			background.add(column);
		}
//		Log.v("BACKGROUND", "CREATED");

		int selectedPlanet = getRandom(planetBmpId.length);
		int planetSize = getRandom(250) + 50;
		Bitmap planetBmp = BitmapFactory.decodeResource(c.getResources(), planetBmpId[selectedPlanet]);
		planetBmp =  Bitmap.createScaledBitmap(planetBmp, planetSize, planetSize, true);
		planet = new Planet(planetBmp,Vector.getRandomVector((int)raceLength,screenHeight,random));

		this.isSetup = true;
	}


	private BackgroundTile firstTile;
	private BackgroundTile lastTile;
	private void update(double offset){
		firstTile = background.get(0).get(0);
		if(firstTile.getX1() - offset < 0){ //x inicial de la primera columna < 0 (fuera de la pantalla)
			aux = background.remove(0);
		}
		lastTile = background.get(background.size()-1).get(0);
		if(lastTile.getX0() - offset < screenWidth){ // si es la ultima fila
			if(aux != null){
				//reutilizo la fila eliminada
				for(int i = 0; i< aux.size(); i++){
					aux.get(i).setX0(lastTile.getX1());
					aux.get(i).setX1(lastTile.getX1() + tileWidth);
					aux.get(i).setIndexTile(getRandom(tiles.length));
				}
			}else{
				//Creo nueva fila. Solo entro la primer vez, despues reutilizo
				aux = new ArrayList<>();
				BackgroundTile tile;
				for(int i=0,y=0; i<rows;i++,y+=tileHeight){
					tile = new BackgroundTile(lastTile.getX1(),lastTile.getX1() + tileWidth,
							y,y+tileHeight,getRandom(tiles.length));
					aux.add(tile);
				}
			}
			background.add(background.size(), aux);
			aux = null;
		}
	}

	public void draw(Canvas canvas,double offset){		
		if(isSetup){
			update(offset);
			int x0,y0;
			int indexTile;
			
			
			for(int c = 0; c < background.size(); c++){
				ArrayList<BackgroundTile> column = background.get(c);
				for(int r = 0; r < background.get(c).size(); r++){
					BackgroundTile row = column.get(r);
					x0=row.getX0();
					y0=row.getY0();
					indexTile=row.getIndexTile();

					canvas.drawBitmap(tiles[indexTile], (float)(x0-offset), y0, null);

	/*				paintLine.setColor(Color.GREEN);
					paintLine.setTextSize(20);
					canvas.drawText(row.getMyId()+"", (float)(x0-offset), y0+25, paintLine);
					canvas.drawLine((float) (x0-offset), y0, (float) (x0-offset), y1, paintLine);				
					paintLine.setColor(Color.CYAN);
					canvas.drawLine((float) (x0-offset), y0, (float) (x1-offset), y0, paintLine);
*/
					
				}
			}

			canvas.drawBitmap(planet.img,(int)(planet.pos.getX()-offset),(int)planet.pos.getY(),null);
		}
	}
}
