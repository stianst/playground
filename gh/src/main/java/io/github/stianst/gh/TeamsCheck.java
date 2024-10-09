package io.github.stianst.gh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TeamsCheck {

    static Map<String, List<String>> users = loadUsers();
    static Set<String> googleUsers = loadGoogleUsers();

    public static void main(String[] args) {

        System.out.println("Not in google:");
        users.keySet().stream().filter(u -> !googleUsers.contains(u)).forEach(TeamsCheck::printUser);

        System.out.println();

        System.out.println("Not in github:");
        googleUsers.stream().filter(u -> !users.containsKey(u)).forEach(TeamsCheck::printUser);
    }

    private static Map<String, List<String>> loadUsers() {
        try {
            File teamsFile = new File("teams.txt");
            BufferedReader br = new BufferedReader(new FileReader(teamsFile));
            Map<String, List<String>> users = new HashMap<>();
            for (String l = br.readLine(); l != null; l = br.readLine()) {
                String[] split = l.split("\t");
                users.put(split[0], Arrays.stream(split[1].split(", ")).toList());
            }
            return users;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Set<String> loadGoogleUsers() {
        try {
            File googleTeamsFile = new File("google-teams.txt");

            Set<String> users = new HashSet<>();
            BufferedReader br = new BufferedReader(new FileReader(googleTeamsFile));
            for (String l = br.readLine(); l != null; l = br.readLine()) {
                users.add(l);
            }
            return users;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printUser(String user) {
        List<String> teams = users.get(user);
        if (teams != null && !teams.isEmpty()) {
            System.out.println(" - " + user + ": " + teams.stream().sorted().collect(Collectors.joining(", ")));
        } else {
            System.out.println(" - " + user + ": NO-TEAM");
        }
    }

}
