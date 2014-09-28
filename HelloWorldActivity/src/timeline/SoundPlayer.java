package timeline;

import android.media.SoundPool;
import timeline.Sound;

public class SoundPlayer implements Comparable{
	Sound sound;
	SoundPool soundpool;
	boolean hasBeenPlayed = false;
	
	public SoundPlayer(Sound sound, SoundPool soundpool) {
		this.sound = sound;
		this.soundpool = soundpool;
	}
	
	public void play() {
		soundpool.play(sound.getSoundID(), 1f, 1f, 1, 0, 1.0f);
		hasBeenPlayed = true;
	}

	@Override
	public int compareTo(Object another) {
		SoundPlayer other = (SoundPlayer) another;
		
		if (this.sound.getTimestamp() > other.getSound().getTimestamp()) return -1;
		else if (this.sound.getTimestamp() < other.getSound().getTimestamp()) return 1;
		else return 0;
	}
	
	public void setPlayed(boolean val) {
		hasBeenPlayed = val;
	}
	
	public boolean hasBeenPlayed() {
		return hasBeenPlayed;
	}
	public Sound getSound() {
		return sound;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}
	
}
