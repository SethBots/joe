package net.swvn9.joe;

import com.sun.org.apache.xpath.internal.operations.Bool;
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
            .put("ownerId","")
            .put("deleteInvoking",false)
            .put("roles",
                    new JSONArray().put(
                            new JSONObject()
                                    .put("roleId","0")
                                    .put("allowed",new JSONArray()
                                            //.put("test")
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
                        .put("ownerId",ownerId)
                        .put("deleteInvoking",deleteInvoking)
                        .put("roles",
                                new JSONArray().put(
                                        new JSONObject()
                                                .put("roleId","0")
                                                .put("allowed",new JSONArray()
                                                        //.put("test")
                                                )
                                )
                        ).toString(3)
                +"\n```"
        ).queue();
        read();
    }

    public static void read(){
        List<Message> msgs = Bot.jda.getTextChannelById("341668341108310016").getIterableHistory().complete().stream().filter(m->m.getAuthor().equals(Bot.jda.getSelfUser())||m.getAuthor().equals(Bot.jda.getUserById("269904635337113603"))).collect(Collectors.toList());
        cfg.clear();
        for(Message m:msgs){
            if(m.getContentRaw().contains("```json")){
                JSONObject hold = new JSONObject(m.getContentRaw().replaceAll("```(json)?",""));
                cfg.put(hold.getString("guildId"),hold);
            }
        }
    }



    public static void addRole(String guild,String role,String perm){
        JSONObject backup = cfg.get(guild);
        Boolean contains = false;
        for(Object j:cfg.get(guild).getJSONArray("roles")){
            JSONObject s = (JSONObject) j;
            if(s.get("roleId").equals(role)){
                contains=true;
                s.getJSONArray("allowed").put(perm);
                break;
            }

        }
        if(!contains){
            cfg.get(guild).getJSONArray("roles").put(new JSONObject()
                    .put("roleId",role)
                    .put("allowed",new JSONArray().put(perm))
            );
        }
        List<Message> msgs = Bot.jda.getTextChannelById("341668341108310016").getIterableHistory().complete().stream().filter(m->m.getAuthor().equals(Bot.jda.getSelfUser())||m.getAuthor().equals(Bot.jda.getUserById("269904635337113603"))).collect(Collectors.toList());
        for(Message m:msgs){
            if(m.getContentRaw().contains(guild)){
                m.delete().queue();
                break;
            }
        }

        if(("```json\n"+cfg.get(guild).toString(3)+"\n```").length()>1999){
            Bot.jda.getTextChannelById("341668341108310016").sendMessage("```json\n"+backup.toString(3)+"\n```").queue();
            System.out.println("Config is too long");
        } else {
            Bot.jda.getTextChannelById("341668341108310016").sendMessage("```json\n"+cfg.get(guild).toString(3)+"\n```").queue();
        }
    }

    public static void removeRole(String guild,String role){
        JSONArray array = cfg.get(guild).getJSONArray("roles");

        int j=0;
        for(Object o:array){
            JSONObject obj = (JSONObject) o;
            if(obj.get("roleId").equals(role)){
                cfg.get(guild).getJSONArray("roles").remove(j);
                break;
            }
            j++;
            if(j>=array.length()) break;
        }

        List<Message> msgs = Bot.jda.getTextChannelById("341668341108310016").getIterableHistory().complete().stream().filter(m->m.getAuthor().equals(Bot.jda.getSelfUser())||m.getAuthor().equals(Bot.jda.getUserById("269904635337113603"))).collect(Collectors.toList());
        for(Message m:msgs){
            if(m.getContentRaw().contains(guild)){
                m.delete().queue();
                break;
            }
        }
        Bot.jda.getTextChannelById("341668341108310016").sendMessage("```json\n"+cfg.get(guild).toString(3)+"\n```").queue();

    }

    public static void removePerm(String guild,String role,String perm){
        JSONArray array;
        int u = 0;
        for(Object o:cfg.get(guild).getJSONArray("roles")){
            JSONObject ob = (JSONObject) o;
            if(((JSONObject) o).get("roleId").equals(role)){
                array = ob.getJSONArray("allowed");

                int y = 0;
                for(Object o2:array){
                    String s = (String) o2;
                    if(s.equals(perm)){
                        array.remove(y);
                        cfg.get(guild).getJSONArray("roles").remove(u);
                        ob.remove("allowed");
                        ob.put("allowed",array);
                        cfg.get(guild).getJSONArray("roles").put(ob);
                    }
                    y++;
                    if(y>=array.length()) break;
                }



                break;
            }
            u++;
        }
        List<Message> msgs = Bot.jda.getTextChannelById("341668341108310016").getIterableHistory().complete().stream().filter(m->m.getAuthor().equals(Bot.jda.getSelfUser())||m.getAuthor().equals(Bot.jda.getUserById("269904635337113603"))).collect(Collectors.toList());
        for(Message m:msgs){
            if(m.getContentRaw().contains(guild)){
                m.delete().queue();
                break;
            }
        }
        Bot.jda.getTextChannelById("341668341108310016").sendMessage("```json\n"+cfg.get(guild).toString(3)+"\n```").queue();
    }

}
