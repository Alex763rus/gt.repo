/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseTests;

import databases.SpecialtyDB;
import databases.StudentDB;
import exception.DataBaseEx;
import exception.ReadWriteEx;
import exception.SpecialtySQLEx;
import exception.StudentSQLEx;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import student.Student;
import university.Specialty;

/**
 *
 * @author Алексей
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestsStudentDB {

    private StudentDB studentDB;
    private SpecialtyDB specialtyDb;

    @Before
    public void init() throws DataBaseEx, StudentSQLEx, SpecialtySQLEx {
        studentDB = new StudentDB();
        specialtyDb = new SpecialtyDB();
        studentDB.dropAndcreateTable();
        specialtyDb.dropAndcreateTable();
    }

    @Test
    public void test1NoStudent() throws ReadWriteEx, StudentSQLEx {
        Assert.assertTrue(studentDB.getStudents().isEmpty());
    }

    @Test
    public void test2FirstStudent() throws ReadWriteEx, StudentSQLEx {
        studentDB.addStudent(new Student(1, "StudentName", 5.0f, 1));
        Assert.assertEquals(studentDB.getStudents().get(0), new Student(1, "StudentName", 5.0f, 1));
    }

    @Test
    public void test3GetStudentsFromSpecialty1() throws StudentSQLEx {
        Assert.assertTrue(studentDB.getStudentsFromSpecialty("specName").isEmpty());
    }

    @Test
    public void test4GetStudentsFromSpecialty2() throws StudentSQLEx, SpecialtySQLEx {
        specialtyDb.addSpecialty(new Specialty(1, "specName", "specNameDescr"));
        studentDB.addStudent(new Student(1, "StudentName", 5.0f, 1));
        Assert.assertEquals(studentDB.getStudentsFromSpecialty("specName").get(0), studentDB.getStudents().get(0));
    }

    @After
    public void end() throws DataBaseEx, StudentSQLEx {
        studentDB.disconnect();
    }
}
