package com.prism.messenger.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

  @Bean
  public OpenAPI myOpenApi() {
    Server devServer = new Server();
    devServer.setUrl("http://localhost:8090/prism/v1/");
    devServer.setDescription("Development Server");

    Server prodServer = new Server();
    prodServer.setUrl("https://workable-wolf-greatly.ngrok-free.app/prism/v1/");
    prodServer.setDescription("Production Server");

    Info info = new Info().title("Prism Authentication API").version("1.0")
        .description("Prism messenger API documentation");
    return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
  }
}
