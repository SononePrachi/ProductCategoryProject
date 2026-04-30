package com.example.ProductCategory.Controller;

import com.example.ProductCategory.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ai")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/chat")
    @ResponseBody
    public String chat(@RequestBody String message)
    {
        return chatService.askAI(message);
    }
    @GetMapping("/page")
    public String openPage() {
        return "chatbot";
    }
}
