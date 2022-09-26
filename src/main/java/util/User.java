package util;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 999L;
    private long id;
    private String username = null;
    private String password = null;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return this.id;
    }

    public boolean isValid() {
        if (this.username == null || this.password == null) return false;
        return true;//checkOrRegisterUser(this.username, this.password); // Проверять логин/пароль существующего пользователя или создать нового; выводить сообщения об ошибке
    }

    @Override
    public String toString() {
        return String.format("Пользователь %s, пароль - %s", username, password);
    }
}
