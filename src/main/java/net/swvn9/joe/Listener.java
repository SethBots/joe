package net.swvn9.joe;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("ALL")
public class Listener extends ListenerAdapter{

    @Override
    public void onReady(ReadyEvent e){
        commandChannel.pull("command");
        configChannel.read();
        //Bot.jda.addEventListener(new LogListener());
    }


    @Override
    public void onGenericMessage(GenericMessageEvent e){
        if(e instanceof MessageUpdateEvent || e instanceof MessageDeleteEvent || e instanceof MessageReceivedEvent)
        if(e.getChannel().getId().equals("341669410827796500")){
            commandChannel.pull("command");
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if(e.getAuthor().isBot()||e.getChannel().getType().equals(ChannelType.PRIVATE)) return;

        if(!configChannel.cfg.keySet().contains(e.getGuild().getId())){
            if(null!=Bot.jda.getGuildById("341666084748787712").getMember(e.getAuthor())&&
                    Bot.jda.getGuildById("341666084748787712").getMember(e.getAuthor()).getRoles().contains(Bot.jda.getRoleById("341705215969460224"))){
                if(e.getMessage().getMentionedUsers().contains(Bot.jda.getSelfUser())&&e.getMessage().getRawContent().toLowerCase().contains("config")){
                    configChannel.putnew(e.getGuild().getId(),e.getGuild().getOwner().getUser().getId(),false);
                    System.out.println("added new guild");
                } else {
                    return;
                }
            }
        }

        /*
        String literal = Config.guildMap.get(e.getGuild().getId()).getLiteral();
        Boolean delete = Config.guildMap.get(e.getGuild().getId()).getDeleteInvoking();
        */

        String literal = "!";
        Boolean delete = true;

        if(e.getMessage().getRawContent().startsWith(literal)){
            //Hard-Coded Commands
            try{
                switch (e.getMessage().getRawContent().toLowerCase().replaceFirst(literal,"").split(" ")[0]){

                }
            } catch(Exception ignored){
                return;
            }
            try{
                commandChannel.list.get(e.getMessage().getRawContent().replaceFirst("(i?)"+literal,"").split(" ")[0]).run(e);
            } catch (Exception ex){
                if (!(ex instanceof NullPointerException)) {
                    e.getChannel().sendMessageFormat("```%s```",ex).queue(m->m.delete().queueAfter(10, TimeUnit.MINUTES));
                }
            }
        }
    }
}
