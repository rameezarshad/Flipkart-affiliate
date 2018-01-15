package com.flipkart.android.register;

/**
 * Created by VectoR on 19-09-2017.
 */

public class Worker {
    private int currentThreads;
    private int concurrent;
    private int limit;
    private int repeat;
    private int rem;

    public int getRem() {
        return rem;
    }

    public void setRem(int rem) {
        this.rem = rem;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }


    public int getCurrentThreads() {
        return currentThreads;
    }

    public void setCurrentThreads(int currentThreads) {
        this.currentThreads = currentThreads;
    }

    public int getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(int concurrent) {
        this.concurrent = concurrent;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


}
