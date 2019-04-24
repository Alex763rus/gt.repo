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
public class DataBaseConnectEx extends DataBaseEx {

    public DataBaseConnectEx() {
        super("Ошибка при попытке установить соединение с базой. ");
    }

    public DataBaseConnectEx(String message) {
        super("Ошибка при попытке установить соединение с базой. " + message);
    }
}
