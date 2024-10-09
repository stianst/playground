package io.github.stianst.gh;

import io.github.stianst.gh.utils.Client;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHTeam;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Teams {

    public static void main(String[] args) throws Exception {
        File file = new File("teams.txt");
        if (file.isFile()) {
            file.delete();
        }
        PrintWriter pw = new PrintWriter(new FileWriter(file));

        GitHub gitHub = Client.getInstance().gitHub();

        GHOrganization organization = gitHub.getOrganization("keycloak");

        LinkedHashMap<String, List<String>> users = new LinkedHashMap<>();

        for (GHUser user : organization.listMembers().toList()) {
            users.put(user.getLogin(), new LinkedList<>());
        }

        Map<String, GHTeam> teams = organization.getTeams();
        for (GHTeam team : teams.values()) {
            for (GHUser u : team.getMembers()) {
                users.get(u.getLogin()).add(team.getName());
            }
        }

        for (String user : users.keySet().stream().sorted().toList()) {
            pw.println(user + "\t" + users.get(user).stream().sorted().collect(Collectors.joining(", ")));
        }

        System.out.println(file.getAbsolutePath());

        pw.flush();
        pw.close();
    }

}
