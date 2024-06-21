package com.messenger.prism.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI myOpenApi() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080/prism/v1/");
        devServer.setDescription("Development Server");

        Server prodServer = new Server();
        prodServer.setUrl("https://workable-wolf-greatly.ngrok-free.app/prism/v1/");
        prodServer.setDescription("Production Server");

        Info info = new Info().title("Prism API").version("1.0").description("Prism messenger " +
                "API" + " documentation");
        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
