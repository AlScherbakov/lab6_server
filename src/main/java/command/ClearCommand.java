package command;

import util.StudyGroup;

import java.util.Set;
import java.util.TreeSet;

/**
 * Clear command returns empty TreeSet
 */

public class ClearCommand extends Command{
    private static final long serialVersionUID = 2L;
    private final Receiver state;

    public ClearCommand(Receiver state){
        this.state = state;
        this.name = CommandEnum.CLEAR;
    }

    public String execute (){
        state.setCollection(new TreeSet<>());
        return "Коллекция очищена";
    }

    public static String describe() {
        return "clear : очистить коллекцию";
    }
}
