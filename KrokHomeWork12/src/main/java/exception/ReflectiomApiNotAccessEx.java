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
public class ReflectiomApiNotAccessEx extends ReflectionApiEx {

    public ReflectiomApiNotAccessEx() {
        super("Ошибка! Отсутствует доступ. ");
    }

    public ReflectiomApiNotAccessEx(String message) {
        super("Ошибка! Отсутствует доступ. " + message);
    }
}
