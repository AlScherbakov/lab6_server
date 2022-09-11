package command;

/**
 * Stops program execution with 0 code
 */

public class ExitCommand extends Command{
    private static final long serialVersionUID = 4L;
    public ExitCommand(){
        this.name = CommandEnum.EXIT;
    }
//    @Override
//    public Integer execute(){
//        System.exit(0);
//        return 0;
//    }
    public static String describe(){
        return "exit : завершить программу (без сохранения в файл)";
    }
}
