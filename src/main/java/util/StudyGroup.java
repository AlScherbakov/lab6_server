package util;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class StudyGroup implements Comparable<StudyGroup>, Serializable {
    private static final long serialVersionUID = 99L;
    static ArrayList<Integer> idList = new ArrayList<>();
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int studentsCount; //Значение поля должно быть больше 0
    private long transferredStudents; //Значение поля должно быть больше 0
    private FormOfEducation formOfEducation; //Поле не может быть null
    private Semester semesterEnum; //Поле не может быть null
    private Person groupAdmin; //Поле может быть null
    private long author;

    public StudyGroup() {
        generateIDs();
        id = newId();
        creationDate = LocalDate.now();
    }

    public StudyGroup(String name, Coordinates coordinates, int studentsCount, long transferredStudents, FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin, long author) {
        this();
        setName(name);
        setCoordinates(coordinates);
        setStudentsCount(studentsCount);
        setTransferredStudents(transferredStudents);
        setFormOfEducation(formOfEducation);
        setSemesterEnum(semesterEnum);
        setGroupAdmin(groupAdmin);
        setAuthor(author);
    }

    public StudyGroup(int id, LocalDate creationDate, String name, Coordinates coordinates, int studentsCount, long transferredStudents, FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin, long author) {
        this();
        this.id = id;
        this.creationDate = creationDate;
        setName(name);
        setCoordinates(coordinates);
        setStudentsCount(studentsCount);
        setTransferredStudents(transferredStudents);
        setFormOfEducation(formOfEducation);
        setSemesterEnum(semesterEnum);
        setGroupAdmin(groupAdmin);
        setAuthor(author);
    }

    public static void generateIDs() {
        for (int i = 4; i < 1000; i++) {
            idList.add(i);
        }
        Collections.shuffle(idList);
    }

    public static int newId() {
        int id = idList.get(1);
        idList.remove(id);
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public boolean isValid() {
        return id != null && id > 0 && name != null && name.length() > 0 && coordinates != null && coordinates.getX() <= 927 && coordinates.getY() <= 772 && creationDate != null && studentsCount > 0 && transferredStudents > 0 && formOfEducation != null && semesterEnum != null && groupAdmin != null;
    }

    public void setGroupAdmin(Person groupAdmin) throws IllegalArgumentException {
        if (groupAdmin != null) {
            this.groupAdmin = groupAdmin;
        } else {
            throw new IllegalArgumentException("Поле не может быть null");
        }
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public long getAuthor() {
        return author;
    }

    public void setAuthor(long author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException {
        if (name != null && name.length() > 0) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Поле не может быть null, Строка не может быть пустой");
        }
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) throws IllegalArgumentException {
        if (coordinates != null) {
            this.coordinates = coordinates;
        } else {
            throw new IllegalArgumentException("Поле может быть null");
        }
    }

    public int getStudentsCount() {
        return studentsCount;
    }

    public void setStudentsCount(int studentsCount) throws IllegalArgumentException {
        if (studentsCount > 0) {
            this.studentsCount = studentsCount;
        } else {
            throw new IllegalArgumentException("Значение поля должно быть больше 0");
        }
    }

    public long getTransferredStudents() {
        return transferredStudents;
    }

    public void setTransferredStudents(long transferredStudents) throws IllegalArgumentException {
        if (transferredStudents > 0) {
            this.transferredStudents = transferredStudents;
        } else {
            throw new IllegalArgumentException("Значение поля должно быть больше 0");
        }
    }

    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    public void setFormOfEducation(FormOfEducation formOfEducation) throws IllegalArgumentException {
        if (formOfEducation != null) {
            this.formOfEducation = formOfEducation;
        } else {
            throw new IllegalArgumentException("Поле не может быть null");
        }
    }

    public Semester getSemesterEnum() {
        return semesterEnum;
    }

    public void setSemesterEnum(Semester semesterEnum) throws IllegalArgumentException {
        if (semesterEnum != null) {
            this.semesterEnum = semesterEnum;
        } else {
            throw new IllegalArgumentException("Поле не может быть null");
        }
    }

    public Person getAdmin() {
        return this.groupAdmin;
    }

    @Override
    public String toString() {
        return String.format("Учебная группа #%d %s. Координаты %s. Создана %s. Студентов - %d, переведённых - %d. Форма обучения - %s. Семестр - %s. Админ группы - %s", id, name, coordinates.toString(), creationDate, studentsCount, transferredStudents, formOfEducation, semesterEnum, groupAdmin.toString());
    }

    @Override
    public int compareTo(StudyGroup o) {
        return id - o.getId();
    }
}
