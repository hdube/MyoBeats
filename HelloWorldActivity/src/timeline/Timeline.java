package timeline;

import myo.beats.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.AsyncTask;
import android.util.Log;


public class Timeline extends AsyncTask<Void, Void, String> {
	
	private Thread thread;
	
	//private boolean loaded = false;
	private Record record;
	private Context context;
	
	private boolean play=false;
	
	private SoundPool soundPool;
	public int beat1ID;
	//private int beat2ID;
	public int sax01ID;
	
	public Timeline(Context context) {
		this.record = new Record();
		this.context = context;

		// Load the sound
		soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		/*soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true;
			}
		});*/
		// sounds we use
		this.beat1ID = soundPool.load(this.context, R.raw.beat1, 1);
		//this.beat2ID = soundPool.load(this.context, R.raw.beat2, 1);
		this.sax01ID = soundPool.load(this.context, R.raw.sax01, 2);
	}
	
	@Override
	protected String doInBackground(Void... params) {
		read();
		return "Done";
	}
	
	public void read() {
		this.play = true;
		
		/* Set volume
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume; */
		
		long startTime;
		long endTime;
		long currentDuration;

		long barreTime = 2000000000l; //2 secs
		long beatTime = barreTime/4;
		byte beatCount = 4; //4 beats per barre
		byte count=0;
		
		startTime = System.nanoTime();
		
		while(this.play) {
			endTime = System.nanoTime();
			currentDuration = endTime-startTime;
			// This if statement only produces beats
			if (endTime-startTime >= beatTime) {
				startTime += beatTime;
				count++;
				count%=beatCount;
				if (count == 0) { 	//barre complete
					soundPool.play(beat1ID, 1f, 1f, 1, 0, 1.0f);
				}
				else { 				//beat complete
					soundPool.play(beat1ID, 1f, 1f, 1, 0, 1.0f);
				}
			}
			
			//This if statement checks for output sound.
			if ((!record.isEmpty()) && record.playNextSound(currentDuration)) {
				 record.getCurrentSoundRecording().play();
				 record.soundPlayed();
			}
		}
	}
	
	public void switchMode() {
		if (this.play) this.play=false;
		else this.read();
	}
	
	public SoundPool getSoundPool() {
		return this.soundPool;
	}

}
