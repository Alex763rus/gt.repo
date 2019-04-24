/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databases;

import exception.DataBaseEx;
import exception.StudentSQLEx;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import student.Student;

/**
 *
 * @author Алексей
 */
public class StudentDB extends DataBaseConnection {

    private final String psStmtSelectAll = "SELECT id, name, averageScore, specialtyId FROM student;";
    private final String psStmtInsertStudent = "INSERT INTO student (name, averageScore, specialtyId) VALUES (?, ?, ?);";
    private final String psStmtSelectStudentsInSpecialty = "SELECT st.id, st.name, averageScore, specialtyId FROM student st INNER JOIN specialty sp on sp.id = st.specialtyId  WHERE sp.name = ?;";

    public StudentDB() throws DataBaseEx {
        log.debug("run StudentDB()");
    }

    public List<Student> getStudents() throws StudentSQLEx {
        log.debug("run getStudents()");
        List Students = new ArrayList();
        try {
            PreparedStatement psStmt = connection.prepareStatement(psStmtSelectAll);
            ResultSet rs = psStmt.executeQuery();
            while (rs.next()) {
                Students.add(new Student(rs.getInt("id"), rs.getString("name"), rs.getFloat("averageScore"), rs.getInt("specialtyId")));
            }
        } catch (SQLException ex) {
            log.error(ex);
            throw new StudentSQLEx();
        }
        return Students;
    }

    public List<Student> getStudentsFromSpecialty(String specialtyName) throws StudentSQLEx {
        log.debug("run getStudentsFromSpecialty()");
        List Students = new ArrayList();
        try {
            PreparedStatement psStmt = connection.prepareStatement(psStmtSelectStudentsInSpecialty);
            psStmt.setString(1, specialtyName);
            ResultSet rs = psStmt.executeQuery();
            while (rs.next()) {
                Students.add(new Student(rs.getInt("id"), rs.getString("name"), rs.getFloat("averageScore"), rs.getInt("specialtyId")));
            }
        } catch (SQLException ex) {
            log.error(ex);
            throw new StudentSQLEx();
        }
        return Students;
    }

    public void addStudent(Student student) throws StudentSQLEx {
        log.debug("run addStudent()");
        try {
            PreparedStatement psStmt = connection.prepareStatement(psStmtInsertStudent);
            psStmt.setString(1, student.getName());
            psStmt.setFloat(2, student.getAverageScore());
            psStmt.setInt(3, student.getSpecialtyId());
            psStmt.addBatch();
            psStmt.executeBatch();
        } catch (SQLException ex) {
            log.error(ex);
            throw new StudentSQLEx();
        }
    }

    public void addStudent(List<Student> students) throws StudentSQLEx {
        log.debug("run addStudent()");
        for (Student student : students) {
            addStudent(student);
        }
    }

    public void dropAndcreateTable() throws StudentSQLEx {
        log.debug("run dropAndcreateTable()");
        try {
            stmt.executeUpdate("DROP TABLE IF EXISTS student;");
            stmt.executeUpdate("CREATE TABLE student (\n"
                    + "        id    INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "        name  TEXT,\n"
                    + "        averageScore  FLOAT,\n"
                    + "        specialtyId  INTEGER, \n"
                    + "        FOREIGN KEY (specialtyId) REFERENCES specialty(id) \n"
                    + "    );");
        } catch (SQLException ex) {
            log.error(ex);
            throw new StudentSQLEx();
        }
        log.info("DROP AND CREATE STUDENT");
    }
}
