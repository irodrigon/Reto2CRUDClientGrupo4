/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.models;

/**
 *
 * @author Iñi
 */
public enum Currency {

    EURO(1.0),
    DOLLAR(1.05),
    YEN(160.0),
    SWISSFRANC(0.95),
    POUND(0.85),
    LEI(5.0);
    
    private final double exchangeRate;

    Currency(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }
}
