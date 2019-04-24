/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

import java.sql.SQLException;

/**
 *
 * @author grigorevap
 */
public class StudentSQLEx extends SQLException {

    /**
     *
     */
    public StudentSQLEx() {
        super("Ошибка при работе со студентами. ");
    }

    public StudentSQLEx(String message) {
        super("Ошибка при работе со студентами. " + message);
    }
}
