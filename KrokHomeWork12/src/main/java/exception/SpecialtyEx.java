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
public class SpecialtyEx extends Exception {

    public SpecialtyEx() {
        super("Ошибка при работе со специальностями. ");
    }

    public SpecialtyEx(String message) {
        super("Ошибка при работе со специальностями. " + message);
    }
}
