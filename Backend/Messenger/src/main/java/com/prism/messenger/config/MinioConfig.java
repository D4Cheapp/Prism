package com.prism.messenger.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

  @Value("${spring.minio.url}")
  private String url;
  @Value("${minio.user}")
  private String accessKey;
  @Value("${minio.password}")
  private String secretKey;

  @Bean
  public MinioClient minioClient() {
    return new MinioClient.Builder()
        .credentials(accessKey, secretKey)
        .endpoint(url)
        .build();
  }
}
