/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.models.Admin;

/**
 *
 * @author rabio
 */
public class AdminManager {
    private static AdminManager instance;
    private Admin admin;
    
    private AdminManager(){
        
    }
    
    public static AdminManager getInstance(){
        if(instance == null){
            instance = new AdminManager();
        }
        return instance;
    }
    
    public void setAdmin(Admin admin){
        this.admin = admin;
    }
    public Admin getAdmin(){
        return admin;
    }
    
}
