package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    private Inventory inventory;
    private String[] to_load;

    @BeforeEach
    public void setUp(){
        inventory = Inventory.getInstance();
        to_load=new String[]{"elder_wand","invisible_cloak","philosopher_stone"};
    }


    @Test
    public  void testLoad(){
        try {
            inventory.load(to_load);
        }
        catch (Exception e){
            fail("Exception"+e);
        }
        assertTrue(inventory.getItem("elder_wand"));
        assertFalse(inventory.getItem("lazerSock"));
        assertFalse(inventory.getItem("happy_hippo"));

    }
    @Test
    public void testGetItem(){
        try{
            inventory.load(to_load);
            assertTrue(inventory.getItem("elder_wand"));
            assertFalse(inventory.getItem("elder_wand"));
            assertFalse(inventory.getItem("pup"));
        }
        catch (Exception e){
            fail("Exception"+ e);
        }
    }


}
