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
	
	private Button[] buttons = new Button[2];
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        
        this.buttons[0] = (Button)findViewById(R.id.button1);
        this.buttons[1] = (Button)findViewById(R.id.button2);
        for(int x=0; x < this.buttons.length; x++) {
        	this.buttons[x].setOnClickListener(this);
        }
        
    }
    
    public void onClick(View v) {
    	switch(v.getId()) {
    		case R.id.button1:
    			Intent i_button1 = new Intent(MainMenu.this, OpenGLActivity.class);
    			//set activity num
    			((LearningOpenGLApp)this.getApplication()).setActivityNum(1);
    			startActivity(i_button1);
    			break;
    		case R.id.button2:
    			Intent i_button2 = new Intent(MainMenu.this, OpenGLActivity.class);
    			//set activity num
    			((LearningOpenGLApp)this.getApplication()).setActivityNum(2);
    			startActivity(i_button2);
    			break;	
    		default:
    			return;
    	}
    	
    }
}