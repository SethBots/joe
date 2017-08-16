import net.swvn9.joe.Command
import net.swvn9.joe.*
import java.util.concurrent.TimeUnit

class list extends Command {
    void command(){
        channel.sendMessageFormat(("`")*3+"%s\n%s"+("`")*3,Commands.show(),Config.show()).queue({
            m->m.delete().queueAfter(1, TimeUnit.MINUTES)
        })
    }
}