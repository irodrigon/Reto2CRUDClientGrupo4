/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.resources.files;

import java.security.SecureRandom;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rabio
 */
public class Smtp {

    final String HOST = "localhost";
    final String TLS_PORT = "25";

    final String SENDER_USERNAME = "RovoBank@helpdesk.com";
    final String SENDER_PASSWORD = "";

    // System.getProperties devuelve las propiedades del sistema
    // NO puede devolver NULL
    Properties props = System.getProperties();

    public void sendMail(String email, String password) {

        props.setProperty(
                "mail.smtp.host", HOST);                                                          // for gmail
        props.setProperty(
                "mail.smtp.port", TLS_PORT);
        props.setProperty(
                "mail.smtp.starttls.enable", "false");
        props.setProperty(
                "mail.smtp.auth", "false");

        Session session = Session.getInstance(props, null); // null porque no hay autenticación
        //getInstance siempre devuelve un objeto

        Transport transport = null;

        try {

            // create the message
            MimeMessage msg = new MimeMessage(session);
            // set recipients and content
            msg.setFrom(new InternetAddress(SENDER_USERNAME));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
            msg.setSubject("Requested New Password");
            msg.setText("Your new password is: "+password , "utf-8", "html");
            msg.setSentDate(new Date());

            // selecciona el protocolo de correo a usar
            transport = session.getTransport("smtp");
            // En caso de que no exista proveedor de smtp lanza una excepción
            // send the mail
            transport.connect(HOST, SENDER_USERNAME, SENDER_PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());

        } catch (MessagingException e) {
            Logger.getLogger(Smtp.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if(transport != null){
                    transport.close();
                }
            } catch (MessagingException e) {
                Logger.getLogger(Smtp.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
}
