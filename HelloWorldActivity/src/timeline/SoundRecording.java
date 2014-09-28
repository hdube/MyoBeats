package timeline;

import android.media.SoundPool;

public final class SoundRecording {

	private long[] timers;
	private int count = 0;
	private int index = 0;
	
	private int soundID;
	private SoundPool soundPool;
	
	public SoundRecording(long[] timers, int soundID, SoundPool soundPool) {
		this.soundPool = soundPool;
		this.soundID = soundID;
		this.count = timers.length;
		this.timers = new long[this.count];
		for (int i=0; i<this.count; i++)
			this.timers[i] = timers[i];
	}
	
	public long getCurrentTimer() {
		return timers[index];
	}
	
	// get the next time we want to play the sound
	public void gotoNextSound() {
		this.index++;
		this.index%=count;
	}
	
	public long getFirstTimer() {
		return timers[0];
	}
	
	public void play(float volume) {
		soundPool.play(soundID, volume, volume, 1, 0, 1.0f);
	}
}
