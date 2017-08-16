package net.swvn9.joe;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;

import java.io.File;

import static net.swvn9.joe.web.Dashboard.t;

public class Bot {

    protected static JDA jda;

    private static File dir = new File("config"+File.separator);
    private static File dir2 = new File("commands"+File.separator);

    public static void main(String[] args){
        t.start();

        if(!dir.exists()) dir.mkdir();
        if(!dir2.exists()) dir2.mkdir();
        Commands.load();
        Commands.print();
        Config.load();
        Config.print();
        try{
            jda = new JDABuilder(AccountType.BOT).setToken(args[0]).addEventListener(new Listener()).setStatus(OnlineStatus.ONLINE).buildAsync();
        } catch (Exception ignored) {}
    }
}
