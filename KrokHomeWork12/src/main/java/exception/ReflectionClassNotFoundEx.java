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
public class ReflectionClassNotFoundEx extends ReflectionApiEx {

    public ReflectionClassNotFoundEx() {
        super("Ошибка! Такой класс не найден. ");
    }

    public ReflectionClassNotFoundEx(String message) {
        super("шибка! Такой класс не найден. " + message);
    }
}
