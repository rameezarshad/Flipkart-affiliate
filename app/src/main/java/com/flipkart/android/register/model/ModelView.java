package com.flipkart.android.register.model;

import com.flipkart.android.register.Logic;

/**
 * Created by VectoR on 19-09-2017.
 */

public class ModelView {

    private String status;
    private String response;
    private String timeStamp, thread_name;
    private int color = -1;

    public Logic getLogic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    private Logic logic;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getThread_name() {
        return thread_name;
    }

    public void setThread_name(String thread_name) {
        this.thread_name = thread_name;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
