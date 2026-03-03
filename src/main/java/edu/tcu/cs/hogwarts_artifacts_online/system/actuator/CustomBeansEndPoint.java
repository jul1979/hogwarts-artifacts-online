package edu.tcu.cs.hogwarts_artifacts_online.system.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Endpoint(id = "custom-beans")
@Component
public class CustomBeansEndPoint {

    private final ApplicationContext applicationContext;

    public CustomBeansEndPoint(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @ReadOperation
    public int beanCount() {
        return this.applicationContext.getBeanDefinitionCount();
    }
}
