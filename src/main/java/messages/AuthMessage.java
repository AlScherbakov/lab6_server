package messages;

import command.CommandEnum;

public class AuthMessage extends Message {
    private static final long serialVersionUID = 17L;
    public String username;
    public String password;

    public AuthMessage(String username, String password) {
        this.username = username;
        this.password = password;
        this.name = CommandEnum.AUTH;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}