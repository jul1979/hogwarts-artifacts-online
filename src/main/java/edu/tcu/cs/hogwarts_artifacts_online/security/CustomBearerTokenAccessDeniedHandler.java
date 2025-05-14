package edu.tcu.cs.hogwarts_artifacts_online.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CustomBearerTokenAccessDeniedHandler
 *
 * Cette classe implémente l'interface
 * {@link org.springframework.security.web.access.AccessDeniedHandler}
 * et est annotée avec {@code @Component} pour permettre sa gestion par le
 * conteneur Spring.
 *
 * Elle est utilisée pour gérer les problèmes d'accès refusé lors de l'utilisation
 * de jetons Bearer, comme lorsqu'un utilisateur authentifié tente d'accéder à une
 * ressource pour laquelle il n'a pas les autorisations suffisantes.
 *
 * La méthode {@code handle} est appelée lorsqu'un accès est refusé et délègue
 * la gestion de l'exception à un bean {@code HandlerExceptionResolver},
 * injecté via {@code @Qualifier("handlerExceptionResolver")}, afin de résoudre
 * correctement l'erreur.
 *
 * Cette délégation permet à l'exception d'être interceptée et traitée par une
 * méthode appropriée définie dans une classe annotée avec {@code @ControllerAdvice},
 * ce qui permet de renvoyer une réponse plus informative au client.
 */
@Component
public class CustomBearerTokenAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver resolver;

    public CustomBearerTokenAccessDeniedHandler(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        this.resolver.resolveException(request, response, null, accessDeniedException);
    }

}
