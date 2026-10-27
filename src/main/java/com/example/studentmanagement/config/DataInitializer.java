package com.example.studentmanagement.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.studentmanagement.entity.AppUser;
import com.example.studentmanagement.repository.AppUserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner createAdmin(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!appUserRepository.existsByUsername("admin")) {

                AppUser admin = new AppUser();

                admin.setUsername("admin");
                admin.setPassword(
                    passwordEncoder.encode("Admin@123")
                );
                admin.setRole("ROLE_ADMIN");

                appUserRepository.save(admin);

                System.out.println(
                    "Administrator account created successfully."
                );

            } else {
                System.out.println(
                    "Administrator account already exists."
                );
            }
        };
    }
    @Bean
    public CommandLineRunner createRegularUser(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!appUserRepository.existsByUsername("teacher1")) {

                AppUser user = new AppUser();

                user.setUsername("teacher1");
                user.setPassword(
                    passwordEncoder.encode("Teacher@123")
                );
                user.setRole("ROLE_USER");

                appUserRepository.save(user);

                System.out.println(
                    "Regular user account created successfully."
                );

            } else {
                System.out.println(
                    "Regular user account already exists."
                );
            }
        };
    }
}