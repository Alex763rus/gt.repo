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
public class SpecialtyNotFoundEx extends SpecialtyEx {

    public SpecialtyNotFoundEx() {
        super("Специальность с указанным id не найдена.");
    }
}
