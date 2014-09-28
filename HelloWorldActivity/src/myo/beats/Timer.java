package myo.beats;

public class Timer {
	long currentTime;
	long programInitTime;
	long startRecordTime = 0;
	long cycleTime;
	long barreTime = 4000000000L;
	
	public Timer() {
		programInitTime = System.nanoTime();
	}
	public synchronized void update() {
		currentTime = System.nanoTime();
		cycleTime = (currentTime - programInitTime)%(barreTime);
	}

	public synchronized long getCurrentTime() {
		return currentTime;
	}

	public synchronized void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public synchronized long getStartRecordTime() {
		return startRecordTime;
	}

	public synchronized void setStartRecordTime() {
		this.startRecordTime = System.nanoTime();
	}

	public synchronized long getCycleTime() {
		return cycleTime;
	}

	public synchronized void setCycleTime(long cycleTime) {
		this.cycleTime = cycleTime;
	}

	public synchronized long getBarreTime() {
		return barreTime;
	}

	public synchronized void setBarreTime(long barreTime) {
		this.barreTime = barreTime;
	}
}
