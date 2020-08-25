package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private int curr_time; // will hold the most updated time sent from TickBroadcast
	private Diary diary = Diary.getInstance();
	private int M_id;
	/**
	 contractor
	 @param id object name
	 */
	public M(int id) {
		super( "M " + id);
	}


	//subscribe to TickBroadcast, MissionrecivedEvent

	/**
	 initialize the M object:
	 sets its callbacks
	 subscribe the needed broadcasts/events
	 */
	@Override
	protected void initialize() {
		//subscribe to TickBroadcast

		Callback<TickBroadcast> call = (TickBroadcast t) -> {
			if (t.isTerminated()) {
				Broadcast tBroadcast=new TerminateBroadcast();//before terminate, tell everyone who cares that im done **new
				getSimplePublisher().sendBroadcast(tBroadcast);//**new
				terminate();
			}
			curr_time=t.getCurrTime();
		};
		subscribeBroadcast(TickBroadcast.class, call);


		//subscribe to MissionReceivedEvent

		Callback<MissionReceivedEvent> missionCall = (MissionReceivedEvent m) ->{

			diary.increase();
			AgentAvailableEvent agentAvailableEvent = new AgentAvailableEvent(m.getSerial_number(),m.getDuration());
			Future <MoneyPennyReport> futureAgentAvailable = getSimplePublisher().sendEvent(agentAvailableEvent);
			if(futureAgentAvailable!=null) {

				if (futureAgentAvailable.get() != null) { //in case future resolved with null


					if (futureAgentAvailable.get().getAreAvailable()) {//continue only if agents are available
						GadgetAvaliableEvent gadgetAvaliableEvent = new GadgetAvaliableEvent(m.getGadget());
						Future<QReport> futureGadgetAvailable = getSimplePublisher().sendEvent(gadgetAvaliableEvent);


						if (futureGadgetAvailable != null) {

							if (futureGadgetAvailable.get() != null) { //in case future resolved with null

								if (futureGadgetAvailable.get().getIs_available()) {//continue only if gadget is available

									if (curr_time <= m.getTimeExpired()) { //send to moneypenny a message to send all agents

										ExecuteMissionEvent executeMissionEvent = new ExecuteMissionEvent(m.getSerial_number(), m.getDuration(), true);//send agents
										Future<MoneyPennyReport> moneyPennyReportFuture = getSimplePublisher().sendEvent(executeMissionEvent);

										Report report = new Report(m.getMission_name(), this.M_id, futureAgentAvailable.get().getMoneyPenny_id(), m.getSerial_number(), futureAgentAvailable.get().getAgents_names(), m.getGadget(), m.getTime_issued(), futureGadgetAvailable.get().getQ_time(), this.curr_time);
										diary.addReport(report);



										complete(m, report);
									} else {
										complete(m, null);
										ExecuteMissionEvent executeMissionEvent = new ExecuteMissionEvent(m.getSerial_number(), m.getDuration(), false);//release all agents
										Future<MoneyPennyReport> moneyPennyReportFuture = getSimplePublisher().sendEvent(executeMissionEvent);
									}

								} else {
									complete(m, null);
									ExecuteMissionEvent executeMissionEvent = new ExecuteMissionEvent(m.getSerial_number(), m.getDuration(), false);//release all agents
									Future<MoneyPennyReport> moneyPennyReportFuture = getSimplePublisher().sendEvent(executeMissionEvent);
								}
							} else {
								complete(m, null);
							}
						}else {//**new else

							ExecuteMissionEvent executeMissionEvent = new ExecuteMissionEvent(m.getSerial_number(), m.getDuration(), false);//release all agents
							Future<MoneyPennyReport> moneyPennyReportFuture = getSimplePublisher().sendEvent(executeMissionEvent);
							complete(m,null);
						}
					} else {

						complete(m, null);
					}
				}else {//**new else- will never be used: because after the TerminateBroadcast change futureAgentAvailableEvent will never be null
					complete(m,null);//and this is because there are always m.p. to resolve the future

				}
			}else {

				complete(m,null);

			}


		};
		subscribeEvent(MissionReceivedEvent.class,missionCall);

	}

}
