package bgu.spl.mics;

import bgu.spl.mics.application.messages.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private ConcurrentHashMap<Subscriber, BlockingQueue<Message>> subscriber_map;
	private ConcurrentHashMap<Class<? extends Message >, ConcurrentLinkedQueue<Subscriber>> eventQueue_map;
	private ConcurrentHashMap<Class<? extends Message >,ConcurrentLinkedQueue<Subscriber>> broadcastList_map;
	private ConcurrentHashMap<Event,Future> futureMap;

	/**
	 * Retrieves the single instance of this class.
	 */
	private static class MessageBrokerHolder{
		private static MessageBrokerImpl instance=new MessageBrokerImpl();
	}

	/**
	 contractor
	 sets all the broadcast and events queues
	 */
	private MessageBrokerImpl() {
		this.subscriber_map = new ConcurrentHashMap<>();
		this.eventQueue_map = new ConcurrentHashMap<>();
		eventQueue_map.put(AgentAvailableEvent.class,new ConcurrentLinkedQueue<>());
		eventQueue_map.put(GadgetAvaliableEvent.class, new ConcurrentLinkedQueue<>());
		eventQueue_map.put(MissionReceivedEvent.class, new ConcurrentLinkedQueue<>());
		eventQueue_map.put(ExecuteMissionEvent.class, new ConcurrentLinkedQueue<>());
		this.broadcastList_map = new ConcurrentHashMap<>();
		broadcastList_map.put(TickBroadcast.class,new ConcurrentLinkedQueue<>());
		this.futureMap = new ConcurrentHashMap<>();
	}
	/**
	 * @return m.b. instance
	 */
	public static MessageBroker getInstance() {
		return MessageBrokerHolder.instance;
	}
	/**
	 * insert a Subscriber to event queue
	 * @param type of event
	 * @param m the Subscriber wants to be added to the event type queue
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		synchronized (this) {

			if (eventQueue_map.get(type) != null)
				eventQueue_map.get(type).add(m);//add subscriber to type's queue
			else {
				eventQueue_map.put(type, new ConcurrentLinkedQueue<>());
				eventQueue_map.get(type).add(m);
			}
		}
	}
	/**
	 * insert a Subscriber to broadcast queue
	 * @param type of broadcast
	 * @param m the Subscriber wants to be added to the broadcast type queue
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		synchronized (this) {
			if (broadcastList_map.get(type)!=null) {
				broadcastList_map.get(type).add(m);

			} else {
				broadcastList_map.put(type, new ConcurrentLinkedQueue<>());
				broadcastList_map.get(type).add(m);
			}
		}

	}
	/**
	 * solve an event
	 * @param e the event needed to be solved
	 * @param result the result of the event
	 */
	@Override
	public <T> void  complete(Event<T> e, T result) {
		synchronized (subscriber_map) {
			Future ret = futureMap.get(e); //search for the future that needs to be returned

			ret.resolve(result);
			futureMap.remove(e);
		}
	}
	/**
	 * sent a broadcast to all interested subjects
	 * @param b the broadcast to send
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (subscriber_map) { //protects from deleting and inserting at the same time
			if (broadcastList_map.get(b.getClass()) != null)
				for (Subscriber subscriber : broadcastList_map.get(b.getClass())) {
					BlockingQueue<Message> queue = subscriber_map.get(subscriber);//insert the massage to every subscriber who wants to receive it
					queue.add(b);
				}
		}
	}

	/**
	 * sent a event to the subscriber which is at the top of the queue
	 * @param e the event to send
	 */
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> newFuture = null;
		Queue<Subscriber> currentEventQueue = eventQueue_map.get(e.getClass());
		if (currentEventQueue == null || currentEventQueue.isEmpty()) {
			return newFuture;

		}//if this type of event is well known, and there are subscribers who will handle it do:
		Subscriber executiveSubscriber;
		synchronized (currentEventQueue) {

			executiveSubscriber = currentEventQueue.poll();//remove subscriber from the head of the queue
			currentEventQueue.add(executiveSubscriber);//put back the subscriber in the back of the queue
		}
		BlockingQueue<Message> executiveSubscriberQueue = subscriber_map.get(executiveSubscriber);
		if (executiveSubscriberQueue!=null) {
			synchronized (executiveSubscriberQueue) {
				if (executiveSubscriberQueue != null) {

					newFuture = new Future<>();
					futureMap.put(e, newFuture);
					try {
						executiveSubscriberQueue.put(e);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
			}
		}

		return newFuture;
	}
	/**
	 * open an events queue for a certain subscriber
	 * @param m the subscriber who want to register
	 */
	@Override
	public void register(Subscriber m) {
		subscriber_map.put(m, new LinkedBlockingQueue<>());
	}
	/**
	 * solve all subscriber's events and remove the subscriber from all events queues
	 * @param m the subscriber who want to unregister
	 */
	@Override
	public void unregister(Subscriber m) {
		for (ConcurrentLinkedQueue concurrentLinkedQueue : eventQueue_map.values()){ //remove my self from all topics
			concurrentLinkedQueue.remove(m);
		}
		for (ConcurrentLinkedQueue concurrentLinkedQueue : broadcastList_map.values()){ //remove my self from all broadcast
			concurrentLinkedQueue.remove(m);
		}
		synchronized (subscriber_map.get(m)) { //ensure that no one will try to add any message to my queue
			for (Message message : subscriber_map.get(m)) { //clean my queue gracefully and resolve all futures.
				if (message instanceof Event)
					complete((Event) message, null);
			}
			subscriber_map.remove(m);
		}
	}
	/**
	 * checks if there is ant messages waiting for the subscriber
	 * @param m the subscriber
	 */
	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		if (subscriber_map.get(m)==null)
			return null;
		return subscriber_map.get(m).take();
	}

	

}
