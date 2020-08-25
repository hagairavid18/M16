package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentAvailableEvent;
import bgu.spl.mics.application.messages.ExecuteMissionEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MoneyPennyReport;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;

import java.util.LinkedList;
import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	private int moneypenny_id;
	private int curr_time;
	private Squad squad  = Squad.getInstance();
	private List<String> serials;
	private int currActiveM;//**new
	/**
	 contractor
	 @param moneypenny_id object name
	 @param serials list of all agents
	 @param currActiveM counter of how many active Ms there are right now
	 */
	public Moneypenny(int moneypenny_id,List<String> serials,int currActiveM) {//**new signature
		super( "MoneyPenny " + moneypenny_id);

		this.moneypenny_id = moneypenny_id;
		this.serials=serials;
		this.currActiveM=currActiveM;//**new
	}

	/**
	 *  initialize the m.p. object:
	 * sets its callbacks
	 * 	 subscribe the needed broadcasts/events:
	 * TimeService, AgentAvailableEvent and TerminateBroadcast
	 */

	@Override
	protected void initialize() {


		//subscribe to timeTick
		Callback<TickBroadcast> call1 = (TickBroadcast t) -> {
//			if (t.isTerminated()) {
//				//will release all agent in order to avoid deadlocks between the any moneypanny which is not the "spacial" one
//				squad.releaseAgents(serials);//when squad gets this kind of list it release all agents
//				terminate();
//			}
			//**new timeService is no longer in charge of mp termination
			curr_time = t.getCurrTime();
		};

		subscribeBroadcast(TickBroadcast.class, call1);

		if (moneypenny_id != 0) {//will create a call back to AgentAvailableEvent

			//subscribe to AgentAvailableEvent
			Callback<AgentAvailableEvent> call2 = (AgentAvailableEvent a) -> {

				if (squad.getAgents(a.getSerial_list()) == true) { //in case all agents are available
					complete(a, new MoneyPennyReport(true, squad.getAgentsNames(a.getSerial_list()), this.moneypenny_id)); //complete the mission with succsess
				} else { // in case one of the agent is missing
					complete(a, new MoneyPennyReport(false, null, this.moneypenny_id));//can't get agents name because they are not in the squad
				}
			};
			subscribeEvent(AgentAvailableEvent.class, call2);

		}else {//we assign one moneyPenny to do only release and send events
			//will create a call back to ExecuteMissionEvent
			Callback<ExecuteMissionEvent> call3 = (ExecuteMissionEvent e) -> {
				if (e.isToExecute()){

					squad.sendAgents(e.getSerial_list(), e.getDuration()); //send agents to Mission
				}else {
					squad.releaseAgents(e.getSerial_list());
				}
				complete(e,true);
			};
			subscribeEvent(ExecuteMissionEvent.class, call3);
		}

		Callback<TerminateBroadcast> call3 = (TerminateBroadcast t)->{//**new callback
			currActiveM--;
			if(currActiveM<=0) {
				squad.releaseAgents(serials);//when squad gets this kind of list it release all agents
				terminate();
			}
		};
		subscribeBroadcast(TerminateBroadcast.class,call3);




	}

}
