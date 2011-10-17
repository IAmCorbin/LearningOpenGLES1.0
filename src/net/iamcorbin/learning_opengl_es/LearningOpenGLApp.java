package net.iamcorbin.learning_opengl_es;

import android.app.Application;

public class LearningOpenGLApp extends Application {

    private int activityNum;
    private boolean b_touchmode = true;

    public int getActivityNum() {
        return activityNum;
    }

    public void setActivityNum(int num) {
        this.activityNum = num;
    }
    
    public boolean getTouchMode() {
    	return b_touchmode;
    }
    public void triggerTouchMode() {
    	if(this.b_touchmode == true)
    		this.b_touchmode = false;
    	else if(this.b_touchmode == false)
    		this.b_touchmode = true;
    }
}