package bgu.spl.mics.application.passiveObjects;

import java.util.List;

/**
 * Passive object representing a message sent from m.p. instance to M instance which tells about m.p mission sent from M.
 */
public class MoneyPennyReport {
    private Boolean areAvailable;
    private List<String> agents_names;
    private int moneyPenny_id;

    /**
     contractor
     @param areAvailable true if agents available false otherwise
     @param agents_names list of agents needed for the mission
     @param moneyPenny_id the m.p. which was in charge of the mission
     */
    public MoneyPennyReport(Boolean areAvailable, List<String> agents_names, int moneyPenny_id) {
        this.areAvailable = areAvailable;
        this.agents_names = agents_names;
        this.moneyPenny_id = moneyPenny_id;
    }
    /**
     * @return true if agents are available
     */
    public Boolean getAreAvailable() {
        return areAvailable;
    }
    /**
     * @return agents names
     */
    public List<String> getAgents_names() {
        return agents_names;
    }
    /**
     * @return m.p. id
     */
    public int getMoneyPenny_id() {
        return moneyPenny_id;
    }
}
