package net.swvn9.joe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class utilities {


}

class convert{

    private static final Pattern id = Pattern.compile("(\\d*)");

    private static final Pattern user = Pattern.compile("<@(\\d*)>");
    private static final Pattern nick = Pattern.compile("<@!(\\d*)>");
    private static final Pattern role = Pattern.compile("<@&(\\d*)>");
    private static final Pattern channel = Pattern.compile("<#(\\d*)>");

    private static final Pattern emote = Pattern.compile("<:\\w*:(\\d*)>");

    public String type = "";
    public String out = "";

    convert(java.lang.String s){
        Matcher m = null;

        m = user.matcher(s);
        if(m.find()){
            this.out = m.group(1);
            this.type = "user";
            return;
        }

        m = nick.matcher(s);
        if(m.find()){
            this.out = m.group(1);
            this.type = "nick";
            return;
        }

        m = role.matcher(s);
        if(m.find()){
            this.out = m.group(1);
            this.type = "role";
            return;
        }

        m = channel.matcher(s);
        if(m.find()){
            this.out = m.group(1);
            this.type = "channel";
            return;
        }

        m = emote.matcher(s);
        if(m.find()){
            this.out = m.group(1);
            this.type = "emote";
            return;
        }

        m = id.matcher(s);
        if(m.find()){
            this.out = m.group(1);
            this.type = "id";
        }

    }
}