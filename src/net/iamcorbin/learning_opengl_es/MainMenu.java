package net.iamcorbin.learning_opengl_es;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * Front Menu for OpenGL ES Learning App
 * @author corbin
 *
 */
public class MainMenu extends Activity implements OnClickListener {
	
	private Button[] buttons = new Button[4];
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        
        this.buttons[0] = (Button)findViewById(R.id.a1);
        this.buttons[1] = (Button)findViewById(R.id.a2);
        this.buttons[2] = (Button)findViewById(R.id.a3);
        this.buttons[3] = (Button)findViewById(R.id.a4);
        for(int x=0; x < this.buttons.length; x++) {
        	this.buttons[x].setOnClickListener(this);
        }
        
    }
    
    public void onClick(View v) {
    	Intent iOpenGL = new Intent(MainMenu.this, OpenGLActivity.class);
    	switch(v.getId()) {
    		case R.id.a1:
    			((LearningOpenGLApp)this.getApplication()).setActivityNum(1);
    			break;
    		case R.id.a2:
    			//set activity num
    			((LearningOpenGLApp)this.getApplication()).setActivityNum(2);
    			break;
    		case R.id.a3:
    			//set activity num
    			((LearningOpenGLApp)this.getApplication()).setActivityNum(3);
    			break;
    		case R.id.a4:
    			//set activity num
    			((LearningOpenGLApp)this.getApplication()).setActivityNum(4);
    			break;
    		default:
    			return;
    	}
    	startActivity(iOpenGL);
    	
    }
}