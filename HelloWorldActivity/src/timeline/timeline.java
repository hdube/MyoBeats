package timeline;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
//import android.app.Activity;


public class timeline implements Runnable {
	private boolean loaded = false;
	
	@Override
	public void run() {
		read();
	}
	
	public void read() {
		// Load the sound
		//final boolean loaded = false;
		SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		/*soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true;
			}
		});*/
		int beat1ID = soundPool.load("R/raw/beat1", 1);
		int beat2ID = soundPool.load("R/raw/beat1", 1);
		
		// Set volume
			//AUDIO_SERVICE is the Context's variable
		/*
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;
		*/
		
		// 160 bpm in 4/4 -> 60 secs/160 beats = 3 secs/8 beats  = 1.5 secs/4 beats = 1.5 secs/barre
		// 100 bpm in 4/4 -> 60 secs/100 beats = 6 secs/10 beats = 2.4 secs/4 beats = 2.4 secs/barre
		long startTime;
		long currentDuration;

		long barreTime = 1500000000l; //1.5 secs
		long beatTime = (long)(barreTime/4);
		byte beatCount = 4; //4 beats per barre
		byte count=0;
		startTime = System.nanoTime();
		// 1 second = 1000000000l
		
		if (loaded)
		while(true) {
			currentDuration = startTime-System.nanoTime();
			if (currentDuration >= beatTime) {
				startTime%=beatTime;
				count++;
				count%=beatCount;
				if (count == 0) { //barre complete
					soundPool.play(beat2ID, 0.4f, 0.4f, 1, 0, 1.0f);
					System.out.println("Barre complete");
				}
				else { //beat complete
					soundPool.play(beat1ID, 0.4f, 0.4f, 1, 0, 1.0f);
					System.out.println("Beat complete");
				}
				//TODO: have new beat sound come out.
			}
			
			//TODO: read input from Oleg's class. I have to do that.
		}
	}

}
