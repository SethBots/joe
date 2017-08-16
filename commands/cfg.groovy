import net.swvn9.joe.Command
import net.swvn9.joe.*
import net.swvn9.joe.masterConfig

import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors
import net.dv8tion.jda.core.entities.Message

class cfg extends Command {
    void command(){
        List<Message> commandMessages = new ArrayList<>(jda.getGuildById("341666084748787712").getTextChannelById("341669410827796500").getIterableHistory().stream().collect(Collectors.toList()));
        Pattern classname = Pattern.compile("class\\s(\\w*)")

        for(Message m:commandMessages){
            Matcher match = classname.matcher(m.getContentRaw())
            String s = "n/a"
            while(match.find()){
                s = match.group(1)
            }
            /*channel.sendMessage(
                    "**message id** "+m.getId()+"\n**name** "+s+"\n"+
                    m.getContentRaw()+"\n\n"
            ).queue()*/
        }
        masterConfig.pull("command")
    }
}