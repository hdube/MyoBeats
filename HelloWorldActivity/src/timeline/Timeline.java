package timeline;

import myo.beats.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;


public class Timeline implements Runnable {
	
	private boolean loaded = false;
	private Record record;
	private Context context;
	
	public Timeline(Context context) {
		Log.e("Hi", "Timeline constructed");
		this.record = new Record();
		this.context = context;
	}
	
	@Override
	public void run() {
		read();
	}
	
	public void read() {
		// Load the sound
		//final boolean loaded = false;
		Log.e("Hi", "In read()");
		SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		/*soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true;
			}
		});*/
		int beat1ID = soundPool.load(this.context, R.raw.beat1, 1);
		int beat2ID = soundPool.load(this.context, R.raw.beat1, 1);
		//int beat1ID = soundPool.load("R.raw.beat1", 1);
		//int beat2ID = soundPool.load("R.raw.beat1", 1);
		
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
		long endTime;

		long barreTime = 2000000000l; //2 secs
		long beatTime = barreTime/4;
		byte beatCount = 4; //4 beats per barre
		byte count=0;
		startTime = System.nanoTime();
		// 1 second = 1000000000l
		Log.e("Hi", "Before loop");
		
		//if (loaded)
		while(true) {
			endTime = System.nanoTime();
			Log.e("Hi", "Inside the loop");
			//Log.e("Hi", "" + currentDuration);
			// This if statement only produces beats
			if (endTime-startTime >= beatTime) {
				startTime += beatTime;
				count++;
				count%=beatCount;
				if (count == 0) { //barre complete
					soundPool.play(beat2ID, 0.4f, 0.4f, 1, 0, 1.0f);
					Log.e("Hi", "Barre complete");
				}
				else { //beat complete
					soundPool.play(beat1ID, 0.4f, 0.4f, 1, 0, 1.0f);
					Log.e("Hi", "Beat complete at time ");
				}
			}
			
			//This if statement checks for output sound.
			/*if (record.playNextSound(currentDuration)) {
				 //PLAY SOUND
				 record.soundPlayed();
			}*/
		}
	}

}
