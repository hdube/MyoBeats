package timeline;

import java.util.*;

public class Record {

	private ArrayList<SoundRecording> recordList;
	
	public Record() {
		recordList = new ArrayList<SoundRecording>();
	}
	
	public void addSoundRecording(SoundRecording sound) {
		long timer = sound.getFirstTimer();
		int size = recordList.size();
		for (int i=0; i<size; i++) {
			if (recordList.get(i).getCurrentTimer() >= timer)
				recordList.add(i, sound);
		}
		if (recordList.size()!=size) recordList.add(sound);
	}
	
	public void soundPlayed() {
		SoundRecording workingSound = recordList.get(0);
		workingSound.gotoNextSound();
		recordList.remove(0);
		long timer = workingSound.getCurrentTimer();
		int size = recordList.size();
		for (int i=0; i<size; i++) {
			if (recordList.get(i).getCurrentTimer() >= timer)
				recordList.add(i, workingSound);
		}
		if (recordList.size()!=size) recordList.add(workingSound);
	}
	
	public boolean playNextSound(long timeStamp) {
		if (timeStamp >= recordList.get(0).getCurrentTimer()) return true;
		return false;
	}
	
}
