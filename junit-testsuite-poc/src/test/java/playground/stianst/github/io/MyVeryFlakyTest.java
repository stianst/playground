package playground.stianst.github.io;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

public class MyVeryFlakyTest {

    @Test
    public void flaky() {
        Random random = new Random();
        int i = random.nextInt(10);
        assertTrue(i < 5);
    }

}
