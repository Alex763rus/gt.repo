/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studenttests;

import exception.ReadWriteEx;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import student.Student;

/**
 *
 * @author Алексей
 */
@RunWith(Parameterized.class)
public class TestsStudentsData {

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws ReadWriteEx {
        return Arrays.asList(new Object[][]{
            {new Student(1, "sudentname1", 4.5f, 1), "src\\main\\resources\\Tests\\studData1.json"},
            {new Student(2, "sudentname2", 3.5f, 2), "src\\main\\resources\\Tests\\studData2.json"}
        }
        );
    }
    private final Student student;
    private final String path;

    public TestsStudentsData(Student student, String path) {
        this.student = student;
        this.path = path;
    }

    @Test
    public void testStudentsEquals() throws ReadWriteEx {
        Assert.assertTrue(Student.readStudentToJson(path).get(0).equals(student));
    }

    @Test
    public void testStudentsDataName() throws ReadWriteEx {
        Assert.assertTrue(Student.readStudentToJson(path).get(0).getName().equals(student.getName()));
    }

    @Test
    public void testStudentsDataAverageScore() throws ReadWriteEx {
        Assert.assertTrue(Student.readStudentToJson(path).get(0).getAverageScore() == student.getAverageScore());
    }

    @Test
    public void testStudentsDataSpecialtyId() throws ReadWriteEx {
        Assert.assertTrue(Student.readStudentToJson(path).get(0).getSpecialtyId() == student.getSpecialtyId());
    }
}
