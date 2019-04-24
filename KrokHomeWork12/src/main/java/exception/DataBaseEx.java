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
public class DataBaseEx extends Exception {

    public DataBaseEx() {
        super("Ошибка при работе с базой. ");
    }

    public DataBaseEx(String message) {
        super("Ошибка при работе с базой. " + message );
    }
    
}
