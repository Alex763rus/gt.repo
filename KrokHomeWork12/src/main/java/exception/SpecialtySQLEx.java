/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

import java.sql.SQLException;
//
/**
 *
 * @author Алексей
 */
public class SpecialtySQLEx extends SQLException {

    public SpecialtySQLEx() {
        super("Ошибка при работе со специальностями. ");
    }

    public SpecialtySQLEx(String message) {
        super("Ошибка при работе со специальностями. " + message);
    }
}
