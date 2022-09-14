package server;

import command.*;
import messages.*;

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
                return new InfoCommand(state).execute();
            }
            case SHOW: {
                state.pushHistory(CommandEnum.SHOW);
                return new ShowCommand(state).execute();
            }
            case ADD: {
                state.pushHistory(CommandEnum.ADD);
                return new AddElementCommand(state, (AddElementMessage) message).execute();
            }
            case CLEAR: {
                state.pushHistory(CommandEnum.CLEAR);
                return new ClearCommand(state).execute();
            }
            case SAVE: {
                state.pushHistory(CommandEnum.SAVE);
                return  new SaveCommand(state).execute();
            }
            case REMOVE_GREATER: {
                state.pushHistory(CommandEnum.REMOVE_GREATER);
                return new RemoveGreaterCommand(state, (RemoveGreaterMessage) message).execute();
            }
            case REMOVE_LOWER: {
                state.pushHistory(CommandEnum.REMOVE_LOWER);
                return new RemoveLowerCommand(state, (RemoveLowerMessage) message).execute();
            }
            case HISTORY: {
                state.pushHistory(CommandEnum.HISTORY);
                return new HistoryCommand(state).execute();
            }
            case MAX_BY_GROUP_ADMIN: {
                state.pushHistory(CommandEnum.MAX_BY_GROUP_ADMIN);
                return new MaxGroupByAdminCommand(state).execute();
            }
            case PRINT_FIELD_DESCENDING_GROUP_ADMIN: {
                state.pushHistory(CommandEnum.PRINT_FIELD_DESCENDING_GROUP_ADMIN);
                return new PrintFieldDescendingGroupAdminCommand(state).execute();
            }
            case UPDATE: {
                state.pushHistory(CommandEnum.UPDATE);
                return new UpdateElementCommand(state, (UpdateElementMessage) message).execute();
            }
            case REMOVE_BY_ID: {
                state.pushHistory(CommandEnum.REMOVE_BY_ID);
                return new RemoveByIdCommand(state, (RemoveByIdMessage) message).execute();
            }
            case EXECUTE_SCRIPT: {
                state.pushHistory(CommandEnum.EXECUTE_SCRIPT);
                return String.format("Выполняется скрипт %s", ((ExecuteScriptMessage) message).getScriptPath());
            }
            case FILTER_LESS_THAN_SEMESTER_ENUM:{
                state.pushHistory(CommandEnum.FILTER_LESS_THAN_SEMESTER_ENUM);
                return new FilterLessThanSemesterEnumCommand(state, (FilterLessThanSemesterEnumMessage) message).execute();
            }
            default: {
                return String.format("Команды '%s' не существует (help - список команд) или аргумент команды не задан\n", commandName);
            }
        }
    }
}
