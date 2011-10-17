package net.iamcorbin.learning_opengl_es;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

public class OpenGLActivity extends Activity {
    
    private GLSurfaceView mGLView;
    private OpenGLRenderer mRenderer;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //setup renderer
        mRenderer = new OpenGLRenderer((LearningOpenGLApp)this.getApplication());
        
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

	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private OpenGLRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;
	
	
    public HelloOpenGLES20SurfaceView(Context context, OpenGLRenderer renderer){
        super(context);
        
        // Set the Renderer for drawing on the GLSurfaceView
        
        mRenderer = renderer;
        setRenderer(mRenderer);
        // Render the view only when there is a change
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
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
                
                mRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR;
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    } 
}
