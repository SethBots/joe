package net.swvn9.joe;

import groovy.lang.GroovyClassLoader;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.swvn9.joe.Bot.jda;

@SuppressWarnings({"ALL", "CanBeFinal"})
public class commandChannel {
    public static Map<String,Command> list = new HashMap<>();
    private static Map<String,Class> classes = new HashMap<>();
    public static List<String> nodes = new ArrayList<>();
    public static Map<String,Command> running = new HashMap<>();

    public static void pull(String s){
        list.clear();
        classes.clear();
        GroovyClassLoader gcl = new GroovyClassLoader();
        String channelID = "341669410827796500";

        List<Message> commandMessages = new ArrayList<>();
        commandMessages.addAll(jda.getGuildById("341666084748787712").getTextChannelById(channelID).getIterableHistory().complete());
        Pattern classname = Pattern.compile("^```?(\\w*)|```$");
        for(Message m:commandMessages){
            Matcher matcher = classname.matcher(m.getRawContent());
            if(matcher.find()&&m.getRawContent().startsWith("```")){
                String loadString = m.getRawContent().replaceAll(classname.toString(),"");
                try{

                    if(loadString.toLowerCase().contains("token")) throw new Exception("Token cannot be revealed by an external command.");
                    if(loadString.toLowerCase().contains("shutdown")) throw new Exception("Cannot shutdown JDA with an external command.");
                    if(loadString.toLowerCase().contains(".exit")) throw new Exception("Cannot close the process with an external command.");

                    Class toLoad = gcl.parseClass("import net.swvn9.joe.Command\nimport net.swvn9.joe.*\n"+loadString);
                    if(classes.containsKey(toLoad.getName())){
                        classes.replace(toLoad.getName(),toLoad);
                        list.replace(toLoad.getName(),(Command) toLoad.newInstance());
                        list.get(toLoad.getName()).meta();
                        nodes.add(list.get(toLoad.getName()).node);
                    } else {
                        classes.put(toLoad.getName(),toLoad);
                        list.put(toLoad.getName(),(Command) toLoad.newInstance());
                        list.get(toLoad.getName()).meta();
                        nodes.add(list.get(toLoad.getName()).node);
                    }
                    for(MessageReaction mr:m.getReactions()){
                        if(mr.getEmote().getEmote().equals(jda.getEmoteById("338773212710305795"))){
                            mr.removeReaction().queue();
                            break;
                        }
                    }
                    m.addReaction(jda.getEmoteById("338773147849588740")).queue();
                } catch (Exception e){
                    for(MessageReaction mr:m.getReactions()){
                        if(mr.getEmote().getEmote().equals(jda.getEmoteById("338773147849588740"))){
                            mr.removeReaction().queue();
                            break;
                        }
                    }
                    m.addReaction(jda.getEmoteById("338773212710305795")).queue();
                    m.getAuthor().openPrivateChannel().queue(c->c.sendMessage("\n<:rt:338773212710305795> **Your command has failed to compile with the following exception:**\n```java\n"+e+"```\nI will attempt to re-compile the command the next time a change is made in <#341669410827796500>, or if you edit the existing command message in the channel.\n*For reference, here is the original content of your command:*\n"+m.getRawContent()).queue());
                    System.out.println("A command by "+m.getAuthor().getName()+" has failed to compile with the exception: "+e);
                }
            } else {
                if(!m.getRawContent().startsWith("//")&&!m.isPinned()){
                    for(MessageReaction mr:m.getReactions()){
                        if(mr.getEmote().getEmote().equals(jda.getEmoteById("338773147849588740"))){
                            mr.removeReaction().queue();
                            break;
                        }
                    }
                    m.addReaction(jda.getEmoteById("338773212710305795")).queue();
                }
            }
        }
        StringBuilder commands = new StringBuilder();
        for(String st:classes.keySet()) commands.append("- ").append(st).append("\n");
        jda.getTextChannelById("341668775394934787").editMessageById("341719410995625984","```markdown\n#Loaded commands from #master-commands\n\n"+commands+"\n[Reloaded "+ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replaceFirst("T"," ")+"]```").queue();
    }
    public static String show(){
        return "Commands: "+classes.keySet();
    }
}
