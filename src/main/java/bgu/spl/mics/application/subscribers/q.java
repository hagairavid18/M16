package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvaliableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyPennyReport;
import bgu.spl.mics.application.passiveObjects.QReport;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private Inventory inventory = Inventory.getInstance();
	private int curr_time;
	private int mission_received_time;

	/**
	 contractor
	 */
	public Q() {
		super("Q");
		curr_time = 0;
		mission_received_time = 0;
	}

	/**
	 *  initialize the Q object:
	 * sets its callbacks
	 * 	 subscribe the needed broadcasts/events:
	 * TimeService, GadgetAvailableEvent
	 */

	@Override
	protected void initialize() {
		//subscribe to timeTick

		Callback<TickBroadcast> call1 = (TickBroadcast t) -> {
			if (t.isTerminated()) {
				terminate();
			}
			//**new: time service is no longer in charge of termination of Q //not good
			curr_time=t.getCurrTime();

		};
		subscribeBroadcast(TickBroadcast.class, call1);

		//subscribe to GadgetAvaliableEvent

		Callback<GadgetAvaliableEvent> call2 = (GadgetAvaliableEvent g) -> {

			mission_received_time = curr_time;
			if (inventory.getItem(g.getGadget())) { //in case the gadget ia available, acquire it
				complete(g,new QReport(true,mission_received_time));
			}else {
				complete(g,new QReport(false,mission_received_time));
			}

		};
		subscribeEvent(GadgetAvaliableEvent.class,call2);


	}
		


}
