package net.iamcorbin.learning_opengl_es;

import android.app.Application;

public class LearningOpenGLApp extends Application {

    private int activityNum;

    public int getActivityNum() {
        return activityNum;
    }

    public void setActivityNum(int num) {
        this.activityNum = num;
    }
}