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
public class ClassValidationEx extends ReflectionApiEx{

    public ClassValidationEx() {
        super("Ошибка! В классе отсутствуют необходимые аннотации. ");
    }

    public ClassValidationEx(String message) {
        super("шибка! В классе отсутствуют необходимые аннотации. " + message);
    }
}
