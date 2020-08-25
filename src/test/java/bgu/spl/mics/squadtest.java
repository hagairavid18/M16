package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SquadTest {
    private Squad squad;
    private Agent[] agents;
    private List<String> serials_ls;
    @BeforeEach
    public void setUp(){
        try {
            squad = Squad.getInstance();
        }catch (Exception e){
            fail("Exception "+e);
        }
//        agents=new Agent[]{new Agent("002","BarniTheBeast"),
//                new Agent("004","HagaiTheMachin"),
//                new Agent("005","AmitTheGreat")};
//        serials_ls=new LinkedList<>();
//        serials_ls.add("002");
//        serials_ls.add("004");

        try {
            squad.load(agents);
        }catch (Exception e){
            fail("Exception "+e);
        }
    }

    @Test
    public void testLoad(){

        List<String> lsNames=squad.getAgentsNames(serials_ls);
        assertTrue(lsNames.contains("BarniTheBeast"),"fail- didnt return all agent names");
        assertTrue(lsNames.contains("HagaiTheMachin"),"fail- didnt return all agent names");
        assertFalse(lsNames.contains("AmitTheGreat"),"fail- return agent name no one asked for");

    }


    @Test
    public void testReleaseAgents(){
        try {
            squad.releaseAgents(serials_ls);
        }catch (Exception e){
            fail("Exception"+ e);
        }
        List<String> lsNames=squad.getAgentsNames(serials_ls);
        assertFalse(lsNames.contains("BarniTheBeast"),"fail- didnt return all agent names");
        assertFalse(lsNames.contains("HagaiTheMachin"),"fail- didnt return all agent names");
    }

    @Test
    public void testSendAgents(){
        try {
            squad.sendAgents(serials_ls,10000);
        }catch (Exception e){
            fail("Exception "+e);
        }
        assertFalse(agents[0].isAvailable(),"fail- Barni is on a mission");
        assertFalse(agents[1].isAvailable(),"fail- Hagai is on a mission");
    }

    @Test
    public void testGetAgents(){
        try {
            assertTrue(squad.getAgents(serials_ls));
        }catch (Exception e){
            fail("Exception"+ e);
        }

    }

    @Test
    public void  testGetAgentsNames(){
        List<String> lsNames=new LinkedList<>();
        try {
            lsNames=squad.getAgentsNames(serials_ls);
        }catch (Exception e){
            fail("Exception"+ e);
        }
        assertTrue(lsNames.contains("BarniTheBeast"));
        assertTrue(lsNames.contains("HagaiTheMachin"));

    }

}
