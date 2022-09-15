package command;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Execute script command returns new BufferedReader from filepath
 */

public class ExecuteScriptCommand extends Command{
    private static final long serialVersionUID = 3L;
    String scriptPath;
    public ExecuteScriptCommand(String p){
        scriptPath = p;
        this.name = CommandEnum.EXECUTE_SCRIPT;
    }
    public static String describe() {
        return "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме";
    }
}
