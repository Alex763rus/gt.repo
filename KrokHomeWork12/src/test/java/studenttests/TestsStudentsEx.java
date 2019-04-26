/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studenttests;

import exception.ReadWriteEx;
import org.junit.Test;
import student.Student;

/**
 *
 * @author Алексей
 */
public class TestsStudentsEx {

    @Test(expected = ReadWriteEx.class)
    public void testReadWriteExNoFile() throws ReadWriteEx {
        Student.readStudentFromJson("");
    }
    @Test(expected = ReadWriteEx.class)
    public void testReadWriteExEmptFile() throws ReadWriteEx {
        Student.readStudentFromJson("Tests\\empt.json");
    }
}
