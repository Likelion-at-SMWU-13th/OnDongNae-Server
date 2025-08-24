package com.example.ondongnae.backend.menu.ocr;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import io.netty.channel.ChannelOption;

import java.time.Duration;

// CLOVA OCR 호출용 WebClient
@Configuration
@EnableConfigurationProperties(ClovaOcrProperties.class)
public class OcrClientConfig {

    @Bean
    public WebClient clovaOcrWebClient(ClovaOcrProperties props) {
        // Netty 클라이언트에 커넥션/응답 타임아웃 설정
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, props.connectTimeoutMs())
                .responseTimeout(Duration.ofMillis(props.readTimeoutMs()));

        // WebClient 빌더에 Netty 커넥터 적용
        return WebClient.builder()
                .baseUrl(props.invokeUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
