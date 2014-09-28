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
	public int cb_kickID;
	public int cb_snareID;
	public int sfx_crunchy_bassID;
	public int sfx_subbass_dropID;
	public int shotsID;
	public int everybodyID;
	public int electrohighID;
	public int electrolowID;
	public int punk1ID;
	public int punk2ID;
	
	public Timeline(Context context, Packet packet) {
		//this.record = new Record();
		this.context = context;
		this.packet = packet;
		this.volume = 1.0f;

		// Load the sound 
		soundPool = new SoundPool(13, AudioManager.STREAM_MUSIC, 0);
		/*soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true;
			}
		});*/
		// sounds we use
		this.cb_snareID = soundPool.load(this.context, R.raw.cb_snare, 1);
		this.beat1ID = soundPool.load(this.context, R.raw.beat1, 1);
		//this.beat2ID = soundPool.load(this.context, R.raw.beat2, 1);
		this.cb_kickID = soundPool.load(this.context, R.raw.cb_kick, 2);
		this.cb_clapID = soundPool.load(this.context, R.raw.cb_clap, 3);
		this.cb_hatID = soundPool.load(this.context, R.raw.cb_hat, 4);
		this.sfx_crunchy_bassID = soundPool.load(this.context, R.raw.sfx_crunchy_bass, 5);
		this.sfx_subbass_dropID = soundPool.load(this.context, R.raw.sfx_subbass_drop, 6);
		this.shotsID = soundPool.load(this.context, R.raw.shots, 7);
		this.everybodyID = soundPool.load(this.context, R.raw.everybody, 8);
		this.electrohighID = soundPool.load(this.context, R.raw.electrohigh, 9);
		this.electrolowID = soundPool.load(this.context, R.raw.electrolow, 10);
		this.punk1ID = soundPool.load(this.context, R.raw.punk1, 11);
		this.punk2ID = soundPool.load(this.context, R.raw.punk2, 12);
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
		


		long barreTime = packet.getBarretime(); //4 secs 
		long beatTime = barreTime/8;
		byte beatCount = 4; //4 beats per barre
		byte count=0;
		
		startTime = System.nanoTime();
		
		while(true) {
			//This if statement checks for output sound.
			ArrayList<SoundPlayer> list = packet.getRecordList();
			
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
					if (!(list == null)) {
						for (SoundPlayer s : list) {
							s.setPlayed(false);
						}
					}
					//soundPool.play(beat1ID, getVolume(), getVolume(), 1, 0, 1.0f);
				}
				else { 				//beat complete
					//soundPool.play(beat1ID, getVolume(), getVolume(), 1, 0, 1.0f);
				}
			}
			


			
			if (!(list == null) && !packet.isRecording()) {
				packet.getTimer().update();
				Log.e("test", "" + Math.abs(list.get(0).getSound().getTimestamp() - packet.getTimer().getCycleTime()));
				for (int i = 0; i < list.size(); i++) {
					if (Math.abs(list.get(i).getSound().getTimestamp() - packet.getTimer().getCycleTime()) < 1000000) {
						if(!list.get(i).hasBeenPlayed())list.get(i).play();
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
