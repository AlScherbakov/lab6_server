package command;

import messages.FilterLessThanSemesterEnumMessage;
import util.Semester;
import util.StudyGroup;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Filter less than semester enum command returns a collection of StudyGroups, which semester is lower than given one
 */

public class FilterLessThanSemesterEnumCommand extends Command {
    private static final long serialVersionUID = 5L;
    private final Semester semester;
    private final Set<StudyGroup> collection;
    private final Receiver state;
    private final FilterLessThanSemesterEnumMessage message;

    public FilterLessThanSemesterEnumCommand(Receiver state, FilterLessThanSemesterEnumMessage message) {
        this.state = state;
        semester = message.getSemester();
        collection = state.getCollection();
        this.message = message;
        this.name = CommandEnum.FILTER_LESS_THAN_SEMESTER_ENUM;
    }

    public static String describe() {
        return "filter_less_than_semester_enum (SECOND, THIRD, SIXTH, SEVENTH) : вывести элементы, значение поля semesterEnum которых меньше заданного";
    }

    public String execute() {
        Set<StudyGroup> groupsToRemove = new TreeSet<>();
        Set<StudyGroup> g = new TreeSet<>(Comparator.comparing(StudyGroup::getSemesterEnum));
        g.addAll(collection);
        g.stream().filter(x -> x.getAuthor() == message.getUser().getId()).forEach(x -> {
            try {
                if (semester.compareTo(x.getSemesterEnum()) <= 0) {
                    state.removeGroupFromDB(x.getId());
                }
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка при фильтрации коллекции");
            }
            groupsToRemove.add(x);
        });
        collection.removeAll(groupsToRemove);
        if (g.size() != collection.size()) {
            state.setCollection(collection);
            return "Коллекция отфильтрована (show - список элементов)";
        } else {
            return "Фильтр применён, но не повлиял на коллекцию";
        }
    }
}
