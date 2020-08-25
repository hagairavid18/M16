package bgu.spl.mics.application.passiveObjects;

/**
 * Passive object representing a message sent from Q instance to M instance which tells if the gadget M asked for is
 * available if it is then in what time did Q got the mission from M.
 */
public class QReport {
    private Boolean is_available;
    private int Q_time;
    /**
     contractor
     @param is_available true if gadget is available
     @param q_time the time when q got the mission
     */
    public QReport(Boolean is_available, int q_time) {
        this.is_available = is_available;
        Q_time = q_time;
    }
    /**
     * @return true if gadget is available
     */
    public Boolean getIs_available() {
        return is_available;
    }
    /**
     * @return the time Q got the mission
     */
    public int getQ_time() {
        return Q_time;
    }
}
