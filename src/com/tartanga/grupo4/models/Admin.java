/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.models;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Iñi
 */

@XmlRootElement
public class Admin extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String password;
    
    private Boolean active;
    
    public Admin(){}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;

        hash += (logIn != null ? logIn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Admin)) {
            return false;
        }
        Admin other = (Admin) object;

        if ((this.logIn == null && other.logIn != null) || (this.logIn != null && !this.logIn.equals(other.logIn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

        return "com.tartanga.grupo4.customers.Admin[ id=" + logIn + " ]";
    }
    
}
