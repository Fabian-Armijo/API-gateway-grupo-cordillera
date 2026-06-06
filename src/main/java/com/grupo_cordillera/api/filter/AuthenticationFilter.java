package com.grupo_cordillera.api.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    // IMPORTANTÍSIMO: Usa exactamente la misma clave secreta que pusiste en tu 'msautenticacion'
    private final String SECRET_KEY = "MiClaveSecretaSuperSeguraQueNadiePuedeAdivinarYEsMuyLarga123456";

    public AuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {
        // Configuración si se requiere pasar parámetros desde el yml
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 1. Verificar si la cabecera Authorization viene en la petición
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Falta la cabecera de autorización", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);

            // 2. Verificar que empiece con "Bearer "
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Formato de token inválido", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            try {
                // 3. Validar el JWT usando la firma secreta
                SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token);

            } catch (Exception e) {
                // Si el token expiró, fue alterado o es falso, rebota la petición de inmediato
                return onError(exchange, "Token no válido o expirado", HttpStatus.UNAUTHORIZED);
            }

            // Si todo está bien, dejamos que la petición continúe hacia el microservicio final
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }
}