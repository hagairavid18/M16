package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;


/**
 * Passive object representing a message sent from M instance to Q instance in order to check if specific gadget is available.
 */
public class GadgetAvaliableEvent implements Event  {
    private String gadget;

    /**
     contractor
     <p> the gadget needed
     */
    public GadgetAvaliableEvent(String gadget) {
        this.gadget = gadget;
    }
    /**
     * @return the gadget needed for the mission
     */
    public String getGadget() {
        return gadget;
    }
}
