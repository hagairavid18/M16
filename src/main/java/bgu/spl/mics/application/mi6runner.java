package bgu.spl.mics.application;

import bgu.spl.mics.JsonParse;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;



import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {

        //---------------jsonParse------------
        Gson gson = new Gson();
        Reader reader = null;

        try {
            reader = new FileReader(args[0]);

        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonParse json = gson.fromJson(reader, JsonParse.class);

        //--------create and Load passive objects ---------------

        Inventory inventory = Inventory.getInstance();
        inventory.load(json.getInventory());
        Squad squad = Squad.getInstance();
        squad.load(json.getSquad());


        //-----------------Initial threads -----------------------

        LinkedList<Thread> threads = new LinkedList<>(); //List of threads

        for (int i =0; i<json.getServices().getIntelligence().length; i++){ //initialize Intelligence
            Intelligence intelligence = new Intelligence(Integer.toString(i),json.getServices().getIntelligence()[i].getMissions());
            Thread t = new Thread(intelligence);
            threads.add(t);
            t.start();
        }
        for (int i =0; i<json.getServices().getM(); i++){//initialize M
            M m = new M(i);
            Thread t = new Thread(m);
            threads.add(t);
            t.start();
        }

        List<String> serials=new LinkedList<>();//creating a list of all serial numbers for Moneypanny
        for(Agent agent:json.getSquad())//it is  necessary for releasing all agents on the last tick
            serials.add(agent.getSerialNumber());
        for (int i =0; i<json.getServices().getMoneypenny(); i++){//initialize Moneypenny
            Moneypenny moneypenny = new Moneypenny(i,serials,json.getServices().getM());//**new constructor
            Thread t = new Thread(moneypenny);
            threads.add(t);
            t.start();
        }
        Q q = new Q();//initialize Q
        Thread t = new Thread(q);
        threads.add(t);
        t.start();

        TimeService timeService = new TimeService(json.getServices().getTime());//initialize Time service
        Thread t1 = new Thread(timeService);
        threads.add(t1);
        t1.start();



        for (Thread thread : threads) { //wait until all subscribes did unregister
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        //-----------------Print File -----------------------


        Diary diary=Diary.getInstance();
        diary.printToFile(args[2]);
        inventory.printToFile(args[1]);

    }

}

