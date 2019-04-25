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
import specialty.Specialty;

/**
 *
 * @author Алексей
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestsSpecialtyDB {

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
    public void test1NoSpecialtys() throws SpecialtySQLEx {
        Assert.assertTrue(specialtyDb.getSpecialtys().isEmpty());
    }

    @Test
    public void test2FirstSpecialtys() throws SpecialtySQLEx {
        specialtyDb.addSpecialty(new Specialty(1, "spec1", "spec1Descr"));
        Assert.assertEquals(specialtyDb.getSpecialtys().get(0), new Specialty(1, "spec1", "spec1Descr"));
    }

    @Test
    public void test3GetStudentsFromSpecialty1() throws StudentSQLEx, SpecialtySQLEx {
        specialtyDb.addSpecialty(new Specialty(1, "spec1", "spec1Descr"));
        studentDB.addStudent(new Student(1, "StudentName1", 5.0f, 1));
        studentDB.addStudent(new Student(2, "StudentName2", 3.0f, 1));

        Assert.assertTrue(specialtyDb.getAverageScoreFromSpecialty("spec1") - 4.0f < 0.01f);
    }

    @After
    public void end() throws DataBaseEx, StudentSQLEx {
        studentDB.disconnect();
    }
}
