/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.RESTfull.LoanRESTFull;


/**
 * Clase singleton que provee acceso a los servicios relacionados con los préstamos.
 * Este patrón asegura que solo haya una instancia de LoanFactory en toda la aplicación.
 */
public class LoanFactory {
    private static LoanFactory instance;

    /**
     * Constructor privado para evitar la instanciación externa, aplicando el patrón Singleton.
     */
    private LoanFactory() {
    }

    /**
     * Obtiene la instancia única de LoanFactory. Si no existe, la crea.
     * 
     * @return la instancia única de LoanFactory.
     */
    public static synchronized LoanFactory getInstance() {
        if (instance == null) {
            instance = new LoanFactory();
        }
        return instance;
    }

    /**
     * Proporciona una instancia de ILoans, actualmente implementado por LoanRESTFull.
     * 
     * @return una nueva instancia de LoanRESTFull que implementa ILoans.
     */
    public ILoan getILoans() {
        return new LoanRESTFull();
    }

    
}
