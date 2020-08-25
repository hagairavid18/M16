package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Agent;

import java.util.List;


/**
 * Passive object representing a message sent from M instance to m.p. instance in order to check if specific agents are available.
 */
public class AgentAvailableEvent implements Event{
    private List<String> serial_list;
    private int duration;

    /**
     contractor
     <p> list of agents needed for a certain mission
     <p> the mission duration
     */
    public AgentAvailableEvent(List<String> serial_list, int duration) {
        this.serial_list = serial_list;
        this.duration = duration;
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
}
