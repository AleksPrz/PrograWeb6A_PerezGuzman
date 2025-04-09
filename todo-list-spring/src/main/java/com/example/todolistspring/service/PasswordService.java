package com.example.todolistspring.service;
import javax.crypto.SecretKey;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String inputPassword, String storedPassword) {
        return BCrypt.checkpw(inputPassword, storedPassword);
    }

}
