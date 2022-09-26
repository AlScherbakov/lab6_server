package command;

import messages.AuthMessage;

public class AuthCommand extends Command{
    private static final long serialVersionUID = 17L;
    private final Receiver state;
    private final String username;
    private final String password;
    public AuthCommand(Receiver state, AuthMessage message){
        this.state = state;
        this.username = message.getUsername();
        this.password = message.getPassword();
        this.name = CommandEnum.AUTH;
    }

    public String execute(){
        return String.valueOf(state.authenticateUser(username, password));
    }

    public static String describe(){
        return "";
    }
}