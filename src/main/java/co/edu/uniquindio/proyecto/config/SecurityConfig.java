package co.edu.uniquindio.proyecto.config;

import co.edu.uniquindio.proyecto.seguridad.AutenticacionEntryPoint;
import co.edu.uniquindio.proyecto.seguridad.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JWTFilter jwtFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configura la seguridad HTTP para la aplicación
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/login/**", "/usuarios/activar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios", "/usuarios/registro","/usuarios/activar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios/registro").hasAuthority("ROLE_CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/reportes/*").hasAnyAuthority("ROLE_CLIENTE","ROLE_MODERADOR")
                        .requestMatchers(HttpMethod.GET, "/moderador/**").hasAnyAuthority( "ROLE_MODERADOR")
                        .requestMatchers(HttpMethod.GET, "/usuarios/perfil").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categorias/listarCategorias").permitAll()
                        .requestMatchers(HttpMethod.POST, "/reportes/crearReporte").hasAnyAuthority("ROLE_CLIENTE","ROLE_MODERADOR")
                        .requestMatchers(HttpMethod.POST, "/moderador/categorias").hasAuthority("ROLE_MODERADOR")
                        .requestMatchers(HttpMethod.POST, "/api/reportes/*/estado").hasAuthority("ROLE_MODERADOR")
                        .requestMatchers(HttpMethod.GET, "/moderador/categorias/*").hasAuthority("ROLE_MODERADOR")
                        .requestMatchers(HttpMethod.GET,"/reportes/mis-reportes").hasAnyAuthority("ROLE_CLIENTE", "ROLE_MODERADOR")
                        .requestMatchers(HttpMethod.DELETE, "/reportes/*").permitAll()
                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex.authenticationEntryPoint( new AutenticacionEntryPoint() ))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).headers(headers -> headers.xssProtection(xss -> xss.disable()));


        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Configura las políticas de CORS para permitir solicitudes desde el frontend
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200", "https://reportes-6b43c.web.app"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        // Permite codificar y verificar contraseñas utilizando BCrypt
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Proporciona un AuthenticationManager para la autenticación de usuarios
        return configuration.getAuthenticationManager();
    }
}
