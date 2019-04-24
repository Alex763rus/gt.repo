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
public class DataBaseDisconnectEx extends DataBaseEx {

    public DataBaseDisconnectEx() {
        super("Ошибка при попытке завершить соединение с базой. ");
    }

    public DataBaseDisconnectEx(String message) {
        super("Ошибка при попытке завершить соединение с базой. " + message);
    }

}
