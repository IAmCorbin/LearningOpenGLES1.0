package net.iamcorbin.learning_opengl_es;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.SystemClock;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

	//application reference
	private LearningOpenGLApp App;
	//shapes
	private FloatBuffer shapeVB;
	//drawmode
	private int drawMode;
	//count
	private int shapeCount;
	//activity number
	private int activityNum;
	//touch switch
	private boolean b_touchSwitch;
	
	public float mAngle;
	
	public OpenGLRenderer(LearningOpenGLApp app) {
		this.App = app;
		this.b_touchSwitch = false;
	}
	
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	// Set the background frame color
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        
        // initialize the triangle vertex array
        initShapes();
        
        // Enable use of vertex arrays
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    }
    
    public void onDrawFrame(GL10 gl) {
    	if(App.getActivityNum() != this.activityNum) { 
        	this.activityNum = App.getActivityNum();
        	//activity has changed, reinitialize the shapes
        	initShapes();
        }
    	
    	// Redraw background color
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        // Set GL_MODELVIEW transformation mode
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();   // reset the matrix to its default state
        
        // When using GL_MODELVIEW, you must set the view point
        GLU.gluLookAt(gl, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        
        // Create a rotation for the triangle
        if(this.b_touchSwitch)
        	//touch rotate
        	gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
        else {
        	//auto rotate
            long time = SystemClock.uptimeMillis() % 4000L;
            float angle = 0.090f * ((int) time);
            gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
        }
        
        // Draw the triangle
        gl.glColor4f(0.63671875f, 0.76953125f, 0.22265625f, 0.0f);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, shapeVB);
        gl.glDrawArrays(this.drawMode, 0, shapeCount);
    }
    
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        
        // make adjustments for screen ratio
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
        gl.glLoadIdentity();                        // reset the matrix to its default state
        gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);  // apply the projection matrix
        
    }
  
    private void initShapes(){
        
    	Vector<Float> shapeCoords = new Vector<Float>();
    	
    	switch(this.activityNum) {
    		case 1:
    			//square
    			shapeCoords.addAll(((List<Float>)Arrays.asList(	
    				-0.5f, 0.5f, 0f,
    				-0.5f, -0.5f, 0f,
    				0.5f, 0.5f, 0f,
    				0.5f, -0.5f, 0f
    			)));
    			this.drawMode = GL10.GL_TRIANGLE_STRIP;
    	    	this.shapeCount = 4;
    			break;
    		case 2:
    			//triangle
    			shapeCoords.addAll(((List<Float>)Arrays.asList(	
		            -0.5f, -0.25f, 0f,
		             0.5f, -0.25f, 0f,
		             0.0f,  0.559016994f, 0f
		        )));
		    	this.drawMode = GL10.GL_TRIANGLES;
    	    	this.shapeCount = 3;
		    	break;
    		case 3:
    			//points
    			shapeCoords.addAll(((List<Float>)Arrays.asList(	
		            -1.5f, -0.25f, 0f,
		             0.5f, -1.25f, 0f
		        )));
		    	this.drawMode = GL10.GL_LINE_LOOP;
    	    	this.shapeCount = 2;
		    	break;
		    default:
		    	break;
    	}
    	
        // initialize vertex Buffer for triangle  
        ByteBuffer vbb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                shapeCoords.size() * 4); 
        vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
        shapeVB = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
        Float FShapeCoords[] = (shapeCoords.toArray(new Float[shapeCoords.size()]));
        float[] fShapeCoords = new float[FShapeCoords.length];
        for(int n=0; n<FShapeCoords.length; n++) {
        	fShapeCoords[n] = FShapeCoords[n].floatValue();
        }
        shapeVB.put(fShapeCoords);    // add the coordinates to the FloatBuffer
        shapeVB.position(0);            // set the buffer to read the first coordinate
    
    }
   
    /**
     * Trigger the touch switch on/off
     */
    public void triggerTouchSwitch() {
    	if(this.b_touchSwitch == true)
    		this.b_touchSwitch = false;
    	else if(this.b_touchSwitch == false)
    		this.b_touchSwitch = true;
    }
    /**
     * Get the touch switch value
     */
    public boolean checkTouchSwitch() {
    	return this.b_touchSwitch;
    }
}