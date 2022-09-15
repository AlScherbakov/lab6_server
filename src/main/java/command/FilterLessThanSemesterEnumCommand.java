package command;

import messages.FilterLessThanSemesterEnumMessage;
import util.Semester;
import util.StudyGroup;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Filter less than semester enum command returns a collection of StudyGroups, which semester is lower than given one
 */

public class FilterLessThanSemesterEnumCommand extends Command{
    private static final long serialVersionUID = 5L;
    private final Semester semester;
    private final Set<StudyGroup> collection;
    private final Receiver state;
    public FilterLessThanSemesterEnumCommand(Receiver state, FilterLessThanSemesterEnumMessage message){
        this.state = state;
        semester = message.getSemester();
        collection = state.getCollection();
        this.name = CommandEnum.FILTER_LESS_THAN_SEMESTER_ENUM;
    }
    public String execute(){
        Set<StudyGroup> g = new TreeSet<>(Comparator.comparing(StudyGroup::getSemesterEnum));
        g.addAll(collection);
        g.removeIf((StudyGroup x) -> semester.compareTo(x.getSemesterEnum()) <= 0);
        if (g.size() != collection.size()){
            state.setCollection(g);
            return "Коллекция отфильтрована (show - список элементов)";
        } else {
            return "Фильтр применён, но не повлиял на коллекцию";
        }
    }
    public static String describe() {
        return "filter_less_than_semester_enum (SECOND, THIRD, SIXTH, SEVENTH) : вывести элементы, значение поля semesterEnum которых меньше заданного";
    }
}
