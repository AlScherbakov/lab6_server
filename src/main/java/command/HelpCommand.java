package command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Help command returns an info about all commands in the project
 */

public class HelpCommand extends Command {
    private static final long serialVersionUID = 6L;
    public HelpCommand(){
        this.name = CommandEnum.HELP;
    }
    public String execute() {
        Class[] commands = {
                HelpCommand.class,
                InfoCommand.class,
                ShowCommand.class,
                AddElementCommand.class,
                UpdateElementCommand.class,
                RemoveByIdCommand.class,
                ClearCommand.class,
                ExecuteScriptCommand.class,
                ExitCommand.class,
                RemoveGreaterCommand.class,
                RemoveLowerCommand.class,
                HistoryCommand.class,
                MaxGroupByAdminCommand.class,
                FilterLessThanSemesterEnumCommand.class,
                PrintFieldDescendingGroupAdminCommand.class
        };
        return Arrays.stream(commands).map(commandClass -> {
            try {
                Method describeCurrentCommand = commandClass.getDeclaredMethod("describe");
                describeCurrentCommand.setAccessible(true);
                return (String) describeCurrentCommand.invoke(null);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
                return "";
            }
        }).collect(Collectors.joining("\n"));
    }
    public static String describe(){
        return "help : вывести справку по доступным командам";
    }
}
