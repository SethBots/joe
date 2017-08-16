package net.swvn9.joe;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static net.swvn9.joe.Config.dirlist;

public class Listener extends ListenerAdapter{

    @Override
    public void onReady(ReadyEvent e){
        masterConfig.pull("command");
        //System.out.println("Session commands loaded.");
    }


    @Override
    public void onGenericMessage(GenericMessageEvent e){
        if(e instanceof MessageUpdateEvent || e instanceof MessageDeleteEvent || e instanceof MessageReceivedEvent)
        if(e.getChannel().getId().equals("341669410827796500")){
            masterConfig.pull("command");
            //System.out.println("Session commands reloaded due to new message/edit/delete");
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if(e.getAuthor().isBot()) return;
        if(!Config.check(e.getGuild().getId())) {
            if(e.getMessage().getMentionedUsers().contains(Bot.jda.getSelfUser())&&
                    e.getMessage().getContentRaw().contains("config")&&
                    e.getAuthor().getId().equals("111592329424470016")){
                Config.add(e.getGuild().getId());
                e.getChannel().sendMessageFormat("Registered & configured %s.\n`%s`",e.getGuild().getName(),e.getGuild().getId()).queue(m->m.delete().queueAfter(10, TimeUnit.MINUTES));
                return;
            } else {
                return;
            }
        }
        String literal = Config.guildMap.get(e.getGuild().getId()).getLiteral();
        Boolean delete = Config.guildMap.get(e.getGuild().getId()).getDeleteInvoking();

        if(e.getMessage().getContentRaw().startsWith(literal)){
            //Hard-Coded Commands
            try{
                switch (e.getMessage().getContentRaw().toLowerCase().replaceFirst(literal,"").split(" ")[0]){
                    case "reload":
                        String s;
                        if((s = e.getMessage().getContentRaw().toLowerCase().replaceFirst(literal,"").split(" ")[1])!=null){
                            if((s = Commands.reload(s))!=null){
                                e.getChannel().sendMessageFormat("%s.",s).queue(m->m.delete().queueAfter(1, TimeUnit.MINUTES));
                            } else {
                                Commands.reload();
                                Config.reload();
                                e.getChannel().sendMessageFormat("Reloaded all commands & guilds from file.").queue(m->m.delete().queueAfter(1, TimeUnit.MINUTES));
                            }
                            if(delete) e.getMessage().delete().queue();
                        }
                        return;
                    case "c":
                        try{
                            masterConfig.list.get(e.getMessage().getContentRaw().replaceFirst("(i?)"+literal+"c ","").split(" ")[0]).run(e);
                            if(delete) e.getMessage().delete().queue();
                        } catch (Exception ex){
                            if (!(ex instanceof NullPointerException)) {
                                e.getChannel().sendMessageFormat("```%s```",ex).queue(m->m.delete().queueAfter(10, TimeUnit.MINUTES));
                            }                        }
                        return;
                    case "dump":
                        for(String str:Config.guildMap.keySet()){
                            net.swvn9.joe.beans.Guild g=Config.guildMap.get(str);
                            Color guild = Color.decode("#2C2F33");
                            if(g.getId().equals(e.getGuild().getId())){
                                guild = Color.decode("#7289DA");
                            }
                            StringBuilder roles = new StringBuilder();
                            for(String ro:g.getRoles().keySet()){
                                StringBuilder perms = new StringBuilder();
                                g.getRoles().get(ro).permissions.forEach(s1 -> perms.append(s1).append(","));
                                roles
                                        .append("**")
                                        .append(ro)
                                        .append("** (")
                                        .append(g.getRoles().get(ro).id)
                                        .append(")\ta:")
                                        .append(g.getRoles().get(ro).admin)
                                        .append("\tp:")
                                        .append(perms)
                                        .append("\n");
                            }
                            e.getChannel().sendMessage(new EmbedBuilder()
                                    .addField("Guild ID",g.getId(),true)
                                    .addField("Owner ID",g.getOwner(),true)
                                    .addField("Literal",g.getLiteral(),true)
                                    .addField("Delete",g.getDeleteInvoking().toString(),true)
                                    .addField("Roles",roles.toString(),false)
                                    .setColor(guild)
                                    .setTitle(Bot.jda.getGuildById(g.getId()).getName())
                                    .build()
                            ).queue();
                        }
                        return;
                    case "g":
                        String arguments[] = e.getMessage().getContentRaw().split(" ");
                        if(arguments.length>1){
                            if(arguments[1].equalsIgnoreCase("edit")){

                                for(File f : Config.dirlist){
                                    if(f.getName().equals(arguments[2]+".yml")){
                                        if(arguments[3]!=null){
                                            System.out.println("edit "+arguments[2]);
                                            FileReader reader = new FileReader(f);
                                            Scanner scan = new Scanner(reader);
                                            StringBuilder lines = new StringBuilder();
                                            List<String> config = new ArrayList<>();
                                            while (scan.hasNextLine()) config.add(scan.nextLine());
                                            String file[] = (String[]) config.toArray();
                                        }
                                    }
                                }
                            } else {
                                for(File f : Config.dirlist){
                                    if(f.getName().equals(arguments[1]+".yml")){
                                        FileReader reader = new FileReader(f);
                                        Scanner scan = new Scanner(reader);
                                        StringBuilder lines = new StringBuilder();
                                        while (scan.hasNextLine()) lines.append(scan.nextLine()).append("\n");
                                        e.getChannel().sendMessageFormat("```yaml\n%s\n```",lines.toString()).queue();
                                    }
                                }
                            }
                        }
                        return;
                }
            } catch(Exception ignored){
                return;
            }
            //Runtime Commands
            try{
                Commands.list.get(e.getMessage().getContentRaw().replaceFirst("(i?)"+literal,"")).run(e);
                if(delete) e.getMessage().delete().queue();
            } catch (Exception ex){
                if (!(ex instanceof NullPointerException)) {
                    e.getChannel().sendMessageFormat("```%s```",ex).queue(m->m.delete().queueAfter(10, TimeUnit.MINUTES));
                }
            }
        }
    }
}
