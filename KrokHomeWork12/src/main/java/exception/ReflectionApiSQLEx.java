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
public class ReflectionApiSQLEx extends ReflectionApiEx {

    public ReflectionApiSQLEx() {
        super("SQL Ошибка при работе с Reflection API. ");
    }

    public ReflectionApiSQLEx(String message) {
        super("SQL Ошибка при работе с Reflection API. " + message);
    }
}
