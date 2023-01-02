package playground.stianst.github.io;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MyFlakyTest {

    @Test
    public void flaky() {
        assertEquals(1, Count.getAndIncrease());
    }

}
