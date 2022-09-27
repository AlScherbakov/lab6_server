package command;

import messages.AddElementMessage;
import util.StudyGroup;

/**
 * Add element command requests and returns new StudyGroup from DataCollector
 */

public class AddElementCommand extends Command {
    private static final long serialVersionUID = 1L;
    private final Receiver state;
    private final AddElementMessage message;

    public AddElementCommand(Receiver state, AddElementMessage message) {
        this.state = state;
        this.message = message;
        this.name = CommandEnum.ADD;
    }

    public static String describe() {
        return "add {element} : добавить новый элемент в коллекцию";
    }

    public String execute() {
        try {
            StudyGroup newGroup = (message).getElement();
            if (newGroup != null) {
                int id = state.addGroupToDB(newGroup, message.getUser().getId());
                if (id != -1) {
                    newGroup.setId(id);
                    state.addToCollection(newGroup);
                    return "Элемент успешно добавлен в коллекцию";
                } else {
                    throw new RuntimeException("Элемент не был добавлен в коллекцию");
                }
            } else {
                throw new RuntimeException("Элемент не был добавлен в коллекцию");
            }
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}