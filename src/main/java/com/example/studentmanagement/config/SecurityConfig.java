package com.example.studentmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.studentmanagement.security.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

    	DaoAuthenticationProvider provider =
    	        new DaoAuthenticationProvider(userDetailsService);

    	provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider authenticationProvider)
            throws Exception {

        http
            .authenticationProvider(authenticationProvider)

            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers(
            	        "/login",
            	        "/css/**",
            	        "/js/**",
            	        "/images/**"
            	    ).permitAll()

            	    // Only administrators can add, edit, save, or delete students
            	    .requestMatchers(
            	        "/students/new",
            	        "/students/save",
            	        "/students/edit/**",
            	        "/students/delete/**"
            	    ).hasRole("ADMIN")

            	    // All authenticated users can view the dashboard and student list
            	    .requestMatchers("/", "/students").hasAnyRole("ADMIN", "USER")

            	    // Protect every other URL by default
            	    .anyRequest().authenticated()
            	)

            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}