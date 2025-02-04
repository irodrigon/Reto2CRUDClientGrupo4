/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.exception;

/**
 *
 * @author 2dami
 */
public class CreateException extends Exception {

    /**
     * Creates a new instance of <code>createException</code> without detail
     * message.
     */
    public CreateException() {
        super("Error al crear");
    }

    /**
     * Constructs an instance of <code>createException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public CreateException(String msg) {
        super(msg);
    }
}
