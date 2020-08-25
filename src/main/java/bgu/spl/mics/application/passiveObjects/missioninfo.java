package bgu.spl.mics.application.passiveObjects;

import java.util.LinkedList;
import java.util.List;

/**
 * Passive data-object representing information about a mission.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class MissionInfo {
	private String[] serialAgentsNumbers;
	private int duration;
	private String gadget;
	private String name;
	private int timeExpired;
	private int timeIssued;


    /**
     * Sets the name of the mission.
     */
    public void setMissionName(String missionName) {
    	this.name = missionName;
	}

	/**
     * Retrieves the name of the mission.
     */
	public String getMissionName() {
		return name;
	}

    /**
     * Sets the serial agent number.
     */
    public void setSerialAgentsNumbers(List<String> serialAgentsNumbers) {
    	this.serialAgentsNumbers=new String[serialAgentsNumbers.size()];
    	int index=0;
    	for(String serial:serialAgentsNumbers){
    		this.serialAgentsNumbers[index]=serial;
    		index++;
		}
    }

	/**
     * Retrieves the serial agent number.
     */
	public List<String> getSerialAgentsNumbers() {
		List<String> retList=new LinkedList<>();
		for(String serial:this.serialAgentsNumbers)
			retList.add(serial);
		return retList;
	}

    /**
     * Sets the gadget name.
     */
    public void setGadget(String gadget) {
    	this.gadget=gadget;
    }

	/**
     * Retrieves the gadget name.
     */
	public String getGadget() {
		return this.gadget;
	}

    /**
     * Sets the time the mission was issued in milliseconds.
     */
    public void setTimeIssued(int timeIssued) {
    	this.timeIssued=timeIssued;
    }

	/**
     * Retrieves the time the mission was issued in milliseconds.
     */
	public int getTimeIssued() {
		return timeIssued;
	}

    /**
     * Sets the time that if it that time passed the mission should be aborted.
     */
    public void setTimeExpired(int timeExpired) {
    	this.timeExpired=timeExpired;
	}

	/**
     * Retrieves the time that if it that time passed the mission should be aborted.
     */
	public int getTimeExpired() {
		return this.timeExpired;
	}

    /**
     * Sets the duration of the mission in time-ticks.
     */
    public void setDuration(int duration) {
        this.duration=duration;
    }

	/**
	 * Retrieves the duration of the mission in time-ticks.
	 */
	public int getDuration() {
		return duration;
	}
}
