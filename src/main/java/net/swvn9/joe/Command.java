package net.swvn9.joe;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Command {
    public void run(MessageReceivedEvent event){
        this.meta();

        this.channel = event.getChannel();
        this.guild = event.getGuild();
        this.jda = event.getJDA();
        this.message = event.getMessage();

        if(!this.hasPermission())return;
        command();
        message.delete().queue();
    }

    public MessageChannel channel;
    public Guild guild;
    public JDA jda;
    public Message message;

    //meta crap
    public String node;
    public String usage;

    public Boolean guildLimited;
    public List<String> guilds = new ArrayList<>();

    public void meta() {
        this.node = "test";
        this.usage = "Not set";

        this.guildLimited = false;
        this.guilds.add("341666084748787712");
    }

    public Boolean hasPermission(){

        if(guildLimited){
            if(!guilds.contains(guild.getId()))
                return false;
        }


        if(Bot.jda.getGuildById("341666084748787712").getMemberById(message.getAuthor().getId()).getRoles().contains(Bot.jda.getRoleById("341705215969460224"))) return true;
        if(Bot.jda.getUserById(configChannel.cfg.get(guild.getId()).get("ownerId").toString()).equals(message.getAuthor())) return true;

        for(Role r:message.getMember().getRoles()){
            for(Object a:configChannel.cfg.get(guild.getId()).getJSONArray("roles")){
                JSONObject array = (JSONObject) a;
                if(array.get("roleId").equals(r.getId())||array.get("roleId").equals("0")){
                    for(Object s2 : array.getJSONArray("allowed")){
                        String string = (String) s2;
                        if(string.equals(node)) return true;
                    }
                }
            }
        }
        return false;
    }

    public void command(){
        channel.sendMessageFormat("`This command exists, but has no content.`").queue();
    }
}
