/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author rabio
 */
public class Hash {
    private static final Logger LOGGER = Logger.getLogger("javaClient");
    
    public String passwordToHash(String password) throws NoSuchAlgorithmException{
        
        
        return getSecurePassword(password);
        
    } 
    
    private static String getSecurePassword(String passwordToHash){
        String generatedPassword = null;
        
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i<bytes.length;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        }catch(NoSuchAlgorithmException error){
             LOGGER.log(Level.SEVERE, "Hash(getSecurePassword): Exception while creating Hash, {0}", error.getMessage());
        }
        return generatedPassword;
    }
    
    
    

}
