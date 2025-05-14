package edu.tcu.cs.hogwarts_artifacts_online.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CustomBearerTokenAuthenticationEntryPoint
 *
 * Cette classe implémente l'interface
 * {@link org.springframework.security.web.AuthenticationEntryPoint}
 * et est annotée avec {@code @Component} pour permettre sa gestion par le
 * conteneur Spring.
 *
 * Elle est utilisée pour gérer les échecs d'authentification par Bearer Token,
 * comme
 * un token invalide, expiré ou malformé.
 *
 * Lorsqu'une authentification Bearer Token échoue, la méthode {@code commence}
 * est appelée.
 * Celle-ci délègue la gestion de l'exception à un bean
 * {@code HandlerExceptionResolver},
 * injecté via {@code @Qualifier("handlerExceptionResolver")}, afin de résoudre
 * correctement l'erreur.
 *
 * Cette délégation permet à l'exception d'être interceptée et traitée par une
 * méthode appropriée définie dans une classe annotée avec
 * {@code @ControllerAdvice},
 * ce qui permet de renvoyer une réponse plus informative au client.
 */
@Component
public class CustomBearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public CustomBearerTokenAuthenticationEntryPoint(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        this.resolver.resolveException(request, response, null, authException);
    }
}
