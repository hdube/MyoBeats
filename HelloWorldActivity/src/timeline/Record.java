package timeline;

import java.util.*;

import android.util.Log;

public class Record {

	private ArrayList<SoundRecording> recordList;
	private static int index;
	
	public Record() {
		recordList = new ArrayList<SoundRecording>();
	}
	
	public SoundRecording getCurrentSoundRecording() {
		SoundRecording temp = recordList.get(index%recordList.size());
		index += (index + 1)%recordList.size();
		return temp;
	}
	
	public void addSoundRecording(SoundRecording sound) {
		recordList.add(sound);
		Collections.sort(recordList);
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
		Log.e("test", "" + recordList.get(index).getCurrentTimer());
		if (timeStamp >= recordList.get(index).getCurrentTimer()) return true;
		return false;
	}
	
	public boolean isEmpty() {
		if (recordList.size()==0) return true;
		else {
			return false;
		}
	}
	
}
