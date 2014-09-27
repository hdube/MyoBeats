package myo.beats;

import android.widget.TextView;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.XDirection;

public class MyoBeatsListener extends AbstractDeviceListener{
	TextView textView;
	Arm mArm;
	XDirection mXDirection;
	
	public MyoBeatsListener(TextView mTextView) {
		this.textView = mTextView;
	}
	
    // onArmRecognized() is called whenever Myo has recognized a setup gesture after someone has put it on their
    // arm. This lets Myo know which arm it's on and which way it's facing.
    @Override
    public void onArmRecognized(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
        mArm = arm;
        mXDirection = xDirection;
    }
    
	public void onPose(Myo myo, long timestamp, Pose pose) {
		switch (pose) {
        case REST:
            int restTextId = R.string.hello_world;
            switch (mArm) {
                case LEFT:
                    restTextId = R.string.arm_left;
                    break;
                case RIGHT:
                    restTextId = R.string.arm_right;
                    break;
            }
            textView.setText("rest");
            break;
        case FIST:
            textView.setText("FIST");
            break;
        case WAVE_IN:
            textView.setText("WAVE IN");
            break;
        case WAVE_OUT:
            textView.setText("WAVE OUT");
            break;
        case FINGERS_SPREAD:
            textView.setText("FS");
            break;
        case THUMB_TO_PINKY:
            textView.setText("TTP");
            break;
    }
	}

}
