package net.iamcorbin.learning_opengl_es;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.SystemClock;
import android.util.Log;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

	private static final String TAG = "OpenGLRenderer_DEBUG";
	//application reference
	private LearningOpenGLApp App;
	//shapes
	private FloatBuffer shapeVB;
	//colors
	private FloatBuffer colorVB;
	//drawmode
	private int drawMode;
	//count
	private int shapeCount;
	//activity number
	private int activityNum;
	//accelerometer values - passed in from OpenGLActivity
	private float accel[];
	
	private int row = 10;
	private int column = 10;
	
	
	public float xAngle;
	public float yAngle;
	public float zAngle;
	
	private final double PI = 3.141592653;
	private int i_numVertices = 55;
	
	//cube indices for activity 4
	private byte indices[] = {
				/*
				 * Example: 
				 * Face made of the vertices lower back left (lbl),
				 * lfl, lfr, lbl, lfr, lbr
				 */
	            0, 4, 5,    0, 5, 1,
	            //and so on...
	            1, 5, 6,    1, 6, 2,
	            2, 6, 7,    2, 7, 3,
	            3, 7, 4,    3, 4, 0,
	            4, 7, 6,    4, 6, 5,
	            3, 0, 1,    3, 1, 2
									};
	private ByteBuffer indexBuffer;
	
	/**
	 * Constructor 
	 * @param app Reference to the Application Object
	 */
	public OpenGLRenderer(LearningOpenGLApp app) {
		this.App = app;
		this.accel = accel;
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}
	
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	// Set the background frame color
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        
        // initialize the shapes
        initShapes();
        
        // Enable use of vertex arrays
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        
        //enable 3d display options
        gl.glEnable(GL10.GL_DEPTH_TEST);
        //gl.glCullFace(GL10.GL_BACK);
        //gl.glFrontFace(GL10.GL_CCW);
        //gl.glEnable(GL10.GL_CULL_FACE);
        //gl.glDisable(GL10.GL_CULL_FACE);
        
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
        //gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();   // reset the matrix to its default state
        //gl.glFrustumf(-4.0f, 4.0f, -6.0f, 6.0f, -20.0f, 20.0f);
        // When using GL_MODELVIEW, you must set the view point
        GLU.gluLookAt(gl, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        
        // Create a rotation for shapes
        if(this.App.getTouchMode()) {
        	//touch rotate
        	gl.glRotatef(xAngle, 1.0f, 0.0f, 0.0f);
        	gl.glRotatef(yAngle, 0.0f, 1.0f, 0.0f);
        	gl.glRotatef(zAngle, 0.0f, 0.0f, 1.0f);
        } else {
        	//auto rotate
            long time = SystemClock.uptimeMillis() % 4000L;
            float angle = 0.090f * ((int) time);
            gl.glRotatef(angle, 0.0f, 1.0f, 1.0f);
        }
        
        //gl.glColor4f(0.63671875f, 0.76953125f, 0.22265625f, 0.0f);
        //Set the face rotation
		gl.glFrontFace(GL10.GL_CCW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, shapeVB);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorVB);
        // Draw the shapes
        if(this.activityNum == 6) {
        	//activity 6 - cube
        	//make smaller
        	gl.glScalef(-0.5f, -0.5f, -0.5f);
        	//draw
        	gl.glDrawElements(this.drawMode, this.shapeCount, GL10.GL_UNSIGNED_BYTE, this.indexBuffer);
        	
        } else 
        	gl.glDrawArrays(this.drawMode, 0, this.shapeCount);
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
    	Vector<Float> colorCoords = new Vector<Float>();
    	
    	switch(this.activityNum) {
    		case 1:
    			//square
    			shapeCoords.addAll(((List<Float>)Arrays.asList(	
    			   -0.5f, 0.5f, 0f,
    			   -0.5f, -0.5f, 0f,
    				0.5f, 0.5f, 0f,
    				0.5f, -0.5f, 0f
    			)));
    			colorCoords.addAll(((List<Float>)Arrays.asList(
			            0.0f,  1.0f,  0.0f,  1.0f,
			            0.0f,  1.0f,  0.0f,  1.0f,
			            1.0f,  0.5f,  0.0f,  1.0f,
			            1.0f,  0.5f,  0.0f,  1.0f
			    								)));
    			this.drawMode = GL10.GL_TRIANGLE_STRIP;
    	    	this.shapeCount = 4;
    			break;
    		case 2:
    			//triangle
    			shapeCoords.addAll(((List<Float>)Arrays.asList(	
		            -0.5f, -0.25f, 0f,
		             0.5f, -0.25f, 0f,
		             0.0f,  0.559016994f, 0f,
		             0.0f,  0.559016994f, 1f,
		             0.5f, -0.25f, 1f,
		            -0.5f, -0.25f, 1f
		        )));
    			colorCoords.addAll(((List<Float>)Arrays.asList(
			            0.0f,  1.0f,  0.0f,  1.0f,
			            0.0f,  1.0f,  0.0f,  1.0f,
			            1.0f,  0.5f,  0.0f,  1.0f,
			            1.0f,  0.5f,  0.0f,  -1.0f,
			            0.0f,  1.0f,  0.0f,  -1.0f,
			            0.0f,  1.0f,  0.0f,  -1.0f
			    								)));
		    	this.drawMode = GL10.GL_TRIANGLES;
    	    	this.shapeCount = 6;
		    	break;
    		case 3:
    			//lines
    			shapeCoords.addAll(((List<Float>)Arrays.asList(	
		            -1.5f, -0.25f, 0f,
		             0.5f, -1.25f, 0f,
		             0.25f, 0.5f, 1f,
		             0.5f, -0.25f, 0.5f
		        )));
    			colorCoords.addAll(((List<Float>)Arrays.asList(
			            0.0f,  1.0f,  0.0f,  1.0f,
			            0.0f,  1.0f,  0.0f,  1.0f,
			            1.0f,  1.0f,  0.0f,  1.0f,
			            0.0f,  1.0f,  1.0f,  1.0f
			    								)));
		    	this.drawMode = GL10.GL_LINES;
    	    	this.shapeCount = 4;
		    	break;
    		case 4:
    			//circle
    			//radius
    			float R = 0.5f;
    			//angle
    			float t = 0;
    			Random rand = new Random();

    			//center point
    			shapeCoords.addAll(((List<Float>)Arrays.asList(0f, 0f, 0f)));
    			colorCoords.addAll(((List<Float>)Arrays.asList(1.0f,  1.0f,  1.0f,  1.0f)));
    			
    			for(int n=0; n < i_numVertices; n++) {
    				shapeCoords.addAll(((List<Float>)Arrays.asList((float) (R * Math.cos(t)), (float) (R * Math.sin(t)), 0f)));
    				//random color
    				colorCoords.addAll(((List<Float>)Arrays.asList(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0f)));
    				//increment angle
    				t += 2 * PI / i_numVertices;
    				/*Log.d(TAG, "t = "+String.valueOf(t)+
    						" | x = "+String.valueOf(R * Math.cos(t))+ 
    						" | y = "+String.valueOf(R * Math.sin(t))+" | n = "+String.valueOf(n));*/
    			}
    			
		    	this.drawMode = GL10.GL_TRIANGLE_FAN;
    	    	this.shapeCount = this.i_numVertices+1;
		    	break;
    		case 5:
    			//line circle
    			//radius
    			float R2 = 0.5f;
    			//angle
    			float t2 = 0;
    			Random rand2 = new Random();
    			
    			for(int n=0; n < i_numVertices; n++) {
    				shapeCoords.addAll(((List<Float>)Arrays.asList((float) (R2 * Math.cos(t2)), (float) (R2 * Math.sin(t2)), 0f)));
    				//random color
    				colorCoords.addAll(((List<Float>)Arrays.asList(rand2.nextFloat(), rand2.nextFloat(), rand2.nextFloat(), 0f)));
    				//increment angle
    				t2 += 2 * PI / i_numVertices;
    				Log.d(TAG, "t = "+String.valueOf(t2)+
    						" | x = "+String.valueOf(R2 * Math.cos(t2))+ 
    						" | y = "+String.valueOf(R2 * Math.sin(t2))+" | n = "+String.valueOf(n));
    			}
    			
		    	this.drawMode = GL10.GL_LINE_LOOP;
    	    	this.shapeCount = this.i_numVertices;
		    	break;
    		case 6:
    			/** Cube **/
    			shapeCoords.addAll(((List<Float>)Arrays.asList(	
    					            -1.0f, -1.0f, -1.0f,	//lower back left (0)
    					            1.0f, -1.0f, -1.0f,		//lower back right (1)
    					            1.0f,  1.0f, -1.0f,		//upper back right (2)
    					            -1.0f, 1.0f, -1.0f,		//upper back left (3)
    					            -1.0f, -1.0f,  1.0f,	//lower front left (4)
    					            1.0f, -1.0f,  1.0f,		//lower front right (5)
    					            1.0f,  1.0f,  1.0f,		//upper front right (6)
    					            -1.0f,  1.0f,  1.0f		//upper front left (7)
    					    							)));
    			colorCoords.addAll(((List<Float>)Arrays.asList(
			            0.0f,  1.0f,  0.0f,  1.0f,
			            0.0f,  1.0f,  0.0f,  1.0f,
			            1.0f,  0.5f,  0.0f,  1.0f,
			            1.0f,  0.5f,  0.0f,  1.0f,
			            1.0f,  0.0f,  0.0f,  1.0f,
			            1.0f,  0.0f,  0.0f,  1.0f,
			            0.0f,  0.0f,  1.0f,  1.0f,
			            1.0f,  0.0f,  1.0f,  1.0f
			    								)));
    			this.drawMode = GL10.GL_TRIANGLES;
    			this.shapeCount = 36;
    			break;
    		case 7:
    			//graph lines
    			shapeCoords.addAll(((List<Float>)Arrays.asList(	
			            -2.0f, 0.0f, 0.0f,	     //x line
			            2.0f, 0.0f, 0.0f,		//
			            0.0f, -2.0f, 0.0f,	//y line
			            0.0f, 2.0f, 0.0f,		//
			            0.0f, 0.0f, -2.0f,		//z line
			            0.0f, 0.0f, 2.0f		//
			    							)));
		    	colorCoords.addAll(((List<Float>)Arrays.asList(
		            0.0f,  1.0f,  0.0f,  1.0f,
		            0.0f,  1.0f,  0.0f,  1.0f,
		            1.0f,  0.0f,  0.0f,  1.0f,
		            1.0f,  0.0f,  0.0f,  1.0f,
		            0.0f,  0.0f,  1.0f,  1.0f,
		            0.0f,  0.0f,  1.0f,  1.0f
		    								)));
    			this.drawMode = GL10.GL_LINES;
    			this.shapeCount = 6;
    			break;
    		case 8:
    			//hemisphere
    			
    			Random rand3 = new Random();
    			R = 0.5f;
    			
	            float angStep = (float) ((PI / 2 ) / this.i_numVertices);
	            float ang1 = 0;
	            float ang2 = 0;
	        	for(int r = 0; r < row; r++) {
	        		for(int c = 0; c < column; c++) {
	        			//draw 2 points
	        			shapeCoords.addAll(((List<Float>)Arrays.asList(	
	        					(float) (R * Math.cos(ang1) * Math.cos(ang2)), (float) ( R * Math.sin(ang1)), (float) ( R * Math.cos(ang1) * Math.sin(ang2)),
	        					(float) (R * Math.cos(ang1+angStep) * Math.cos(ang2)), (float) (R * Math.sin(ang1+angStep)), (float) (R * Math.cos(ang1+angStep) * Math.sin(ang2)))));
	        			//color points randomly
	        			colorCoords.addAll(((List<Float>)Arrays.asList(rand3.nextFloat(), rand3.nextFloat(), rand3.nextFloat(), 0f,
	        														   rand3.nextFloat(), rand3.nextFloat(), rand3.nextFloat(), 0f)));
	        			//increment angle
	        			ang2 += angStep;
	        		}
	        		ang1 += angStep;
	        	}
    			this.drawMode = GL10.GL_TRIANGLE_STRIP;
    			this.shapeCount = 100;
    			break;	
		    default:
		    	break;
    	}
    	
    	
    	
    	
        // initialize vertex Buffer  
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

        //initialize color Buffer
        vbb = ByteBuffer.allocateDirect(colorCoords.size() * 4);
		vbb.order(ByteOrder.nativeOrder());
		colorVB = vbb.asFloatBuffer();
		Float FColorCoords[] = (colorCoords.toArray(new Float[colorCoords.size()]));
		float[] fColorCoords = new float[FColorCoords.length];
		for(int n=0; n<FColorCoords.length; n++) {
			fColorCoords[n] = FColorCoords[n].floatValue();
		}
		colorVB.put(fColorCoords);
		colorVB.position(0);
		
    }
}