package com.example.ProductCategory.Controller;

import com.example.ProductCategory.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ai")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/chat")
    public String chat(@RequestParam("message") String message,
                       Model model) {

        String response = chatService.askAI(message);

        model.addAttribute("userMessage", message);
        model.addAttribute("aiResponse", response);

        return "chatbot";
    }
}
