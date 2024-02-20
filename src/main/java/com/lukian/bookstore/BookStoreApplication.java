package com.lukian.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BookStoreApplication {

    public static void main(String[] args) {
        String password = "PasswOrd123@";

        // Create an instance of BCryptPasswordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Hash the password
        String hashedPassword = passwordEncoder.encode(password);

        // Print the hashed password
        System.out.println("Original Password: " + password);
        System.out.println("Hashed Password: " + hashedPassword);
        SpringApplication.run(BookStoreApplication.class, args);
    }
}
