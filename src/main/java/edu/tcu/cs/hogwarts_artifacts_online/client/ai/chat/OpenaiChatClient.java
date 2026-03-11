package edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto.ChatRequest;
import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto.ChatResponse;

@Component
public class OpenaiChatClient implements ChatClient {

    private final RestClient restClient;

    public OpenaiChatClient(RestClient.Builder restclientBuilder,
            @Value("${spring.ai.openai.base-url}") String endpoint,
            @Value("${spring.ai.openai.api-key}") String apikey) {

        this.restClient = restclientBuilder
                .baseUrl(endpoint)
                .defaultHeader("Authorization", "Bearer " + apikey)
                .build();
    }

    @Override
    public ChatResponse generate(ChatRequest ChatRequest) {
        return this.restClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ChatRequest)
                .retrieve()
                .body(ChatResponse.class);
    }

}
