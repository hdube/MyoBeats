package timeline;

public class Sound implements Comparable {
	long timestamp;
	int soundID;
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getSoundID() {
		return soundID;
	}

	public void setSoundID(int soundID) {
		this.soundID = soundID;
	}

	public Sound(Packet packet, int soundID) {
		this.timestamp = packet.getTimer().getCycleTime();
		this.soundID = soundID;
	}

	@Override
	public int compareTo(Object another) {
		if (this.timestamp > ((Sound) another).getTimestamp()) return 1;
		else if (this.timestamp < ((Sound) another).getTimestamp()) return -1;
		else return 0;
	}
}
