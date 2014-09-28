package timeline;

import java.util.ArrayList;

import myo.beats.Timer;

public class Packet {
	Timer timer;
	boolean recording = false;
	long currentDuration = 0;
	ArrayList<SoundPlayer> recordList;
	static int index = 0;
	private int beatCount = 8;
	private long barretime =  4000000000L;
	
	public long getBarretime() {
		return barretime;
	}

	public int getBeatCount() {
		return beatCount;
	}

	public void setBeatCount(int beatCount) {
		this.beatCount = beatCount;
	}

	public Packet() {
		this.recordList = new ArrayList<SoundPlayer>();
	}
	
	public Packet(Timer timer) {
		this.timer = timer;
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
	
	public boolean isRecording() {
		return recording;
	}
	
	public Timer getTimer() {
		return timer;
	}
}
