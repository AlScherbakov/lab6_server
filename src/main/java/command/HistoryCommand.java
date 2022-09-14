package command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * History command returns last 6 entries of called commands list
 */

public class HistoryCommand extends Command{
    private static final long serialVersionUID = 7L;
    private final Receiver state;
    private final List<CommandEnum> history;
    public HistoryCommand(Receiver state){
        this.state = state;
        this.history = state.getHistory();
        this.name = CommandEnum.HISTORY;
    }
    public String execute(){
        int historySize = history.size();
        if (historySize <= 6){
            return history.stream().map(Enum::toString).collect(Collectors.joining("\n"));
        } else{
           return new ArrayList<>(history.subList(historySize - 6, historySize)).stream().map(Enum::toString).collect(Collectors.joining("\n"));
        }
    }
    public static String describe(){
        return "history : вывести последние 6 команд (без их аргументов)";
    }
}
