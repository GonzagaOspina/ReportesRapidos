package co.edu.uniquindio.proyecto.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.List;


@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        System.out.println("🛡️ Entrando a JWTFilter");

        String token = getToken(request);
        System.out.println("📦 Token recibido: " + token);

        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Jws<Claims> payload = jwtUtil.parseJwt(token);
            String username = payload.getBody().getSubject();
            String role = payload.getBody().get("rol", String.class);
            String prefixedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;

            System.out.println("🎯 Rol detectado: " + prefixedRole);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = new User(
                        username,
                        "",
                        List.of(new SimpleGrantedAuthority(prefixedRole))
                );

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("✅ Contexto de seguridad configurado");
            }

        } catch (Exception e) {
            System.out.println("❌ Error de autenticación: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        chain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        return header != null && header.startsWith("Bearer ") ? header.replace("Bearer ", "") : null;
    }
}
