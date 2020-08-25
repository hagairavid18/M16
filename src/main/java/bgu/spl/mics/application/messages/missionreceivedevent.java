package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

/**
 * Passive object representing a message sent from Intelligence instance to M instance which tells about a new mission.
 */

public class MissionReceivedEvent implements Event {
    private String mission_name;
    private List<String> serial_number;
    private String gadget;
    private int timeExpired;
    private int time_issued;
    private int duration;

    public MissionReceivedEvent(){}

    /**
     contractor
     @param mission_name mission name
     @param serial_number list of agents needed for the mission
     @param gadget needed for the mission
     @param timeExpired when the mission is no longer relevant
     @param time_issued when the mission need to be started
     @param duration the mission duration
     */
    public MissionReceivedEvent(String mission_name, List<String> serial_number, String gadget, int timeExpired,int time_issued, int duration) {
        this.mission_name = mission_name;
        this.serial_number = serial_number;
        this.gadget = gadget;
        this.timeExpired=timeExpired;
        this.time_issued=time_issued;
        this.duration=duration;
    }

    /**
     * @return the mission name
     */
    public String getMission_name() {
        return mission_name;
    }
    /**
     * @return a list of agents needed for the mission
     */
    public List<String> getSerial_number() {
        return serial_number;
    }
    /**
     * @return the gadget needed for the mission
     */
    public String getGadget() {
        return gadget;
    }
    /**
     * @return the expired time
     */
    public int getTimeExpired() {
        return timeExpired;
    }
    /**
     * @return the issued time
     */
    public int getTime_issued() {
        return time_issued;
    }
    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }
}
