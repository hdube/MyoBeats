package timeline;

import java.util.ArrayList;

import myo.beats.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.AsyncTask;
import android.util.Log;


public class Timeline extends AsyncTask<Void, Void, String> {
	
	int j = 0;
	private float volume;
	long startTime;
	long endTime;
	long currentDuration;
	Packet packet;
	
	//private boolean loaded = false;
	private SoundPlayer record;
	private Context context;
	
	private boolean play=false;
	
	private SoundPool soundPool;
	public int beat1ID;
	//private int beat2ID;
	public int sax01ID;
	public int cb_clapID;
	public int cb_hatID;
	public int sfx_crunchy_bassID;
	public int sfx_subbass_dropID;
	
	public Timeline(Context context, Packet packet) {
		//this.record = new Record();
		this.context = context;
		this.packet = packet;
		this.volume = 1.0f;

		// Load the sound
		soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
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
		this.cb_clapID = soundPool.load(this.context, R.raw.cb_clap, 3);
		this.cb_hatID = soundPool.load(this.context, R.raw.cb_hat, 4);
		this.sfx_crunchy_bassID = soundPool.load(this.context, R.raw.sfx_crunchy_bass, 5);
		this.sfx_subbass_dropID = soundPool.load(this.context, R.raw.sfx_subbass_drop, 6);
	}
	
	public void setRecord(SoundPlayer record) {
		this.record = record;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		read();
		return "Done";
	}
	
	public void read() {		
		/* Set volume
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume; */
		


		long barreTime = 2000000000l; //2 secs
		long beatTime = barreTime/4;
		byte beatCount = 4; //4 beats per barre
		byte count=0;
		
		startTime = System.nanoTime();
		
		while(true) {
			endTime = System.nanoTime();
			currentDuration = endTime-startTime;
			packet.setCurrentDuration(currentDuration);
			// This if statement only produces beats
			if (endTime-startTime >= beatTime) {
				startTime += beatTime;
				count++;
				count%=beatCount;
				packet.setBeatCount((int) count);
				if (count == 0) { 	//barre complete
					soundPool.play(beat1ID, getVolume(), getVolume(), 1, 0, 1.0f);
				}
				else { 				//beat complete
					soundPool.play(beat1ID, getVolume(), getVolume(), 1, 0, 1.0f);
				}
			}
			

			//This if statement checks for output sound.
			ArrayList<SoundPlayer> list = packet.getRecordList();
			
			if (count == 0) {
				synchronized (list) {
					for (SoundPlayer s : list) {
						s.setPlayed(false);
					}
				}
			}
			
			if ((!packet.getRecording()) ) {
				for (int i = list.size() - 1; i > 0; i--) {
					if (j == 0) {
						Log.e("test", "-------");
						Log.e("test", "" + (list.get(i).getSound().getTimestamp() - currentDuration));
					}
					if (list.get(i).getSound().getTimestamp() - currentDuration < 10000 && list.get(i).getSound().getTimestamp() - currentDuration >                                                                                                                                                                                                                                                                                                                                                                                                                       0) {
						list.get(i).play();
						break;
					}
				}
				j++;
			}
		}
	}
	
	public void switchMode() {
		if (this.play) {
			this.play=false;
			this.volume=0f;
		}
		else {
			this.play=true;
			this.volume=1f;
		}
	}
	
	public float getVolume() {
		return this.volume;
	}
	
	public SoundPool getSoundPool() {
		return this.soundPool;
	}
	
	public long getCurrentDuration() {
		//Log.e("test", "" + currentDuration);
		return this.currentDuration;
	}
}
