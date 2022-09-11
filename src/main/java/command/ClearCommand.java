package command;

import util.StudyGroup;

import java.util.Set;
import java.util.TreeSet;

/**
 * Clear command returns empty TreeSet
 */

public class ClearCommand extends Command{
    private static final long serialVersionUID = 2L;

    public ClearCommand(){
        this.name = CommandEnum.CLEAR;
    }

//    @Override
//    public Set<StudyGroup> execute (){
//        return new TreeSet<>();
//    }

    public static String describe() {
        return "clear : очистить коллекцию";
    }
}
