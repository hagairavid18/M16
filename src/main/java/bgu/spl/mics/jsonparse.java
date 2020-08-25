package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Services;

import java.util.List;

/**
 * Passive object needed for the parse of a json file
 */

public class JsonParse {
    private String[] inventory;
    private Agent[] squad;
    private Services services;
    /**
     * @return a list of gadgets
     */
    public String[] getInventory() {
        return inventory;
    }
    /**
     * @return the squad
     */
    public Agent[] getSquad() {
        return squad;
    }
    /**
     * @return a services object holds information of the missions, intelligence exe
     */
    public Services getServices() {
        return services;
    }
}
