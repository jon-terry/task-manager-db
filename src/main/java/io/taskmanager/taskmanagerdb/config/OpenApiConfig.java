package io.taskmanager.taskmanagerdb.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .description("Backend API for managing tasks, analytics, search, and more.")
                        .version("1.0.0"));

    }


}
