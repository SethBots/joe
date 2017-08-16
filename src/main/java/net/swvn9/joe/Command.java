package net.swvn9.joe;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class Command {
    public void run(MessageReceivedEvent event){
        this.channel = event.getChannel();
        this.cfg  = Config.guildMap.get(event.getGuild().getId());
        this.guild = event.getGuild();
        this.jda = event.getJDA();
        this.message = event.getMessage();

        command();
    }

    public net.swvn9.joe.beans.Guild cfg;
    public MessageChannel channel;
    public Guild guild;
    public JDA jda;
    public Message message;

    public void command(){
        channel.sendMessageFormat("`This command exists, but has no content.`").queue();
    }
}
