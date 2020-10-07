package com.crazyrace.juego;

import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

import com.crazyrace.juego.Game;
import com.crazyrace.R;


public class GameThread extends Thread /*implements SensorEventListener*/{
	
	private final static int NANOSEC = 1000000000;


	
	private Game game;
	boolean running = false;
	private float volumen;
	MediaPlayer mediaPlayer;
	private Surface surface;


	public GameThread(Game g, Context c, float volume){
		this.game=g;
		this.volumen= volume;
//		mediaPlayer = MediaPlayer.create(c, R.raw.crazy_race);
		mediaPlayer = MediaPlayer.create(c, R.raw.fondo_juego);
		mediaPlayer.setLooping(true);

	}

	public void setRunning(boolean r){

		Log.v("TEXTUREVIEW", "Thread running..." + r);
		running = r;
	}

	public boolean isRunning() {
		return running;
	}

	public void setSurface(Surface surface){
		this.surface = surface;
	}


    private int frameCount = 0;
    @Override
    public void run() {

        mediaPlayer.setVolume(volumen,volumen);
        mediaPlayer.start();

        Canvas c = null;




        /**
         * Frecuencia de actualizaciones
         * f = 1 / t --> t = 1 / f => 1 / 30 = 0.03333 segundos
         */
        final double UPDATE_HZ = 60.0;
        /**
         * Tiempo en ns para cada actualizacion
         */
        final double TIME_NS_PER_UPDATE = NANOSEC / UPDATE_HZ;
        /**
         * Numero maximo de actualizaciones antes de pintar
         */
        final int MAX_UPDATES = 5;
        /**
         * Frecuencia de dibujo
         */
        final double FPS = 60;
        /**
         * Tiempo en ns para cada dibujo
         */
        final double TIME_NS_PER_RENDER = NANOSEC / FPS;
        /**
         * Ultima actualizacion
         */
        double lastUpdate = System.nanoTime();
        /**
         * Ultimo dibujo
         */
        double lastRender;
        /**
         * Ultimo segundo y segundo actual
         */
        int lastSecond = (int) (lastUpdate / NANOSEC);
        int currentSecond;
        /**
         * ahora
         */
        double now;
        /**
         * contador de actualizaciones
         */
        int updateCount;

         /**
         *
         *
         * bucle
         *
         *
         */
        while (running)	{
            try {

                //bloqueo el canvas en funcion de la version de android
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    c = surface.lockHardwareCanvas();
                } else {
                    c = surface.lockCanvas(null);
                }

                // por si hay error
                if (c == null) {
                    running = false;
                    break;
                }


                synchronized (c) {


                    //inicio hora actual y contador de actualizaciones
                    now = System.nanoTime();
                    updateCount = 0;

                    //Actualizo
                    while( now - lastUpdate > TIME_NS_PER_UPDATE && updateCount < MAX_UPDATES)	{
                        for(int n = 1; n<40;n++){
                            this.game.update();
                        }
                        lastUpdate += TIME_NS_PER_UPDATE;
                        updateCount++;
                    }

                    if ( now - lastUpdate > TIME_NS_PER_UPDATE){
                        lastUpdate = now - TIME_NS_PER_UPDATE;
                    }

                    //pinto
                    this.game.dibujar(c);
                    frameCount++;
                    lastRender = now;

                    //actualizo contador de frames
                    currentSecond = (int) (lastUpdate / NANOSEC);
                    if (currentSecond > lastSecond){
                        game.setFps(frameCount);
                        lastSecond = currentSecond;
                        frameCount = 0;
                    }


                    /**
                     * Dormir
                     */
                    while ( now - lastRender < TIME_NS_PER_RENDER && now - lastUpdate < TIME_NS_PER_UPDATE)			{
                        Thread.yield();
						try {
                            Thread.sleep(1);} catch(Exception e) {}

                        now = System.nanoTime();
                    }




                }

            }finally {
                //desbloqueo canvas
                if(c!=null)
                    surface.unlockCanvasAndPost(c);


            }
        }
		mediaPlayer.stop();
		game.stopAudio();


    }



}
