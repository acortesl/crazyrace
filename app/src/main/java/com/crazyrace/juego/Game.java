package com.crazyrace.juego;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.crazyrace.controles.ElevationControl;
import com.crazyrace.controles.SpeedControl;
import com.crazyrace.db.DBCrazyRace;
import com.crazyrace.objetos.Aura;
import com.crazyrace.objetos.Bomba;
import com.crazyrace.objetos.Crono;
import com.crazyrace.objetos.Player;
import com.crazyrace.objetos.Rebotable;
import com.crazyrace.objetos.Background;
import com.crazyrace.objetos.Coin;
import com.crazyrace.objetos.ProgressBar;
import com.crazyrace.objetos.EndLine;
import com.crazyrace.objetos.Roca;
import com.crazyrace.util.Constants;
import com.crazyrace.util.GeneradorObjetos;
import com.crazyrace.util.Vector;
import com.crazyrace.R;


public class Game extends TextureView implements TextureView.SurfaceTextureListener{
	private Context context;
    private Surface surface;
    private boolean isInicialized;
	//general data
	private float volumen;
	private String playerName;
	private int level;
	private int stage;

	private Player yo;

	//drawControles
	private Bitmap controlSubir;
	private Bitmap controlAvanzar;
	private Bitmap controlPressAvanzar;
	private Bitmap controlPressSubir;
	private ElevationControl ec;
	private SpeedControl sc;
	//fondo
	private Background background;

	//meta
    private int raceLength;
	private EndLine end;
	private ProgressBar progress;

	//hilo principal
	GameThread gameThread;

	//monedas y obstaculos
	private List<Roca> obstaculo = new ArrayList<Roca>();
	private List<Coin> coins = new ArrayList<Coin>();
	private int coinsCollected;
	private List<Bomba> bombas = new ArrayList<Bomba>();
	private List<Aura> auras = new ArrayList<Aura>();


	private List<Player> pelotas = new ArrayList<Player>();
	private float timestep = 0.00005f;
	private float timestepRebote =0.00005f;

	private double offset =0;
	private double maxOffset = 0;

	private int width;
	private int height;

	private Crono crono;

	private Bitmap pelotabmp;
	private Bitmap coinBmp;
    private Bitmap bombabmp;
    private Bitmap auraPlayerbmp;
	private Bitmap miniaurabmp;
	private Bitmap[] rocasBmp;

	private boolean finish = false;
	private boolean muertePorBomba = false;
	private boolean isSaved;

	private Paint painttxt;
	private Paint paintEndRect;
	private Paint paintEndText;

	private SharedPreferences prefs;

	private Activity activity;

	private boolean debugMode = false;

	private Map<String,MediaPlayer> mediaPlayer= new HashMap<>();

	private float density;

    public Game(Context context, String playerName, float volumen, int level, int stage, Activity activity) {
		super(context);
		Log.v("Game Created", "Game created");
        /**
         * Se guardan los valores necesarios para la partida
         * y se arranca el surfaceview
         *
         * Nota: no se pueden generar los objetos del juego porque
         * hasta que no se inicializa el surfaceview y se pasa por
         * surfacechange no se tienen las dimensiones de la superficie de juego
         */
        //general data
		this.context = context;
		this.playerName = playerName;
		this.volumen = volumen;
		this.stage = stage;
		this.level = level;
		this.activity = activity;

        this.isInicialized = false;


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        density = metrics.scaledDensity;


		pelotabmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.nave);
		auraPlayerbmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.aura);
        miniaurabmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.auramini);
        bombabmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomba);
		controlAvanzar = BitmapFactory.decodeResource(context.getResources(), R.drawable.boton1);
		controlSubir = BitmapFactory.decodeResource(context.getResources(), R.drawable.boton2);
		controlPressAvanzar = BitmapFactory.decodeResource(context.getResources(), R.drawable.boton1_2);
		controlPressSubir = BitmapFactory.decodeResource(context.getResources(), R.drawable.boton2_2);
        coinBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin);

		background = new Background(context, stage);
		Bitmap[] rocas = {
				BitmapFactory.decodeResource(context.getResources(), R.drawable.roca1),
				BitmapFactory.decodeResource(context.getResources(), R.drawable.roca2),
				BitmapFactory.decodeResource(context.getResources(), R.drawable.roca3),

		};
		rocasBmp = rocas;

		prefs = context.getSharedPreferences("crazyRacePrefs",Context.MODE_PRIVATE);

        crono = new Crono();

		painttxt = new Paint();
		paintEndRect = new Paint();
		paintEndText = new Paint();

		//sonido
		configSound();


		//start game thread
        setSurfaceTextureListener(this);
		gameThread = new GameThread(this, context, volumen);
		gameThread.setName("GameThread");

	}



	public void onPause(){
		onSurfaceTextureDestroyed(null);

	}

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
		Log.v("TEXTUREVIEW", "AVAILABLE");
        if (gameThread.getState()==Thread.State.TERMINATED) {
            //config el sonido solo cuando la actividad estaba pausada
            //cuando la aplicacion arranca de 0 se configura en el constructor
            configSound();
            gameThread = new GameThread(this, context, volumen);

        }
		this.width = width;
		this.height = height;
		initGame();
		//lo siguiente depende de h y w, se ejecuta en cada cambio para recalcular
		sc.initButton(width, height);
		ec.initButton(width, height);
		background.setScreenSize(width, height, raceLength);
		progress.setScreenSize(width, height);
		end.setScreenSize(width, height);
		maxOffset = end.getxPos() - width + 200 + yo.getRadio()*2;

        surface = new Surface(surfaceTexture);
        gameThread.setSurface(surface);
		gameThread.setRunning(true);
		gameThread.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
        Log.v("TEXTUREVIEW", "CHANGE");
        this.width = width;
        this.height = height;
        //lo siguiente depende de h y w, se ejecuta en cada cambio para recalcular
        sc.initButton(width, height);
        ec.initButton(width, height);
        background.setScreenSize(width, height, raceLength);
        progress.setScreenSize(width, height);
        end.setScreenSize(width, height);
        maxOffset = end.getxPos() - width + 200 + yo.getRadio()*2;
        if(!gameThread.isRunning()) {
			gameThread.setRunning(true);
			gameThread.start();
		}
    }


    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        Log.v("TEXTUREVIEW", "DESTROY");
        try{
            stopAudio();
            gameThread.setRunning(false);
            //surface.release();
            surface = null;
            gameThread.join();
        }catch(InterruptedException e){}
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    private void initGame(){
        if(isInicialized){
            return;
        }
        isInicialized = true;

        //pelotas
        Player p = new Player(pelotabmp, 75,75, density, timestep, 1); //x&y en dp!
        p.setEscudoBmp(auraPlayerbmp);
        pelotas.add(p);
        yo = p;

        //drawControles
        int w = Math.round(controlAvanzar.getWidth()/1.2f);
        int h = Math.round(controlAvanzar.getHeight()/1.2f);
        controlAvanzar = Bitmap.createScaledBitmap(controlAvanzar, w, h, true);
        controlPressAvanzar = Bitmap.createScaledBitmap(controlPressAvanzar, w, h, true);
        controlSubir = Bitmap.createScaledBitmap(controlSubir, w, h, true);
        controlPressSubir = Bitmap.createScaledBitmap(controlPressSubir, w, h, true);
        ec = new ElevationControl(p, controlSubir, controlPressSubir);
        sc= new SpeedControl(p, controlAvanzar, controlPressAvanzar);

        //meta y progress bar

        raceLength = GeneradorObjetos.calcStageLenght(stage, density);
        Bitmap meta = BitmapFactory.decodeResource(context.getResources(), R.drawable.meta);
        end = new EndLine(raceLength,new BitmapDrawable(context.getResources(),meta));
        progress = new ProgressBar(raceLength);


        //obstaculos
        obstaculo = GeneradorObjetos.generateObstacle(rocasBmp, stage, level, raceLength, height, density);

        //coins
        coins = GeneradorObjetos.generateCoins(coinBmp,stage,raceLength,height);
        coinsCollected = 0;

        //bombas
        bombas = GeneradorObjetos.generateBombas(bombabmp,stage,level,raceLength,height);
        //auras
        auras = GeneradorObjetos.generateAuras(miniaurabmp,stage,level,raceLength,height);

        //cronometro
        crono.start();

        //guardando datos de la partida
        isSaved = false;

        setupPaintText();

    }


	public void configSound(){
		mediaPlayer.put(Constants.SOUND_MONEDA,configSound(R.raw.moneda, false));
		mediaPlayer.put(Constants.SOUND_ESCUDO,configSound(R.raw.escudo, true));
		mediaPlayer.put(Constants.SOUND_BOMBA,configSound(R.raw.bomba, false));
		mediaPlayer.put(Constants.SOUND_REBOTE,configSound(R.raw.rebote, false));
		mediaPlayer.put(Constants.SOUND_GANAR,configSound(R.raw.ganar, false));
		mediaPlayer.put(Constants.SOUND_PERDER,configSound(R.raw.perder, false));
	}

	private MediaPlayer configSound(int id, boolean loop){
		MediaPlayer m = MediaPlayer.create(context, id);
		m.setVolume(volumen, volumen);
		m.setLooping(loop);
		return m;
	}

	private void setupPaintText(){
		this.painttxt.setTextSize(height*0.05f);
		this.painttxt.setColor(Color.YELLOW);
		this.painttxt.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
	}

	private void drawControles(Canvas canvas){
		ec.draw(canvas);
		sc.draw(canvas);
	}


	private void drawMarcadores(Canvas canvas){

		canvas.drawText(String.format("%1$s : %2$s",playerName, coinsCollected ),width/100*3, (height/100*7), painttxt);
		canvas.drawText(crono.getCrono(),width-(width/100*20), height/100*7, painttxt);
    //    canvas.drawText( String.format("FPS:  %1$d", this.fps ),300,300, painttxt);
	}

	private void drawFin(Canvas c){
		float xVal = width/4;
		float yVal = height/4;

		paintEndRect.setColor(Color.DKGRAY);
		c.drawRect(xVal,yVal,3*xVal,3*yVal,paintEndRect);
		paintEndRect.setColor(Color.GRAY);
		c.drawRect(xVal-15,yVal-15,3*xVal-15,3*yVal-15,paintEndRect);

		paintEndText.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
		paintEndText.setColor(Color.YELLOW);

		paintEndText.setTextSize(height*0.1f);
		paintEndText.setShadowLayer(5.0f, 10.0f, 10.0f, Color.RED);
		paintEndText.setStrokeWidth(25);
		if(muertePorBomba){
			c.drawText(getResources().getString(R.string.pierdes),xVal+getResources().getDimension(R.dimen.result_title_margin_x), yVal+getResources().getDimension(R.dimen.result_title_margin_y), paintEndText);
		}else{
			c.drawText(getResources().getString(R.string.ganas),xVal+getResources().getDimension(R.dimen.result_title_margin_x), yVal+getResources().getDimension(R.dimen.result_title_margin_y), paintEndText);
		}

		paintEndText.setTextSize(height*0.055f);
		paintEndText.setShadowLayer(0,0,0,0);
		c.drawText(getResources().getString(R.string.monedas),xVal+getResources().getDimension(R.dimen.result_text_margin_x), yVal+getResources().getDimension(R.dimen.result_coins_margin_y), paintEndText);
		c.drawText("" + coinsCollected,xVal+getResources().getDimension(R.dimen.result_coins_margin_x), yVal+getResources().getDimension(R.dimen.result_coins_margin_y), paintEndText);
		c.drawText(getResources().getString(R.string.tiempo),xVal+getResources().getDimension(R.dimen.result_text_margin_x), yVal+getResources().getDimension(R.dimen.result_time_margin_y), paintEndText);
		c.drawText( crono.getCrono(),xVal+getResources().getDimension(R.dimen.result_time_margin_x), yVal+getResources().getDimension(R.dimen.result_time_margin_y), paintEndText);
	}

	public void dibujar(Canvas canvas) {
    	//canvas.drawColor(Color.BLACK);
		background.draw(canvas,offset);
		end.draw(canvas,(float)offset);
		for(int i=0; i < pelotas.size(); i++){
			pelotas.get(i).draw(canvas, (float) offset, debugMode);
		}
		for(int i=0; i < obstaculo.size(); i++){
			if(obstaculo.get(i).getPosicion().getX() - offset + obstaculo.get(i).getRadio()> 0 && obstaculo.get(i).getPosicion().getX() - offset -obstaculo.get(i).getRadio()< width ){
				obstaculo.get(i).draw(canvas,(float)offset, debugMode);
			}
		}
		for(int i=0; i < coins.size(); i++){
			if(coins.get(i).getPosicion().getX() - offset + coins.get(i).getRadio()> 0 && coins.get(i).getPosicion().getX() - offset< width ) {
				coins.get(i).draw(canvas, offset);
			}
		}
		for(int i=0; i < bombas.size(); i++){
			if(bombas.get(i).getPosicion().getX() - offset + bombas.get(i).getRadio()> 0 && bombas.get(i).getPosicion().getX() - offset< width ) {
				bombas.get(i).draw(canvas,offset);
			}
		}
		for(int i=0; i < auras.size(); i++){
			if(auras.get(i).getPosicion().getX() - offset + auras.get(i).getRadio()> 0 && auras.get(i).getPosicion().getX() - offset< width ) {
				auras.get(i).draw(canvas, offset);
			}
		}
		progress.draw(canvas);
		drawMarcadores(canvas);
		drawControles(canvas);
		if(finish){
            drawFin(canvas);
        }
	}


	public void update(){
	    if(!finish) {
            for (int j=0; j <pelotas.size(); j++) {
				pelotas.get(j).moverPelota(timestep);
                if (pelotas.get(j) == yo) {
                    actualizarOffset(yo);
                    progress.setCurrentPos(yo.getPosicion().getX());
                    if(end.isTheEnd(yo.getPosicion().getX() - yo.getRadio()*2)){
                        finish = true;
                        crono.stop();
						this.mediaPlayer.get(Constants.SOUND_GANAR).start();
                        if(!isSaved){
                        	isSaved = true;
                        	saveData(stage);
							saveRanking(stage,level,crono,coinsCollected);
						}

                    }
					for (int i=0; i <coins.size(); i++) {
						if (coins.get(i).collect(pelotas.get(j).getBounds())) {
							coins.get(i).setVisible(false);
							this.mediaPlayer.get(Constants.SOUND_MONEDA).start();
							coinsCollected++;
						}
					}
					for(int i=0; i <auras.size(); i++){
						if (auras.get(i).collect(pelotas.get(j).getBounds())) {
							auras.get(i).setVisible(false);
							yo.setHasEscudo(true);
							this.mediaPlayer.get(Constants.SOUND_ESCUDO).start();
						}
					}
					for (int i=0; i <bombas.size(); i++) {
						if (bombas.get(i).collect(pelotas.get(j).getBounds())) {
							bombas.get(i).setVisible(false);
							if(yo.hasEscudo()){
								yo.setHasEscudo(false);
								this.mediaPlayer.get(Constants.SOUND_ESCUDO).pause();
							}else {
								this.mediaPlayer.get(Constants.SOUND_BOMBA).start();
								finish = true;
								muertePorBomba = true;
								crono.stop();
								this.mediaPlayer.get(Constants.SOUND_PERDER).start();
							}
						}
					}
                }
                rebotarPantalla(pelotas.get(j));
                rebotarPelotasFijas(pelotas.get(j));
            }
        }
	}

	private void actualizarOffset(Rebotable yo){
		double screenPos = yo.getPosicion().getX()-offset;
		if(screenPos > this.width /2){
			offset++;
		}
		if(offset > maxOffset){
			offset = maxOffset;
		}
	}

	ArrayList<Vector> rebotes = new ArrayList<Vector>();
	private void rebotarPantalla(Rebotable p){
		Vector pos = p.getPosicion();


		rebotes.clear();
		if(pos.getX()-offset-yo.getRadio() < 0){
			Vector plano =new Vector(5,0);
			plano.unitario();
			rebotes.add(p.rebotar(plano));
		}
		/*if(pos.getX() + rad > this.getWidth()){
			Vector plano =new Vector(-5,0);
			plano.unitario();
			rebotes.add(p.rebotar(plano));
		}		*/
		if(pos.getY() - p.getRadio() < 0 ){
			Vector plano =new Vector(0,5);
			plano.unitario();
			rebotes.add(p.rebotar(plano));
		}
		if(pos.getY() + p.getRadio() > this.getHeight()){
			Vector plano =new Vector(0,-5);
			plano.unitario();
			rebotes.add(p.rebotar(plano));
		}
		if(rebotes.size() > 0){
			Vector newPos = Vector.sumarVectores(rebotes);
			p.actualizarPosicion(newPos);
			this.mediaPlayer.get(Constants.SOUND_REBOTE).start();
		}
	}

	private void rebotarPelotasFijas(Rebotable p){

		for(int i = 0; i < obstaculo.size(); i++){
			if(p!=obstaculo.get(i)){
				double d = Vector.distancia(p.getPosicion(), obstaculo.get(i).getPosicion());
				if(d<p.getRadio()+obstaculo.get(i).getRadio()){
					obstaculo.get(i).setEstaRebotando(true);
					this.mediaPlayer.get(Constants.SOUND_REBOTE).start();
					Vector n = new Vector(p.getPosicion().getX()-obstaculo.get(i).getPosicion().getX(),p.getPosicion().getY()-obstaculo.get(i).getPosicion().getY());
					n.unitario();
					p.actualizarPosicion(p.rebotar(n));
					int j = 0;
					while(Vector.distancia(p.getPosicion(), obstaculo.get(i).getPosicion())< p.getRadio()+obstaculo.get(i).getRadio() && j<5){
						j++;
						p.moverPelota(timestepRebote);
					}
					obstaculo.get(i).setEstaRebotando(false);
					for(int k = 0; k > pelotas.size(); k++)
						rebotarPantalla(pelotas.get(k));
				}
			}
		}
	}




	private void saveData(int stage){
		int s = prefs.getInt(Constants.STAGE,-1);
		if(stage > s){
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt(Constants.STAGE,stage);
			editor.commit();
		}
	}

	private void saveRanking(int stage,int level,Crono crono,int coinsCollected){
		DBCrazyRace db = new DBCrazyRace(context, DBCrazyRace.TipoDB.JUGAR);
		db.updateRanking(stage,level,coinsCollected,crono.getTime(),crono.getCrono());
	}

	public void volverAlMenu(){
		stopAudio();
    	activity.finish();
	}



	@Override
	public boolean performClick() {
		super.performClick();
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e){
		int action = e.getActionMasked();
		int index = e.getActionIndex();
		if(!finish) {
			boolean invalidate = false;
			if (ec.actuate(action, e.getX(index), e.getY(index))) {
				performClick();
				invalidate = true;

			}
			if (sc.actuate(action, e.getX(index), e.getY(index))) {
				performClick();
				invalidate = true;

			}
			if(progress.isPressed(e.getX(index), e.getY(index))){
				this.debugMode = !this.debugMode;
			}
			if (invalidate) {
				invalidate();
				return true;
			}
			return false;
		}else{
			if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN){
				volverAlMenu();
//				invalidate();
				return true;
			}
			return false;
		}

	}

	public void stopAudio(){
    	Set<String> keys = this.mediaPlayer.keySet();
    	for(String key:keys){
    		MediaPlayer m = this.mediaPlayer.get(key);
    		if(m!=null){
    			m.release();
			}
		}
	}

	int fps= 0;
    public void setFps(int fps){
        this.fps = fps;
    }

}
