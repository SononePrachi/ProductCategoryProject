package com.example.ProductCategory.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatService {

    @Value("${google.api.key}")
    private String apiKey;

    public String askAI(String msg) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = """
            {
              "contents":[
                {
                  "parts":[
                    {"text":"%s"}
                  ]
                }
              ]
            }
            """.formatted(msg);

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            String url =
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
                            + apiKey;

            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey,
                            entity,
                            String.class
                    );

            return response.getBody();

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}