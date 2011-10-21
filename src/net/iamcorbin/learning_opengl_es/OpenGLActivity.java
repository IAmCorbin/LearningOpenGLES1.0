package net.iamcorbin.learning_opengl_es;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

public class OpenGLActivity extends Activity {
    
	private static final String TAG = "OpenGLActivity_DEBUG";
	
    private GLSurfaceView mGLView;
    private OpenGLRenderer mRenderer;
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //setup renderer
        mRenderer = new OpenGLRenderer((LearningOpenGLApp)this.getApplication());
        
        //force portrait view
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new HelloOpenGLES20SurfaceView(this, mRenderer);
        setContentView(mGLView);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.openglactivity, menu);
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	super.onOptionsItemSelected(item);
    	//find which item has been selected
    	switch(item.getItemId()) {
    		case R.id.openglactivityMenuTouch:
    			((LearningOpenGLApp)this.getApplication()).triggerTouchMode();
    			return true;
    	}
    	//return false if nothing handled
    	return false;
    }
}
  
class HelloOpenGLES20SurfaceView extends GLSurfaceView {

	private static final String TAG = "HelloOpenGLES20SurfaceView_DEBUG";
	
	
	
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private OpenGLRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;
    
    //accelerometer sensor
	private SensorManager sensorManager;
	//accelerometer values
	//[0]x -roll;
	//[1]y - pitch;
	//[2]z - yaw;
	private float[] accel;
	//rotation matrix
	private float[] rmatrix;
	private static final float f_movementMod = 0.8f;
	
    public HelloOpenGLES20SurfaceView(Context context, OpenGLRenderer renderer){
        super(context);
        
        // Set the Renderer for drawing on the GLSurfaceView
        
        mRenderer = renderer;
        setRenderer(mRenderer);
        // Render the view only when there is a change
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        
        
        accel = new float[] {0f, 0f, 0f, 0f, 0f, 0f};
        rmatrix = new float[9];
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener,
                                       accelerometer,
                                       SensorManager.SENSOR_DELAY_FASTEST);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
    
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
    
                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                  dx = dx * -1 ;
                }
    
                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                  dy = dy * -1 ;
                }
                
                mRenderer.zAngle += (dx + dy) * TOUCH_SCALE_FACTOR;
                requestRender();
                break;
            case MotionEvent.ACTION_DOWN:
            	if(x<10 && y<10)
            		
            	break;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
    
    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        double calibration = SensorManager.STANDARD_GRAVITY;
        
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }

        //map accelerometer to x and y object rotations
        public void onSensorChanged(SensorEvent event) {
        		
          
          if(event.values[0] > 1)
        	  mRenderer.yAngle -= f_movementMod;
          else if(event.values[0] < -1)
        	  mRenderer.yAngle += f_movementMod;
        	//Log.d(TAG,"values[0]="+event.values[0]+"accel[0]="+accel[0]+" | Math.abs="+Math.abs(event.values[0] % accel[0]));
        	//pitch check?
        
          if(event.values[1] > 1) {
        	 mRenderer.xAngle -= f_movementMod;
        	 //Log.d(TAG, "event.values[1]="+event.values[1]);
          }
          else if(event.values[1] < -1) {
        	 mRenderer.xAngle += f_movementMod;
        	 //Log.d(TAG, "event.values[1]="+event.values[1]);
          }
        
          /*
          if(event.values[2] > 1) {
        	  mRenderer.xAngle += f_movementMod;
        	  Log.d(TAG, "event.values[2]="+event.values[2]);
          } else if(event.values[2] < -1) {
        	  mRenderer.xAngle -= f_movementMod;
              Log.d(TAG, "event.values[2]="+event.values[2]);
          }
          */
          accel[0] = event.values[0];
          accel[1] = event.values[1];
          //accel[2] = event.values[2];
          
          
          //Log.d(TAG,"Accel-sensor | roll:"+accel[0]+"| pitch:"+accel[1]+"| yaw:"+accel[2]);
        }
      };
    
}
