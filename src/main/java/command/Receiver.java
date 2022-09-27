package command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import util.*;

import java.io.BufferedReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * Receiver class - Command pattern element - stores program state
 */

public class Receiver {
    private final long serialVersionUID = 1L;
    private final Deque<BufferedReader> readers = new ArrayDeque<>();
    private final Connection connection;
    private final Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").registerTypeAdapter(LocalDate.class, new LocalDateDeserializer()).registerTypeAdapter(LocalDate.class, new LocalDateSerializer()).create();
    public String collectionInitializationDate;
    private Set<StudyGroup> collection;
    private List<CommandEnum> history;
    private boolean working;
    private DataInputSource source;

    public Receiver(Set<StudyGroup> c, List<CommandEnum> h, boolean w, DataInputSource s, String cid, Connection conn) {
        collection = c;
        history = h;
        working = w;
        source = s;
        collectionInitializationDate = cid;
        connection = conn;
    }

    public Set<StudyGroup> getCollection() {
        return collection;
    }

    public void setCollection(Set<StudyGroup> c) {
        collection = c;
    }

    public List<CommandEnum> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<CommandEnum> h) {
        history = h;
    }

    public boolean getWorking() {
        return working;
    }

    public void setWorking(boolean w) {
        working = w;
    }

    public DataInputSource getSource() {
        if (!readers.isEmpty()) {
            return new DataInputSource(readers.getFirst());
        }
        return source;
    }

    public void setSource(DataInputSource s) {
        source = s;
    }

    public String getCollectionInitializationDate() {
        return collectionInitializationDate;
    }

    public void setCollectionInitializationDate(String d) {
        collectionInitializationDate = d;
    }

    public Deque<BufferedReader> getReaders() {
        return readers;
    }

    public void pushReader(BufferedReader r) {
        readers.push(r);
    }

    public void pushHistory(CommandEnum commandName) {
        history.add(commandName);
    }

    public void removeFirstReader() {
        if (readers.size() > 0) {
            readers.remove();
        }
    }

    public boolean hasElementWithId(int id) {
        return collection.stream().filter(x -> x.getId() == id).findAny().orElse(null) != null;
    }

    public void addToCollection(StudyGroup e) {
        collection.add(e);
    }

    public int removeGroupFromDB(long id) throws SQLException {
        int rowsChangedCount = 0;
        PreparedStatement removeStatement = connection.prepareStatement("DELETE FROM groups WHERE id=?");
        removeStatement.setLong(1, id);
        rowsChangedCount = removeStatement.executeUpdate();
        return rowsChangedCount;
    }

    public int addGroupToDB(StudyGroup group, long authorId) {
        int id = -1;
        try {
            PreparedStatement addStatement = connection.prepareStatement("INSERT INTO groups VALUES (nextval('groups_ids'), ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNS id");
            addStatement.setString(1, group.getName());
            addStatement.setString(2, gson.toJson(group.getCoordinates()));
            addStatement.setString(3, gson.toJson(group.getCreationDate()));
            addStatement.setInt(4, group.getStudentsCount());
            addStatement.setLong(5, group.getTransferredStudents());
            addStatement.setString(6, gson.toJson(group.getFormOfEducation()));
            addStatement.setString(7, gson.toJson(group.getSemesterEnum()));
            addStatement.setString(8, gson.toJson(group.getAdmin()));
            addStatement.setLong(9, authorId);
            id = addStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении элемента в базу данных");
        }
        return id;
    }

    public int updateElementInDB(StudyGroup group) {
        int rowsChangedCount = 0;
        try {
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE groups SET name=?, coordinates=?, creationDate=?, studentsCount=?, transferredStudents=?, formOfEducation=?, semesterEnum=?, groupAdmin=?, author=? WHERE id=?");
            updateStatement.setString(1, group.getName());
            updateStatement.setString(2, gson.toJson(group.getCoordinates()));
            updateStatement.setString(3, gson.toJson(group.getCreationDate()));
            updateStatement.setInt(4, group.getStudentsCount());
            updateStatement.setLong(5, group.getTransferredStudents());
            updateStatement.setString(6, gson.toJson(group.getFormOfEducation()));
            updateStatement.setString(7, gson.toJson(group.getSemesterEnum()));
            updateStatement.setString(8, gson.toJson(group.getAdmin()));
            updateStatement.setLong(9, group.getAuthor());
            updateStatement.setLong(10, group.getId());
            rowsChangedCount = updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении элемента в базе данных");
        }
        return rowsChangedCount;
    }

    public void updateCollectionFromDB() {
        try {
            TreeSet<StudyGroup> collectionCandidate = new TreeSet<>();
            ResultSet rows = connection.createStatement().executeQuery("SELECT * FROM groups");
            while (rows.next()) {
                StudyGroup groupCandidate = new StudyGroup(rows.getInt("id"), gson.fromJson(rows.getString("creationDate"), LocalDate.class), rows.getString("name"), gson.fromJson(rows.getString("coordinates"), Coordinates.class),
                        rows.getInt("studentsCount"), rows.getLong("transferredStudents"), gson.fromJson(rows.getString("formOfEducation"), FormOfEducation.class),
                        gson.fromJson(rows.getString("semesterEnum"), Semester.class), gson.fromJson(rows.getString("groupAdmin"), Person.class), rows.getLong("author"));
                if (groupCandidate.isValid()) {
                    collectionCandidate.add(groupCandidate);
                }
            }
            setCollection(collectionCandidate);
        } catch (SQLException e) {
            System.err.println("Ошибка при получении коллекции из базы данных");
            System.err.println(ExceptionUtils.getStackTrace(e));
        }
    }

    public long authenticateUser(String username, String password) {
        long id = -1;
        try {
            PreparedStatement registerStatement = connection.prepareStatement("INSERT INTO users VALUES (nextval('users_ids'), ?, sha224(?)) RETURNING id");
            registerStatement.setString(1, username);
            registerStatement.setBytes(2, password.getBytes(StandardCharsets.UTF_8));

            PreparedStatement authStatement = connection.prepareStatement("SELECT id , password FROM users WHERE username = ?"); //AND password = sha224(?) ON CONFLICT (password) RETURN -1 OR (username) INSERT INTO users VALUES (nextval('users_ids'), ?, sha224(?)) RETURN currval('users_ids')");
            authStatement.setString(1, username);
            ResultSet authRes = authStatement.executeQuery();
            if (authRes.next()) {
                long idCandidate = authRes.getLong("id");
                String passwd = authRes.getString("password");
                MessageDigest md = MessageDigest.getInstance("SHA-224");
                byte[] encryptedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
                BigInteger no = new BigInteger(1, encryptedBytes);
                StringBuilder encryptedPassword = new StringBuilder(no.toString(16));
                while (encryptedPassword.length() < 32) {
                    encryptedPassword.insert(0, "0");
                }
                encryptedPassword.insert(0, "\\x");
                if (passwd.equals(encryptedPassword.toString())) {
                    id = idCandidate;
                }
            } else {
                ResultSet regRes = registerStatement.executeQuery();
                if (regRes.next()) {
                    id = regRes.getLong(1);
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.err.println("Ошибка авторизации");
        }
        return id;
    }
}
