package timeline;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class timeline implements Runnable {
	@Override
	public void run() {
		read();
	}
	
	public void read() {
		SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		// 160 bpm in 4/4 -> 60 secs/160 beats = 3 secs/8 beats = 1.5 secs/4 beats = 1.5 secs/barre
		long startTime;
		long currentDuration;

		long barreTime = 1500000000l; //1.5 secs
		long beatTime = (long)(barreTime/4);
		byte beatCount = 4; //4 beats per barre
		byte count=0;
		startTime = System.nanoTime();
		// 1 second = 1000000000l
		while(true) {
			currentDuration = startTime-System.nanoTime();
			if (currentDuration >= beatTime) {
				startTime%=beatTime;
				count++;
				count%=beatCount;
				if (count == 0) //barre complete
					System.out.println("Barre complete");
				else //beat complete
					System.out.println("Beat complete");
				//TODO: have new beat sound come out.
			}
			
			//TODO: read input from Oleg's class. I have to do that.
		}
	}

}
