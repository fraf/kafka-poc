package fr.poc.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactivé pour les API REST stateless
                .csrf(AbstractHttpConfigurer::disable)

                // Définition des règles d'accès :
                .authorizeHttpRequests(auth -> auth
                        // N'importe quelle requête nécessite simplement d'être authentifié
                        .anyRequest().authenticated()
                )

                // Active l'authentification HTTP Basic (utile pour tester avec Postman, RestClient ou curl)
                // OBLIGATOIRE pour que BasicAuthenticationInterceptor fonctionne :
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails testUser = User.builder()
                .username("test-user")
                .password(passwordEncoder.encode("password")) // Doit correspondre au mot de passe du test
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(testUser);
    }
}