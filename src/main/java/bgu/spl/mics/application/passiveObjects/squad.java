package bgu.spl.mics.application.passiveObjects;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents ;

	/**
	 * Retrieves the single instance of this class.
	 */
	private static class SquadHolder{
		private static Squad instance=new Squad();
	}
	private Squad(){
		agents=new ConcurrentHashMap<>();
	}
	public static Squad getInstance() {
		return SquadHolder.instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for(Agent agent:agents){
			this.agents.put(agent.getSerialNumber(),agent);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		for (String serial:serials){
			Agent agentToRelease=agents.get(serial);
			synchronized (agentToRelease) {
				agentToRelease.release();
				agentToRelease.notifyAll();
			}
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		try {
			Thread.sleep(100*time,0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		releaseAgents(serials);//after we finish our mission we release all agents we used
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials){

		boolean areExist=true;
		for (String serial:serials) {//check if all agents exist in the squad
			if (this.agents.get(serial) == null) {
				areExist = false;
				return areExist;
			}
		}
		bubbleSort(serials);//we avoid deadlocks by locking all the agents we need in a certain order
		for (String serial:serials) {//if all agents are exist then we will try to acquire them
			Agent agentToAcquire = agents.get(serial);
			synchronized (agentToAcquire) {
				while (!agentToAcquire.isAvailable()) {
					try {
						agentToAcquire.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				agentToAcquire.acquire();
			}
		}
		return areExist;
	}

	private void bubbleSort(List<String > list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 0; j < list.size() - i - 1; j++) {
				if (list.get(j).compareTo(list.get(j + 1)) > 0) {
					//swap
					String temp = list.get(j);
					list.set(j, list.get(j + 1));
					list.set(j + 1, temp);
				}
			}
		}
	}



    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
        List<String> agentsNames=new LinkedList<>();
        for (String serial:serials){
        	agentsNames.add(agents.get(serial).getName());
		}
        return agentsNames;
    }

}
