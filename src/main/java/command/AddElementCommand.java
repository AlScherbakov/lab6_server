package command;

import util.DataCollector;
import util.DataInputSource;
import util.StudyGroup;

/**
 * Add element command requests and returns new StudyGroup from DataCollector
 */

public class AddElementCommand extends Command{
    private static final long serialVersionUID = 1L;
    private final DataInputSource source;
    public AddElementCommand(DataInputSource s) {
        source = s;
        this.name = CommandEnum.ADD;
    }
//    @Override
//    public StudyGroup execute(){
//        return new DataCollector(source).requestStudyGroup();
//    }
    @Override
    public String describe(){
        return "add {element} : добавить новый элемент в коллекцию";
    }
}