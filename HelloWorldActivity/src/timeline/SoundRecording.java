package timeline;

import android.media.SoundPool;
import android.util.Log;

public final class SoundRecording implements Comparable{
	//private static timer = 0;
	
	//private long[] timers;
	//private int count = 0;
	private static int count = 0;
	private static long minTime;
	int position = 0;
	private int index = 0;
	
	private Sound sound;
	private int soundID;
	private SoundPool soundPool;
	
	public SoundRecording(Sound sound, SoundPool soundPool) {
		this.soundPool = soundPool;
		this.soundID = soundID;
		this.sound = sound;
		position = count++;
		if (sound.getTimestamp() < minTime) minTime = sound.getTimestamp();
		//this.count = timers.length;
		//this.timers = new long[this.count];
		//for (int i=0; i<this.count; i++)
			//this.timers[i] = timers[i];
	}
	
	public long getCurrentTimer() {
		return sound.getTimestamp();
	}
	
	// get the next time we want to play the sound
	public void gotoNextSound() {
		this.index++;
		this.index%=count;
	}
	
	public long getFirstTimer() {
		return minTime;
	}
	
	public Sound getSound() {
		return this.sound;
	}
	
	public void play(float volume) {
		soundPool.play(soundID, volume, volume, 1, 0, 1.0f);
	}

	@Override
	public int compareTo(Object another) {
		return ((SoundRecording) another).getSound().compareTo(this.getSound());
	}
}
