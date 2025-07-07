package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(
        AccountService accountService,
        MessageService messageService    
    ) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public @ResponseBody Account register(@RequestBody Account account) {
        return this.accountService.registerAccount(account);
    }

    @PostMapping("/login")
    public @ResponseBody Account login(@RequestBody Account account) {
        return this.accountService.login(account);
    }

    @PostMapping("/messages")
    public @ResponseBody Message postMessage(@RequestBody Message message) {
        return this.messageService.createMessage(message);
    }

    @GetMapping("/messages")
    public @ResponseBody List<Message> getMessages() {
        return this.messageService.findAllMessages();
    }

    @GetMapping("/messages/{id}")
    public @ResponseBody ResponseEntity<Message> getMessage(@PathVariable Integer id) {
        Optional<Message> message = this.messageService.findById(id);
        if (message.isEmpty())
            return ResponseEntity.ok().build();
        return ResponseEntity.of(message);
    }

    @DeleteMapping("/messages/{id}")
    public @ResponseBody ResponseEntity<Integer> deleteMessage(@PathVariable Integer id) {
        return this.messageService.deleteById(id) ?
            ResponseEntity.ok(1) :
            ResponseEntity.ok().build();
    }

    @PatchMapping("/messages/{id}")
    public @ResponseBody ResponseEntity<Integer> patchMessage(@PathVariable Integer id, @RequestBody Message message) {
        return this.messageService.updateMessageText(id, message.getMessageText()) ?
            ResponseEntity.ok(1) :
            ResponseEntity.badRequest().build();
    }

    @GetMapping("/accounts/{id}/messages")
    public @ResponseBody List<Message> getUserMessages(@PathVariable Integer id) {
        return this.messageService.findAllUserMessages(id);
    }
}
