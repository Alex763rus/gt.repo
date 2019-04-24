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
public class NullObjectEx extends ReflectionApiEx{

    public NullObjectEx() {
        super("Ошибка. Пустой NULL объект. ");
    }

    public NullObjectEx(String message) {
        super("Ошибка. Пустой NULL объект. " + message);
    }
}
