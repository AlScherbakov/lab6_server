package messages;

import command.CommandEnum;
import util.User;

import java.io.Serializable;

/**
 * Abstract Command class - superclass for all commands - a part of Command pattern
 */

public abstract class Message implements Serializable {
    protected CommandEnum name;
    protected User user;

    public CommandEnum getCommandName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
