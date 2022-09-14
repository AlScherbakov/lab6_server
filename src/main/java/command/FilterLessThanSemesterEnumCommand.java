package command;

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
    Semester semester;
    Set<StudyGroup> collection;
    public FilterLessThanSemesterEnumCommand(Semester s, Set<StudyGroup> c){
        semester = s;
        collection = c;
        this.name = CommandEnum.FILTER_LESS_THAN_SEMESTER_ENUM;
    }
    public Set<StudyGroup> execute(){
        Set<StudyGroup> g = new TreeSet<>(Comparator.comparing(StudyGroup::getSemesterEnum));
        g.addAll(collection);
        g.removeIf((StudyGroup x) -> semester.compareTo(x.getSemesterEnum()) <= 0);
        if (g.size() > 0){
            return g;
        } else {
            return collection;
        }
    }
    public static String describe() {
        return "filter_less_than_semester_enum (SECOND, THIRD, SIXTH, SEVENTH) : вывести элементы, значение поля semesterEnum которых меньше заданного";
    }
}
