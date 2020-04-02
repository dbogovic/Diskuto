/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.models;

import java.util.Random;
import org.diskuto.helpers.Database;

/**
 *
 * @author dario
 */
public class User {

    private String email;
    private String username;
    private String password;
    private int confirmCode;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmCode = new Random().nextInt(899999) + 100000;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(int confirmCode) {
        this.confirmCode = confirmCode;
    }

    public void register() throws Exception {
        
        /* https://projects.eclipse.org/projects/ee4j.glassfish/downloads
            String from = "no-reply@diskuto.com";
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.starttls.enable", "true");
            Session session = Session.getInstance(properties);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Potvrdite registraciju");
            message.setText("Kod za registraciju je: " + confirmCode);
            Transport.send(message);
         */
        
        Database db = new Database();
        db.xquery("update insert <user><email>"+email+"</email><name>"+username+
                "</name><password>"+password+"</password><code>"+confirmCode+
                "</code></user> into /users");
        db.close();
    }
}
