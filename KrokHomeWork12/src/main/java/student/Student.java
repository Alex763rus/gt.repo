/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.ReadWriteEx;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reflectionapi.Autoincrement;
import reflectionapi.PrimaryKey;
import reflectionapi.XField;
import reflectionapi.XTable;

/**
 *
 * @author grigorevap
 */
@XTable(title = "student")
public class Student {

    static final Logger log = LogManager.getLogger(Student.class);
    @XField
    @Autoincrement
    @PrimaryKey
    private int id;
    @XField
    private String name;
    @XField
    private float averageScore;
    @XField
    private int specialtyId;

    public Student() {
    }

    public Student(int id, String name, float averageScore, int specialtyId) {
        this.id = id;
        this.name = name;
        this.averageScore = averageScore;
        this.specialtyId = specialtyId;
    }

    public Student(String name, float averageScore, int specialtyId) {
        this.name = name;
        this.averageScore = averageScore;
        this.specialtyId = specialtyId;
    }

    public static void writeStudentToJson(List<Student> students, String path) throws ReadWriteEx {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(path), students);
        } catch (IOException ex) {
            log.error(ex);
            throw new ReadWriteEx();
        }
        log.info("writeStudentToJson to path: " + path);
    }

    public static List<Student> readStudentFromJson(String path) throws ReadWriteEx {
        List<Student> studentFromFile = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            studentFromFile = mapper.readValue(new File(path), new TypeReference<List<Student>>() {
            });
        } catch (IOException ex) {
            log.error(ex);
            throw new ReadWriteEx();
        }
        return studentFromFile;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Student other = (Student) obj;
        return this.id == other.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }

    public void setSpecialtyId(int specialtyId) {
        this.specialtyId = specialtyId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public int getSpecialtyId() {
        return specialtyId;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", name=" + name + ", averageScore=" + averageScore + ", specialtyId=" + specialtyId + '}';
    }

}
