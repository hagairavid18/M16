package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;


/**
 * Passive object representing a message sent TimeService to all subscribers, tells about the current time
 */
public class TickBroadcast implements Broadcast {
    private int curr_time;
    private boolean terminated;

    /**
     contractor
     @param curr_time the current time
     @param terminated true if program should stop, false otherwise
     */
    public TickBroadcast(int curr_time, boolean terminated) {
        this.curr_time = curr_time;
        this.terminated = terminated;
    }
    /**
     * @return the current time
     */
    public int getCurrTime() {
        return curr_time;
    }
    /**
     * @return if time finished
     */
    public boolean isTerminated() {
        return terminated;
    }
}
