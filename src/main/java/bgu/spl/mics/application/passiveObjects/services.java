package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.subscribers.Intelligence;

/**
 * Passive object needed for parsing the json file
 */
public class Services {
    private int M;
    private int Moneypenny;
    public Intelligence[] intelligence;
    private int time;
    /**
     * @return the number of M we got
     */
    public int getM() {
        return M;
    }
    /**
     * @return the number of m.p. we got
     */
    public int getMoneypenny() {
        return Moneypenny;
    }
    /**
     * @return the object of Intelligence
     */
    public Intelligence[] getIntelligence() {
        return intelligence;
    }
    /**
     * @return the time we got for the whole proses
     */
    public int getTime() {
        return time;
    }
}
