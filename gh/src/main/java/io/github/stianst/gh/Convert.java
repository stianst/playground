package io.github.stianst.gh;

import java.util.Arrays;

public class Convert {

    static String input = "7m 48s\n" +
            "9m 14s\n" +
            "28m 29s\n" +
            "20m 28s\n" +
            "27m 42s\n" +
            "23m 48s\n" +
            "30m 17s\n" +
            "2m 23s\n" +
            "5m 22s\n" +
            "16m 0s\n" +
            "10m 40s\n" +
            "17m 59s\n" +
            "16m 55s\n" +
            "23m 15s\n" +
            "22m 17s\n" +
            "7m 29s\n" +
            "6m 26s\n" +
            "7m 26s\n" +
            "7m 26s\n" +
            "12m 41s\n" +
            "6m 17s\n" +
            "26m 37s\n" +
            "29m 31s\n" +
            "12m 20s\n" +
            "3m 14s\n" +
            "3m 11s\n" +
            "48m 23s\n" +
            "59m 31s\n" +
            "1h 15m 33s\n" +
            "40m 41s\n" +
            "4m 35s\n" +
            "9m 26s";

    public static void main(String[] args) {
        Arrays.stream(input.split("\\n")).forEach(s -> {
            String[] split = s.split(" ");
            Integer hours = 0;
            Integer min;
            Integer sec;
            if (split.length == 2) {
                min = Integer.valueOf(split[0].replace("m", ""));
                sec = Integer.valueOf(split[1].replace("s", ""));
            } else {
                hours = Integer.valueOf(split[0].replace("h", ""));
                min = Integer.valueOf(split[1].replace("m", ""));
                sec = Integer.valueOf(split[2].replace("s", ""));
            }
            System.out.format("%02d:%02d:%02d\n", hours, min, sec);
        });
    }

}
