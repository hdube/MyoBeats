package myo.beats;

import java.util.ArrayList;
import java.util.Collections;

import timeline.Packet;
import timeline.Sound;
import timeline.SoundPlayer;
import timeline.SoundRecording;
import timeline.Timeline;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public final static String[] Instrument = {"Drums 1", "Drums 2", "SFX", "Bass", "Go Crazy"};
	int position = 0, counter = 0;
	Vector3 currentAccel = new Vector3(0.0, 0.0, 0.0);
	// 0. Roll, 1. Pitch, 2. Yaw
	Float[] prevOrient = {0.0f, 0.0f, 0.0f};
	float currentYaw = 0.0f;
	Float[] currentOrient = {0.0f, 0.0f, 0.0f};
	float initYaw = 0.0f;
	boolean ready = false, leftExec = false, rightExec = false;
	Timeline timeline;
	
	/*
	 * Recording stuff
	 */
	long recordStartTime = 0;
	long currentTime = 0;
	boolean record = false;
	Packet packet;
	ArrayList<SoundPlayer> soundList = new ArrayList<SoundPlayer>();
	
	// This code will be returned in onActivityResult() when the enable Bluetooth activity exits.
    private static final int REQUEST_ENABLE_BT = 1;
    
    private TextView[] counters = {null, null};
    private int[] counterInts = {0,0};

    private TextView mTextView;
    private TextView InstrumentView;

    // Classes that inherit from AbstractDeviceListener can be used to receive events from Myo devices.
    // If you do not override an event, the default behavior is to do nothing.
    private DeviceListener mListener = new AbstractDeviceListener() {

        private Arm mArm = Arm.UNKNOWN;
        private XDirection mXDirection = XDirection.UNKNOWN;

        // onConnect() is called whenever a Myo has been connected.
        @Override
        public void onConnect(Myo myo, long timestamp) {
            // Set the text color of the text view to cyan when a Myo connects.
            mTextView.setTextColor(Color.CYAN);
        }

        // onDisconnect() is called whenever a Myo has been disconnected.
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            // Set the text color of the text view to red when a Myo disconnects.
            mTextView.setTextColor(Color.RED);
        }

        // onArmRecognized() is called whenever Myo has recognized a setup gesture after someone has put it on their
        // arm. This lets Myo know which arm it's on and which way it's facing.
        @Override
        public void onArmRecognized(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
            mArm = arm;
            mXDirection = xDirection;
        }

        // onArmLost() is called whenever Myo has detected that it was moved from a stable position on a person's arm after
        // it recognized the arm. Typically this happens when someone takes Myo off of their arm, but it can also happen
        // when Myo is moved around on the arm.
        @Override
        public void onArmLost(Myo myo, long timestamp) {
            mArm = Arm.UNKNOWN;
            mXDirection = XDirection.UNKNOWN;
        }

        /*
         * Subtract pitch by a threshhold
         * Fix yaw reorientation, print both values
         */
        
        // onOrientationData() is called whenever a Myo provides its current orientation,
        // represented as a quaternion.
        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
            // Calculate Euler angles (roll, pitch, and yaw) from the quaternion.
            float roll = (float) Math.toDegrees(Quaternion.roll(rotation));
            float pitch = (float) Math.toDegrees(Quaternion.pitch(rotation));
            float yaw = (float) Math.toDegrees(Quaternion.yaw(rotation));

            // Adjust roll and pitch for the orientation of the Myo on the arm.
            if (mXDirection == XDirection.TOWARD_ELBOW) {
                roll *= -1;
                pitch *= -1;
            }
            
            if (initYaw == 0.0f) {
            	initYaw = yaw;
            }
            
         // 0. Roll, 1. Pitch, 2. Yaw
            prevOrient[0] = currentOrient[0];
            prevOrient[1] = currentOrient[1];
            prevOrient[2] = currentOrient[2];
            currentYaw = yaw;
            
            currentOrient[0] = roll;
            currentOrient[1] = pitch;
            currentOrient[2] = currentYaw - initYaw;

            mTextView.setText("" + (packet.getBeatCount() + 1));
            
            if (currentOrient[1] < -46f) {
            	ready = true;
            }
        }
        
        public double getAccelMag(Vector3 accel) {
    		// calculate the magnitude of the acceleration
    		double acc_mag = Math.sqrt(accel.x()*accel.x() +
    			accel.y()*accel.y() + 
    			accel.z()*accel.z());
    		return acc_mag;
        }
        public void onAccelerometerData (Myo myo, long timestamp, Vector3 accel) {
        	currentAccel = accel;
        	if (leftExec && currentOrient[1] < -5.0f) leftExec = false; 
        	if (rightExec && currentOrient[1] < -5.0f) rightExec = false;
        	
        	
        	if (currentOrient[1] > -45.0f && ready) {
            		// LEFT
            		if (currentOrient[2] > 5.0f) {
            			noise(true);
            		}
            	
            		// RIGHT
            		else if (currentOrient[2] < -5.0f){
            			noise(false);
            		}
            		ready = false; 
        	}
             
            if (record) {
            	packet.getTimer().update();
            	//Log.e("test", "CURRENT: ")
                if (packet.getTimer().getCurrentTime() - packet.getTimer().getStartRecordTime() > packet.getBarretime()) {
        			Collections.sort(soundList);
        			Log.e("test", "Done Recording");
        			record = false;
        			packet.setRecording(false);
        		}
            }
        	
        }
        
        public void noise(boolean left) {
        	int soundID = 0;
        	String selector = InstrumentView.getText().toString();
        	SoundPool soundPool;
        	
        	packet.getTimer().update();
        	//Log.e("test", "" + timeline.beat1ID);
        	soundPool = timeline.getSoundPool();
        	
        	switch (selector) {
        		case "Drums 1":
        			if (left) soundID = timeline.cb_kickID;
        			else soundID = timeline.cb_snareID;
        			break;
        		case "Drums 2":
        			if (left) soundID = timeline.cb_clapID;
        			else soundID = timeline.cb_hatID;
        			break;
        		case "SFX":
        			if (left) soundID = timeline.electrohighID;
        			else soundID = timeline.electrolowID;
        			break;
        		case "Bass":
        			if (left) soundID = timeline.punk1ID;
        			else soundID = timeline.punk2ID;
        			break;
        		case "Go Crazy":
        			if (left) soundID = timeline.shotsID;
        			else soundID = timeline.everybodyID;
        			break;
        	}
        	
        	soundPool.play(soundID, 1f, 1f, 1, 0, 1f);

        	
        	if (record) {
        		currentTime = System.nanoTime();
        		soundList.add(new SoundPlayer(new Sound(packet, soundID), soundPool));
            	Log.e("test", "Cycle Time " + packet.getTimer().getCycleTime());
            	
            	packet.setRecordList(soundList);        		
        	}
        }
        
        // onPose() is called whenever a Myo provides a new pose.
        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Handle the cases of the Pose enumeration, and change the text of the text view
            // based on the pose we receive.
        	if (getAccelMag(currentAccel) > 1.3) return;
            switch (pose) {
                case UNKNOWN:
                    mTextView.setText(getString(R.string.hello_world));
                    break;
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
                    mTextView.setText(getString(restTextId));
                    break;
                case FIST:
                    mTextView.setText(getString(R.string.pose_fist));
                    break;
                case WAVE_IN:
                	MainActivity.this.PreviousInstrument(InstrumentView);
                    mTextView.setText(getString(R.string.pose_wavein));
                    break;
                case WAVE_OUT:
                	MainActivity.this.NextInstrument(InstrumentView);
                    mTextView.setText(getString(R.string.pose_waveout));
                    break;
                case FINGERS_SPREAD:
                	resetYaw();
                    mTextView.setText(getString(R.string.pose_fingersspread));
                    break;
                case THUMB_TO_PINKY:
                    mTextView.setText(getString(R.string.pose_thumbtopinky));
                    break;
            }

        }
        
        public void resetYaw() {
        	initYaw = currentYaw;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);
       
        
        mTextView = (TextView) findViewById(R.id.text);
        InstrumentView = (TextView) findViewById(R.id.textView1);    
        
        // First, we initialize the Hub singleton with an application identifier.
        Hub hub = Hub.getInstance();
        if (!hub.init(this)) {
            // We can't do anything with the Myo device if the Hub can't be initialized, so exit.
            Toast.makeText(this, "Couldn't initialize Hub", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Next, register for DeviceListener callbacks.
        hub.addListener(mListener);
        

	   packet = new Packet(new Timer());
	    timeline = new Timeline(this, packet); 
	    timeline.execute();
    }

    
    /** Called when the user clicks the Next Button */
    public void NextInstrument(View view) {
    	InstrumentView.setText(Instrument[(++position)%Instrument.length]);
    }
    
    /** Called when the user clicks the Previous Button */
    public void PreviousInstrument(View view) {
    	position--;
    	if (position == -1) position = 3;
    	InstrumentView.setText(Instrument[(position)%Instrument.length]);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If Bluetooth is not enabled, request to turn it on.
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // We don't want any callbacks when the Activity is gone, so unregister the listener.
        Hub.getInstance().removeListener(mListener);

        if (isFinishing()) {
            // The Activity is finishing, so shutdown the Hub. This will disconnect from the Myo.
            Hub.getInstance().shutdown();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth, so exit.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.action_scan == id) {
            onScanActionSelected();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onScanActionSelected() {
        // Launch the ScanActivity to scan for Myos to connect to.
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }
    
    public void Record() {
    	record = true;
    	packet.setRecording(true);
    	Log.e("test", "Recording");
    	packet.getTimer().setStartRecordTime();
    }
    
    public void Play(View view) {
    	//timeline.switchMode();
    	//mTextView.setText("" + packet.getCurrentDuration());
    	Record();
    }
    
    public void reset(View view) {
    	initYaw = currentYaw;
    }
    
    public void clear(View view) {
    	packet.setRecordList(null);
    }
}