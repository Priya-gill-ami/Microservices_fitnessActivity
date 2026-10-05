//package com.fitness.gateway;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/actuator/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                // JWT authentication
//                .oauth2ResourceServer(oauth2 ->
//                        oauth2.jwt(Customizer.withDefaults())
//                );
//
//        return http.build();
//    }
//}

package com.fitness.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .authorizeExchange(exchange -> exchange
//                        .pathMatchers("/actuator/**").permitAll()
                        .anyExchange().authenticated()
                );

//                // JWT authentication
//                .oauth2ResourceServer(oauth2 ->
//                        oauth2.jwt(Customizer.withDefaults())
//                );

        return http.build();
    }
}