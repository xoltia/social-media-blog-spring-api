package com.example.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.ValidationException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(
        MessageRepository messageRepository,
        AccountRepository accountRepository
    ) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Message createMessage(Message message) throws ValidationException  {
        if (message.getMessageText().length() == 0)
            throw new ValidationException("Message text must not be empty.");
        if (message.getMessageText().length() > 255)
            throw new ValidationException("Message text must not be longer than 255 characters.");
        if (!this.accountRepository.existsById(message.getPostedBy()))
            throw new ValidationException("Invalid user ID.");
        return this.messageRepository.save(message);
    }

    public List<Message> findAllMessages() {
        return this.messageRepository.findAll();
    }

    public List<Message> findAllUserMessages(Integer userId) {
        return this.messageRepository.findAllByPostedBy(userId);
    }

    public Optional<Message> findById(Integer userId) {
        return this.messageRepository.findById(userId);
    }

    @Transactional
    public boolean deleteById(Integer id) {
        if (!this.messageRepository.existsById(id))
            return false;
        this.messageRepository.deleteById(id);
        return true;
    }

    @Transactional
    public boolean updateMessageText(Integer id, String text) throws ValidationException {
        if (text.length() == 0)
            throw new ValidationException("Message text must not be empty.");
        if (text.length() > 255)
            throw new ValidationException("Message text must not be longer than 255 characters.");
        Optional<Message> maybeMessage = this.messageRepository.findById(id);
        if (maybeMessage.isEmpty())
            return false;
        Message message = maybeMessage.get();
        message.setMessageText(text);
        this.messageRepository.save(message);
        return true;
    }
}
