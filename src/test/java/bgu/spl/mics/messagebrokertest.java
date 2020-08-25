package bgu.spl.mics;

import bgu.spl.mics.application.messages.AgentAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvaliableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MessageBrokerTest {

    private MessageBroker messageBroker;
    private Subscriber subscriberM;
    private Subscriber subscriberQ;
    private Subscriber subscriberPenny;
    private MissionReceivedEvent eventM;
    private Event eventQ;
    private Event eventPenny;
    private Event ex;



    @BeforeEach
    public void setUp(){
        try {
            messageBroker=MessageBrokerImpl.getInstance();
            subscriberM=new M(1);
            subscriberQ=new Q();
            subscriberPenny=new Moneypenny(1,null,5);

            eventM=new MissionReceivedEvent();
            eventQ=new GadgetAvaliableEvent(null);
            eventPenny= new AgentAvailableEvent(null,1);

            ex=new ExampleEvent("amit");

        }catch (Exception e){
            fail("Exception "+e);
        }


    }



    @Test
    public void testSubscribeEvent(){
        try {
            messageBroker.register(subscriberM);
         //   messageBroker.subscribeEvent(MissionReceivedEvent.class,subscriberM);
            messageBroker.sendEvent(eventM);
            Message mess = messageBroker.awaitMessage(subscriberM);
            assertTrue(mess!=null);

        }catch (Exception e){
            fail("Exception "+e);
        }
    }

    @Test
    public void testRegister(){
        try {
            messageBroker.register(subscriberM);
            messageBroker.awaitMessage(subscriberM);


        }catch (IllegalStateException e){
            fail("subscriber was never registered "+e);
        }
        catch (Exception e){
            fail("Exception "+e);
        }
    }

    @Test
    public  void testUnRegister(){
        try {
            messageBroker.register(subscriberM);
            messageBroker.unregister(subscriberM);
            messageBroker.awaitMessage(subscriberM);


        }catch (IllegalStateException | InterruptedException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            fail("Exception "+e);
        }
        fail("No exception thrown although excepted");
    }


}
