package net.swvn9.joe;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class Listener extends ListenerAdapter{

    @Override
    public void onReady(ReadyEvent e){
        commandChannel.pull("command");
        configChannel.read();
        //System.out.println("Session commands loaded.");
    }


    @Override
    public void onGenericMessage(GenericMessageEvent e){
        if(e instanceof MessageUpdateEvent || e instanceof MessageDeleteEvent || e instanceof MessageReceivedEvent)
        if(e.getChannel().getId().equals("341669410827796500")){
            commandChannel.pull("command");
            //System.out.println("Session commands reloaded due to new message/edit/delete");
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if(e.getAuthor().isBot()||e.getChannel().getType().equals(ChannelType.PRIVATE)) return;

        if(!configChannel.cfg.keySet().contains(e.getGuild().getId())){
            if(null!=Bot.jda.getGuildById("341666084748787712").getMember(e.getAuthor())&&
                    Bot.jda.getGuildById("341666084748787712").getMember(e.getAuthor()).getRoles().contains(Bot.jda.getRoleById("341705215969460224"))){
                if(e.getMessage().getMentionedUsers().contains(Bot.jda.getSelfUser())&&e.getMessage().getContentRaw().toLowerCase().contains("config")){
                    configChannel.putnew(e.getGuild().getId(),e.getGuild().getOwner().getUser().getId(),false);
                    System.out.println("added new guild");
                } else {
                    return;
                }
            }
        }

        /*
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
        */

        String literal = "!";
        Boolean delete = true;

        if(e.getMessage().getContentRaw().startsWith(literal)){
            //Hard-Coded Commands
            try{
                switch (e.getMessage().getContentRaw().toLowerCase().replaceFirst(literal,"").split(" ")[0]){
                    /*case "dump":
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
                        return;*/
                }
            } catch(Exception ignored){
                return;
            }
            try{
                commandChannel.list.get(e.getMessage().getContentRaw().replaceFirst("(i?)"+literal,"").split(" ")[0]).run(e);
            } catch (Exception ex){
                if (!(ex instanceof NullPointerException)) {
                    e.getChannel().sendMessageFormat("```%s```",ex).queue(m->m.delete().queueAfter(10, TimeUnit.MINUTES));
                }
            }
        }
    }
}
