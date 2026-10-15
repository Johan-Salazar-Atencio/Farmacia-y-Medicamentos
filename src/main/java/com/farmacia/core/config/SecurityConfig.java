
package com.farmacia.core.config;

import com.farmacia.core.security.ManejadorAccesoDenegado;
import com.farmacia.core.security.ManejadorExitoLogin;
import com.farmacia.core.security.ManejadorFalloLogin;
import com.farmacia.core.security.ManejadorLogout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ManejadorExitoLogin manejadorExitoLogin;
    private final ManejadorFalloLogin manejadorFalloLogin;
    private final ManejadorLogout manejadorLogout;
    private final ManejadorAccesoDenegado manejadorAccesoDenegado;

    public SecurityConfig(
            ManejadorExitoLogin manejadorExitoLogin,
            ManejadorFalloLogin manejadorFalloLogin,
            ManejadorLogout manejadorLogout,
            ManejadorAccesoDenegado manejadorAccesoDenegado) {
        this.manejadorExitoLogin = manejadorExitoLogin;
        this.manejadorFalloLogin = manejadorFalloLogin;
        this.manejadorLogout = manejadorLogout;
        this.manejadorAccesoDenegado = manejadorAccesoDenegado;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/error",
                                "/favicon.ico"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(manejadorExitoLogin)
                        .failureHandler(manejadorFalloLogin)
                        .permitAll()
                )
                .logout(logout -> logout
                        .addLogoutHandler(manejadorLogout)
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(manejadorAccesoDenegado)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
