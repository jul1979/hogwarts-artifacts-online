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
 * CustomBasicAuthenticationEntryPoint
 *
 * Cette classe implémente l'interface
 * {@link org.springframework.security.web.AuthenticationEntryPoint}
 * et est annotée avec {@code @Component} pour permettre sa gestion par le
 * conteneur Spring.
 *
 * Elle est utilisée pour gérer les échecs d'authentification HTTP Basic, comme
 * un nom d'utilisateur
 * ou un mot de passe incorrect.
 *
 * Lorsqu'une authentification Basic échoue, la méthode {@code commence} est
 * appelée.
 * Celle-ci :
 * <ul>
 * <li>Ajoute un en-tête HTTP {@code WWW-Authenticate} à la réponse.</li>
 * <li>Délègue ensuite la gestion de l'exception à un bean
 * {@code HandlerExceptionResolver},
 * injecté via {@code @Qualifier("handlerExceptionResolver")}, afin de résoudre
 * correctement l'erreur.</li>
 * </ul>
 *
 * Cette délégation permet à l'exception d'être interceptée et traitée par une
 * méthode appropriée
 * définie dans une classe annotée avec {@code @ControllerAdvice} (par exemple,
 * {@code ExceptionHandlerAdvice}),
 * ce qui permet de renvoyer une réponse plus informative au client.
 *
 * Cette classe est généralement enregistrée dans la configuration de sécurité
 * Spring pour remplacer
 * le comportement par défaut de
 * {@code httpBasic().authenticationEntryPoint(...)}, afin de fournir une
 * réponse structurée même en cas d’échec d’authentification Basic.
 */

@Component
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public CustomBasicAuthenticationEntryPoint(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        response.addHeader("WWW-Authenticate", "Basic realm=\"Realm\"");
        this.resolver.resolveException(request, response, null, authException);
    }

}
