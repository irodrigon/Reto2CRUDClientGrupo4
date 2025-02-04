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
public class DeleteException extends Exception {

    /**
     * Creates a new instance of <code>DeleteException</code> without detail
     * message.
     */
    public DeleteException() {
        super("Error al borrar");
    }

    /**
     * Constructs an instance of <code>DeleteException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public DeleteException(String msg) {
        super(msg);
    }
}
