package timeline;

import java.util.ArrayList;

public class Packet {
	boolean recording = false;
	long currentDuration = 0;
	ArrayList<SoundPlayer> recordList;
	static int index = 0;
	private int beatCount = 0;
	
	public int getBeatCount() {
		return beatCount;
	}

	public void setBeatCount(int beatCount) {
		this.beatCount = beatCount;
	}

	public Packet() {
		this.recordList = new ArrayList<SoundPlayer>();
	}
	
	public ArrayList<SoundPlayer> getRecordList() {
		return recordList;
	}

	public void setRecordList(ArrayList<SoundPlayer> recordList) {
		this.recordList = recordList;
	}

	public void setCurrentDuration(long val) {
		currentDuration = val;
	}
	
	public long getCurrentDuration() {
		return currentDuration;
	}
	
	public void setRecording(boolean val) {
		this.recording = val;
	}
	
	public boolean getRecording() {
		return recording;
	}
}
