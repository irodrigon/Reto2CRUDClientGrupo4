/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.exception;

/**
 *
 * @author rabio
 */
public class AccountDoesNotExistException extends Exception {

    /**
     * Creates a new instance of <code>AccountDoesNotExistException</code>
     * without detail message.
     */
    public AccountDoesNotExistException() {
    }

    /**
     * Constructs an instance of <code>AccountDoesNotExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public AccountDoesNotExistException(String msg) {
        super(msg);
    }
}
