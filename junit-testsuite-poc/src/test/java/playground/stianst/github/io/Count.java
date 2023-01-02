package playground.stianst.github.io;

public class Count {

    private static int count = 0;

    public synchronized static int getAndIncrease() {
        int c = count;
        count++;
        return c;
    }

}
