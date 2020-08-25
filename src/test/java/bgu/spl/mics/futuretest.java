package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    private  Future<String> future;
    private String result;
    @BeforeEach
    public void setUp(){
        try {
            future= new Future<>();
            result="pending";
        }catch (Exception e){
            fail("Exception "+e);
        }

    }

    @Test
    public void testResolve(){
        try {
           future.resolve("very good");
        }catch (Exception e){
            fail("Exception "+e);
        }
        assertEquals("very good",future.get());
    }

    @Test
    public void testGet1(){
        try {
            future.resolve(result);
            assertEquals(result,future.get());

        }catch (Exception e){
            fail("Exception "+e);
        }
    }

    @Test
    public void testGet2(){
        try {
            assertEquals(null,future.get(1000, TimeUnit.MILLISECONDS));
            Thread.sleep(1000);
            future.resolve(result);
            assertEquals(result,future.get(1000, TimeUnit.MILLISECONDS));

        }catch (Exception e){
            fail("Exception "+e);
        }
    }

    @Test
    public void testsDone(){
        try {
            assertFalse(future.isDone());
            future.resolve(result);
            assertTrue(future.isDone());

        }catch (Exception e){
            fail("Exception "+e);
        }
    }

}
