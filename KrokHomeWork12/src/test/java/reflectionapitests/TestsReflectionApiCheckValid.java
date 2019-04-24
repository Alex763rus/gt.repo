/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reflectionapitests;

import exception.ClassValidationEx;
import exception.DataBaseEx;
import exception.NullObjectEx;
import exception.ReadWriteEx;
import exception.ReflectiomApiNotAccessEx;
import exception.ReflectionApiSQLEx;
import exception.StudentSQLEx;
import java.util.Arrays;
import java.util.Collection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import reflectionapi.DataBaseReflectionApi;
import student.Student;
import university.Specialty;

/**
 *
 * @author Алексей
 */
@RunWith(Parameterized.class)
public class TestsReflectionApiCheckValid {

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws ReadWriteEx {
        return Arrays.asList(new Object[][]{
            {Student.class, true},
            {Specialty.class, true},
            {String.class, false},}
        );
    }

    private static DataBaseReflectionApi reflectionDB;

    private final Class cls;
    private final boolean res;

    @Before
    public void init() throws DataBaseEx, ReflectionApiSQLEx, ClassValidationEx, ReflectiomApiNotAccessEx, NullObjectEx {
        reflectionDB = new DataBaseReflectionApi();
    }

    public TestsReflectionApiCheckValid(Class cls, boolean res) {
        this.cls = cls;
        this.res = res;
    }

    @Test
    public void testsCheckValid() throws ReadWriteEx {
        if (res) {
            Assert.assertTrue(reflectionDB.checkValidClass(cls));
        } else {
            Assert.assertFalse(reflectionDB.checkValidClass(cls));
        }
    }

    @After
    public void end() throws DataBaseEx, StudentSQLEx {
        reflectionDB.disconnect();
    }
}
