/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author grigorevap
 */
public class ReadWriteEx extends StudentEx {

    public ReadWriteEx() {
        super("Ошибка чтения/записи при работе с файлами. ");
    }

    public ReadWriteEx(String message) {
        super("Ошибка чтения/записи при работе с файлами. " + message);
    }
}
