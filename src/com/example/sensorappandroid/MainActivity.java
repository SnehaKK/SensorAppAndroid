package com.example.sensorappandroid;

import java.util.List;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private boolean isFlashOn = false;
	// final String p=Parameters.FLASH_MODE_OFF;
	private Camera camera;
	private Button button;
    private boolean hasFlash;
    Parameters params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//isFlashOn=false;
		PackageManager pm = this.getPackageManager();

		// if device support camera?
		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
			Log.e("err", "Device has no camera!");
			return;
		}
		
		 // get the camera
        getCamera();
         
		button = (Button) findViewById(R.id.button_enable_sensors);
		button.setOnClickListener(new OnClickListener() {
			@Override
		    public void onClick(View v) {
		        if (isFlashOn) {
		        	Log.i("info", "turning torch light off!");
		            // turn off flash
		            turnOffFlash();
		        } else {
		        	Log.i("info", "turning torch light on!");
		        	// turn on flash
		            turnOnFlash();
		        }
		    }
		});
		// };
	}

	// Get the camera
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }
 
    // Turning On flash
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
            	Log.i("info", "camera== null or params=nul");
            	return;
            }
             
            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
        }
 
    }
    
    // Turning Off flash
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
             
            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
        }
    }
     
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


    @Override
    protected void onPause() {
        super.onPause();
        Log.i("info", "In Pause");
        // on pause turn off the flash
        turnOffFlash();
    }
 
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("info", "In Restart");
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("info", "In Resume");
        // on resume turn on the flash
        if(hasFlash)
            turnOnFlash();
    }
 
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("info", "In start");
        // on starting the app get the camera params
        getCamera();
    }
 
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("info", "In stop");
        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
 

	public void cameraAction() {
		camera = Camera.open();
		final Parameters p = camera.getParameters();
		List<String> flashModes = p.getSupportedFlashModes();
		String flashMode = p.getFlashMode();
		
		Log.i("info", "Flash mode: " + flashMode);
	    Log.i("info", "Flash modes: " + flashModes);
		if (isFlashOn) {

			Log.i("info", "torch is turn off!");

			p.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(p);
			camera.stopPreview();
			isFlashOn = false;

		} else {
			Log.i("info", "torch is turn on!");

			p.setFlashMode(Parameters.FLASH_MODE_TORCH);

			camera.setParameters(p);
			camera.startPreview();
			isFlashOn = true;
		}

	}

	public void closeApp(View view) {
		finish();
		System.exit(0);
	}
}
