package net.swvn9.joe;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;

@SuppressWarnings("ALL")
public class Bot {

    static JDA jda;

    public static void main(String[] args){
        //t.start();
        try{
            jda = new JDABuilder(AccountType.BOT).setToken(args[0]).addEventListener(new Listener()).setStatus(OnlineStatus.ONLINE).buildAsync();
        } catch (Exception ignored) {}
    }
}
