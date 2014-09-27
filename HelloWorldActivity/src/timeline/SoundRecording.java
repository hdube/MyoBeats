package timeline;

public final class SoundRecording {

	private String soundPath; 	//not sure if we'll use that
	private long[] timers;
	private Record record;
	private int count = 0;
	private int index = 0;
	
	public SoundRecording(String soundPath, long[] timers, Record record) {
		this.soundPath = soundPath;
		this.count = timers.length;
		this.timers = new long[this.count];
		for (int i=0; i<this.count; i++)
			this.timers[i] = timers[i];
		this.record = record;
	}
	
	// get the sound to be made
	public String getSoundPath() {
		return this.soundPath;
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
}
