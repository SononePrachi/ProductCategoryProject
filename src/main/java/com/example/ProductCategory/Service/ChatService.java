package com.example.ProductCategory.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatService {

    @Value("${openai.api.key}")
    private String apiKey;

    public String askAI(String msg) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = """
        {
          "model":"gpt-4o-mini",
          "messages":[
            {"role":"user","content":"%s"}
          ]
        }
        """.formatted(msg);

        HttpEntity<String> entity =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        "https://api.openai.com/v1/chat/completions",
                        entity,
                        String.class
                );

        return response.getBody();
    }
}
