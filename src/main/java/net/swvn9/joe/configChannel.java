package net.swvn9.joe;

import jdk.nashorn.internal.parser.JSONParser;
import net.dv8tion.jda.core.entities.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class configChannel {
    public static Map<String,JSONObject> cfg = new HashMap<>();
    public static JSONObject defaultCfg = new JSONObject()
            .put("guildId","")
            .put("guildName","")
            .put("ownerId","")
            .put("deleteInvoking",false)
            .put("roles",
                    new JSONArray().put(
                            new JSONObject()
                                    .put("name","default")
                                    .put("roleId","0")
                                    .put("allowed",new JSONArray()
                                            .put("test1")
                                    )
                    )
            );

    public static void test(){
        cfg.put("one",defaultCfg);
        cfg.replace("one",cfg.get("one").put("guildId","asdasda"));
        System.out.println(cfg.get("one").toString(3));
        Bot.jda.getTextChannelById("341668341108310016").sendMessageFormat("```json\n%s\n```",cfg.get("one").toString(3)).queue();
    }

    public static String keys(){
        return cfg.keySet().toString();
    }

    public static void putnew(String guildId,String ownerId,boolean deleteInvoking){
        Bot.jda.getTextChannelById("341668341108310016").sendMessage(
                "```json\n"+
                new JSONObject()
                        .put("guildId",guildId)
                        .put("guildName",Bot.jda.getGuildById(guildId).getName())
                        .put("ownerId",ownerId)
                        .put("deleteInvoking",deleteInvoking)
                        .put("roles",
                                new JSONArray().put(
                                        new JSONObject()
                                                .put("name","default")
                                                .put("roleId","0")
                                                .put("allowed",new JSONArray()
                                                        .put("test1")
                                                )
                                )
                        ).toString(3)
                +"\n```"
        ).queue();
        read();
    }

    public static void read(){
        List<Message> msgs = Bot.jda.getTextChannelById("341668341108310016").getIterableHistory().complete().stream().filter(m->m.getAuthor().equals(Bot.jda.getSelfUser())).collect(Collectors.toList());
        cfg.clear();
        for(Message m:msgs){
            if(m.getContentRaw().contains("```json")){
                JSONObject hold = new JSONObject(m.getContentRaw().replaceAll("```(json)?",""));
                cfg.put(hold.getString("guildId"),hold);
            }
        }
    }
}
