package com.library;

import com.library.model.User;
import com.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {

            // 1. Afișare beaturi Spring Boot (poți păstra sau comenta)
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

            // 2. Adăugare utilizator admin dacă nu există
            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = new User();
                user.setUsername("admin");
                user.setPassword(encoder.encode("admin123"));
                userRepository.save(user);
                System.out.println("User admin a fost creat cu succes!");
            } else {
                System.out.println("Userul admin există deja.");
            }
        };
    }
}
