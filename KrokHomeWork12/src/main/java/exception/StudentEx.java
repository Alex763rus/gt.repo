/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Алексей
 */
public class StudentEx extends Exception {

    public StudentEx() {
        super("Ошибка при работе со студентами. ");
    }

    public StudentEx(String message) {
        super("Ошибка при работе со студентами. " + message);
    }
}
