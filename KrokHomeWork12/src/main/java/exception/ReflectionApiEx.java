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
public class ReflectionApiEx extends Exception {

    public ReflectionApiEx() {
        super("Ошибка при работе с Reflection API. ");
    }

    public ReflectionApiEx(String message) {
        super("Ошибка при работе с Reflection API. " + message);
    }
}
