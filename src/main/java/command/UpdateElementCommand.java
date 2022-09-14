package command;

import util.DataCollector;
import util.DataInputSource;
import util.StudyGroup;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Update element command returns new collection with updated element by given id
 */

public class UpdateElementCommand extends Command{
    private static final long serialVersionUID = 16L;
    int id;
    Set<StudyGroup> collection;
    StudyGroup updatedGroup;
    public UpdateElementCommand(int i, Set<StudyGroup> collection, StudyGroup updatedGroup){
        id = i;
        this.collection = collection;
        this.updatedGroup = updatedGroup;
        this.name = CommandEnum.UPDATE;
    }
    public Set<StudyGroup> execute(){
        TreeSet<StudyGroup> g = new TreeSet<>(collection);
        g.removeIf(x -> !Objects.equals(x.getId(), id));
        StudyGroup aGroup = g.first();
        assert aGroup != null;
        collection.remove(aGroup);
        collection.add(updatedGroup);
        return collection;
    }
    public static String describe(){
        return "update (int)id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }
}
