/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import exception.ReadWriteEx;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import reflectionapi.XField;
import reflectionapi.XTable;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reflectionapi.Autoincrement;
import reflectionapi.PrimaryKey;
import student.Student;

/**
 *
 * @author grigorevap
 */
@XTable(title = "specialty")
public class Specialty {

    static final Logger log = LogManager.getLogger(Specialty.class);
    @XField
    @Autoincrement
    @PrimaryKey
    private int id;
    @XField
    private String name;
    @XField
    private String description;
    private List<Student> students;

    public Specialty() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Specialty(String name, String description) {
        this.name = name;
        this.description = description;
        this.students = new ArrayList();
    }

    public Specialty(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.students = new ArrayList();
    }

    public Specialty(int id) {
        this.id = id;
        this.name = "";
        this.description = "";
        this.students = new ArrayList();
    }

    public static void writeStudentToXML(List<Specialty> specialty, String path) throws ReadWriteEx {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            xmlMapper.writeValue(new File(path), specialty);
        } catch (IOException ex) {
            log.error(ex);
            throw new ReadWriteEx();
        }
    }

    public void addStudent(Student student) {
        if (student.getSpecialtyId() == id && !students.contains(student)) {
            students.add(student);
        }
    }

    public void addStudents(List<Student> students) {
        students.stream().forEach((student) -> {
            addStudent(student);
        });
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List getStudents() {
        return students;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + this.id;
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
        final Specialty other = (Specialty) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Specialty{" + "id=" + id + ", name=" + name + ", description=" + description + ", students=" + students + '}';
    }

}
