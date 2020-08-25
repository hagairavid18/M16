package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;


/**
 * Passive object representing a message sent from M instance to m.p. instance in order to send/release agents from
 * being locked in a certain mission.
 */
public class ExecuteMissionEvent implements Event {
    private List<String> serial_list;
    private int duration;
    private boolean toExecute;


    /**
     contractor
     <p> list of agents needed for a certain mission
     <p> the mission duration
     <p> boolean parameter which tells what action to do:
     true-> send the agents
     false-> release the agents immediately to do other tasks
     */
    public ExecuteMissionEvent(List<String> serial_list, int duration,boolean toExecute) {
        this.serial_list = serial_list;
        this.duration = duration;
        this.toExecute=toExecute;
    }
    /**
     * @return a list of agents needed for the mission
     */
    public List<String> getSerial_list() {
        return serial_list;
    }
    /**
     * @return the mission duration
     */
    public int getDuration() {
        return duration;
    }
    /**
     * @return if the mission need to be proceed
     */
    public boolean isToExecute() {
        return toExecute;
    }
}
