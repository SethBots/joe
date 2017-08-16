package net.swvn9.joe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Config {

    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    private static File dir = new File("config"+File.separator);
    public static File[] dirlist = dir.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
    protected static Map<String,net.swvn9.joe.beans.Guild> guildMap;

    static void print(){
        if(!dir.exists()) dir.mkdir();
        System.out.println("Guilds: "+guildMap.keySet());
    }

    static void load(){
        guildMap = new HashMap<>();
        for(File f:dirlist){
            net.swvn9.joe.beans.Guild hold;
            try {
                hold = mapper.readValue(f, net.swvn9.joe.beans.Guild.class);
                guildMap.put(hold.getId(),hold);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void add(String id){
        String config =
                "#Generated "+ LocalDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ISO_DATE_TIME)+" for "+Bot.jda.getGuildById(id).getName()+"\n\n"+
                "id: "+id+"\n" +
                "owner: "+Bot.jda.getGuildById(id).getOwner().getUser().getId()+"\n" +
                "literal: \"!\"\n" +
                "deleteInvoking: false\n" +
                "roles:\n" +
                "  default:\n" +
                "    #In the default group, the ID doesn't matter, any permissions granted apply to all guild members.\n" +
                "    id: 0\n" +
                "    #Don't set admin to True for the default group (Bad Idea)\n" +
                "    admin: false\n" +
                "    #The permission is the name of the command class. ie. \"list\")\n" +
                "    permissions:\n" +
                "      - name";
        File toAdd = new File(dir+File.separator+id+".yml");
        if(!toAdd.exists()){
            try {
                FileWriter write = new FileWriter(toAdd);
                write.write(config);
                write.close();
                reload();
                print();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void reload(){
        Config.dirlist = dir.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
        load();
        //print();
    }

    public static String show(){
        return "Guilds: "+guildMap.keySet();
    }

    static boolean check(String id){
        for(File f:dirlist){
            if(f.getName().contains(id)) return true;
        }
        return false;
    }
}
