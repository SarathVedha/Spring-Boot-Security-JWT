package com.vedha.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedha.filter.JwtAuthenticationFilter;
import com.vedha.repository.UsersRepository;
import com.vedha.util.AppSecurityWhiteList;
import com.vedha.util.Permissions;
import com.vedha.util.Roles;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthenticationFilter jwtAuthenticationFilter,
                                            AuthenticationProvider authenticationProvider, AuthenticationManager authenticationManager,
                                            ObjectMapper objectMapper) throws Exception {

        /*
         * H2 Console uses frames and, as it is intended for development, only, does not implement CSRF protection measures.
         * If your application uses Spring Security, you need to configure it to
         * Disable CSRF protection for requests against the console.
         * Set the header X-Frame-Options to the SAME ORIGIN on responses from the console.
         */
        httpSecurity.csrf(CsrfConfigurer::disable); // For H2 Console and JWT Token

        httpSecurity.headers((headers) -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) // For H2 Console
        );

        httpSecurity.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers(PathRequest.toH2Console()).permitAll() // For H2 Console
                        .requestMatchers(AppSecurityWhiteList.AUTH_WHITELIST).permitAll() // For Allowlisted URLs
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll() // For Authentication

//                .requestMatchers("/api/v1/admin/**").hasRole(Roles.ADMIN.name())
//                .requestMatchers(HttpMethod.GET, "/api/v1/admin/**").hasAuthority(Permissions.ADMIN_READ.getPermissionValue())
//                .requestMatchers(HttpMethod.POST, "/api/v1/admin/**").hasAuthority(Permissions.ADMIN_WRITE.getPermissionValue())
//                .requestMatchers(HttpMethod.PUT, "/api/v1/admin/**").hasAuthority(Permissions.ADMIN_WRITE.getPermissionValue())
//                .requestMatchers(HttpMethod.DELETE, "/api/v1/admin/**").hasAuthority(Permissions.ADMIN_WRITE.getPermissionValue())

                        .requestMatchers("/api/v1/manager/**").hasAnyRole(Roles.ADMIN.name(), Roles.MANAGER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/manager/**").hasAnyAuthority(Permissions.ADMIN_READ.getPermissionValue(), Permissions.MANAGER_READ.getPermissionValue())
                        .requestMatchers(HttpMethod.POST, "/api/v1/manager/**").hasAnyAuthority(Permissions.ADMIN_WRITE.getPermissionValue(), Permissions.MANAGER_WRITE.getPermissionValue())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/manager/**").hasAnyAuthority(Permissions.ADMIN_WRITE.getPermissionValue(), Permissions.MANAGER_WRITE.getPermissionValue())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/manager/**").hasAnyAuthority(Permissions.ADMIN_WRITE.getPermissionValue(), Permissions.MANAGER_WRITE.getPermissionValue())

                        .requestMatchers("/api/v1/user/**").hasAnyRole(Roles.ADMIN.name(), Roles.MANAGER.name(), Roles.USER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/user/**").hasAnyAuthority(Permissions.ADMIN_READ.getPermissionValue(), Permissions.MANAGER_READ.getPermissionValue(), Permissions.USER_READ.getPermissionValue())
                        .requestMatchers(HttpMethod.POST, "/api/v1/user/**").hasAnyAuthority(Permissions.ADMIN_WRITE.getPermissionValue(), Permissions.MANAGER_WRITE.getPermissionValue(), Permissions.USER_WRITE.getPermissionValue())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/user/**").hasAnyAuthority(Permissions.ADMIN_WRITE.getPermissionValue(), Permissions.MANAGER_WRITE.getPermissionValue(), Permissions.USER_WRITE.getPermissionValue())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/user/**").hasAnyAuthority(Permissions.ADMIN_WRITE.getPermissionValue(), Permissions.MANAGER_WRITE.getPermissionValue(), Permissions.USER_WRITE.getPermissionValue())

                        .anyRequest().authenticated() // For All Other Requests
        );

        // Custom Access Denied Handler
        httpSecurity.exceptionHandling((exceptionHandling) -> exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) ->
                {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value()), accessDeniedException.getMessage());
                    problemDetail.setProperty("description", "You are not authorized to access this resource");

                    response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
                })
//                .accessDeniedPage("/access-denied") // For Access Denied Page
        );

        httpSecurity.sessionManagement((sessionManagement) -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // For JWT Token Authentication (No Session) - Stateless
        );

        httpSecurity.authenticationProvider(authenticationProvider);

        httpSecurity.authenticationManager(authenticationManager);

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return authenticationProvider;
    }

    @Bean
    UserDetailsService userDetailsService(UsersRepository usersRepository) {

        return username -> usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found: ".concat(username)));
    }

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
