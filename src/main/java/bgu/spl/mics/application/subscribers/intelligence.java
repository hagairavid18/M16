package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private List<MissionInfo> missions;
	private int curr_time; // will hold the most updated time sent from TickBroadcast

	/**
	 contractor
	 @param name object name
	 @param missionInfoList list of missions needed to be executed, and the information of them
	 */
	public Intelligence(String name, List<MissionInfo> missionInfoList){
		super("Intelligence " + name);
		this.missions=missionInfoList;
		curr_time = -1;
	}


	/**
	 initialize the Intelligence object:
	 sets its callbacks
	 subscribe the needed broadcasts/events
	 */

	@Override
	protected void initialize() {
		// subscribe only to TickBroadcast

		//create a call back to TickBroadcast

		Callback<TickBroadcast> call = (TickBroadcast t) -> {
			if (!t.isTerminated()) {
				curr_time = t.getCurrTime();
				for (MissionInfo missionInfo : missions) {
					if (missionInfo.getTimeIssued() == curr_time) {
						MissionReceivedEvent missionReceivedEvent = new MissionReceivedEvent(missionInfo.getMissionName(), missionInfo.getSerialAgentsNumbers(), missionInfo.getGadget(), missionInfo.getTimeExpired(), curr_time, missionInfo.getDuration());

						Future<Report> future = getSimplePublisher().sendEvent(missionReceivedEvent);

					}
				}

			}else {
				terminate();
			}

		};

		subscribeBroadcast(TickBroadcast.class, call);

	}


	/**
	 * @return the missions list
	 */
	public List<MissionInfo> getMissions() {
		return missions;
	}
}
