package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	private int curr_time;
	private int duration;

	/**
	 default contractor
	 */
	public TimeService() {
		super("timeService");
	}

	/**
	 contractor
	 @param duration the program duration
	 */
	public TimeService(int duration){
		super("timeService");
		this.duration=duration;
		curr_time = 0;
	}
	/**
	 initialize the clock
	 */
	@Override
	protected void initialize() {
	}
	/**
	 * The entry point of the TimeService.
	 */
	@Override
	public void run() {

		while (curr_time<duration){
			Broadcast timeBroadcast=new TickBroadcast(curr_time,false);
			getSimplePublisher().sendBroadcast(timeBroadcast);
			try {
				Thread.sleep(100,0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			curr_time++;
		}
		Broadcast timeBroadcast=new TickBroadcast(curr_time,true);
		getSimplePublisher().sendBroadcast(timeBroadcast);
	}

}
