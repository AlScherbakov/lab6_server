package server;

import command.*;
import messages.*;
import util.Semester;
import util.StudyGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class CommandExecutor {
    private final Message message;
    private final Receiver state;

    public CommandExecutor (Message processedMessage, Receiver state) {
        this.message = processedMessage;
        this.state = state;
    }

    public String execute(){
        CommandEnum commandName = message.getCommandName();
        System.out.println(commandName);
        switch (commandName){
            case HELP: {
                state.pushHistory(CommandEnum.HELP);
                return new HelpCommand().execute();
            }
            case INFO: {
                state.pushHistory(CommandEnum.INFO);
                return new InfoCommand(state.getCollection(), state.getCollectionInitializationDate()).execute();
            }
            case SHOW: {
                state.pushHistory(CommandEnum.SHOW);
                return new ShowCommand(state.getCollection()).execute();
            }
            case ADD: {
                try {
                    StudyGroup newGroup = ((AddElementMessage) message).getElement();
                    if (newGroup != null) {
                        state.addToCollection(newGroup);
                        return "Элемент успешно добавлен в коллекцию";
                    } else {
                        throw new RuntimeException("Элемент не был добавлен в коллекцию");
                    }
                } catch (RuntimeException e){
                    System.err.println(e.getMessage());
                    return e.getMessage();
                }
                finally{
                    state.pushHistory(CommandEnum.ADD);
                }
            }
            case CLEAR: {
                state.pushHistory(CommandEnum.CLEAR);
                state.setCollection(new ClearCommand().execute());
                return "Коллекция очищена";
            }
            case SAVE: {
                try {
                    new SaveCommand(state.getOutputFilepath(), state.getCollection()).execute();
                    System.out.println("Коллекция сохранена в файл");
                } catch (IOException e) {
                    System.err.println("Возникла ошибка при выполнении команды save. Проверьте путь и права доступа к файлу");
                } finally {
                    state.pushHistory(CommandEnum.SAVE);
                }
            }
            case REMOVE_GREATER: {
                try {
                    StudyGroup aGroup = ((RemoveGreaterMessage) message).getElement();
                    state.setCollection(new RemoveGreaterCommand((TreeSet<StudyGroup>) state.getCollection(), aGroup).execute());
                    return ("Все элементы, превышающие заданный, удалены из коллекции");
                } catch (ClassCastException e) {
                    System.err.println(e.getMessage());
                    return "Возникла ошибка при выполнении команды remove_greater";
                } finally {
                    state.pushHistory(CommandEnum.REMOVE_GREATER);
                }
            }
            case REMOVE_LOWER: {
                try {
                    StudyGroup aGroup = ((RemoveLowerMessage) message).getElement();
                    state.setCollection(new RemoveLowerCommand((TreeSet<StudyGroup>) state.getCollection(), aGroup).execute());
                    return "Все элементы, меньшие, чем заданный, удалены из коллекции";
                } catch (ClassCastException e) {
                    System.err.println(e.getMessage());
                    return "Возникла ошибка при выполнении команды remove_lower";
                } finally {
                    state.pushHistory(CommandEnum.REMOVE_LOWER);
                }
            }
            case HISTORY: {
                StringBuilder result = new StringBuilder();
                List<CommandEnum> historyToShow = new HistoryCommand(state.getHistory()).execute();
                state.pushHistory(CommandEnum.HISTORY);
                historyToShow.forEach(it -> result.append(it.toString()));
                return result.toString();
            }
            case MAX_BY_GROUP_ADMIN: {
                state.pushHistory(CommandEnum.MAX_BY_GROUP_ADMIN);
                return new MaxGroupByAdminCommand(state.getCollection()).execute().toString(); //ban string exchange
            }
            case PRINT_FIELD_DESCENDING_GROUP_ADMIN: {
                state.pushHistory(CommandEnum.PRINT_FIELD_DESCENDING_GROUP_ADMIN);
                return new PrintFieldDescendingGroupAdminCommand(state.getCollection()).execute();  //ban string exchange
            }
            case UPDATE: {
                state.pushHistory(CommandEnum.UPDATE);
                int id = ((UpdateElementMessage) message).getId();
                StudyGroup updatedGroup = ((UpdateElementMessage) message).getElement();
                state.setCollection(new UpdateElementCommand(id, state.getCollection(), updatedGroup).execute());
                return String.format("Элемент #%d обновлён (show - список элементов)", id);
            }
            case REMOVE_BY_ID: {
                state.pushHistory(CommandEnum.REMOVE_BY_ID);
                int id = ((RemoveByIdMessage) message).getId();
                state.setCollection(new RemoveByIdCommand(id, state.getCollection()).execute());
                return String.format("Элемент #%d удалён из коллекции (show - список элементов)", id);
            }
            case EXECUTE_SCRIPT: {
                state.pushHistory(CommandEnum.EXECUTE_SCRIPT);
//            state.pushReader(new ExecuteScriptCommand(scriptPath).execute()); // process on Client
            }
            case FILTER_LESS_THAN_SEMESTER_ENUM:{
                state.pushHistory(CommandEnum.FILTER_LESS_THAN_SEMESTER_ENUM);
                Semester semester = ((FilterLessThanSemesterEnumMessage) message).getSemester();
                state.setCollection(new FilterLessThanSemesterEnumCommand(semester, state.getCollection()).execute());
                return "Коллекция отфильтрована (show - список элементов)";
            }
            default: {
                return String.format("Команды '%s' не существует (help - список команд) или аргумент команды не задан\n", commandName);
            }
        }
    }
}
