package playground.stianst.github.io;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

public class MyFlakyTest {

    @Test
    public void flaky() {
        Random random = new Random();
        int i = random.nextInt(10);
        assertTrue(i < 2);
    }

    @Test
    public void flaky2() {
        Random random = new Random();
        int i = random.nextInt(10);
        assertTrue(i < 2);
    }

}
