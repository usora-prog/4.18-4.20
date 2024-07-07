package com.example.demo.controller;

import com.example.demo.dto.Message;
import com.example.demo.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class MessageController {

    @Autowired
    private MessageRepository repository;

    // Возврат списка объектов Message
    @GetMapping("/message")
    public Iterable<Message> getMessage() {
        return repository.findAll();
    }

    // Возврат объекта Message по id
    @GetMapping("/message/{id}")
    public Optional<Message> findMessageById(@PathVariable int id) {
        return repository.findById(id);
    }

    // Добавление объекта Message
    @PostMapping("/message")
    public Message addMessage(@RequestBody Message message) {
        repository.save(message);
        return message;
    }

    // Изменение объекта Message по id
    @PutMapping("/message/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable int id, @RequestBody Message message) {

        if (repository.existsById(id)) {
            message.setId(id);
            return new ResponseEntity<>(repository.save(message), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(addMessage(message), HttpStatus.CREATED);
        }
    }

    // Удаление объекта Message по id
    @DeleteMapping("/message/{id}")
    public void deleteMessage(@PathVariable int id) {
        repository.deleteById(id);
    }


}