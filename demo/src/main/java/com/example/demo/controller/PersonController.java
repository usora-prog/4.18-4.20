package com.example.demo.controller;

import com.example.demo.dto.Message;

import com.example.demo.dto.Person;
import com.example.demo.repository.PersonRepository;
import com.example.demo.service.ServiceManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class PersonController {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private ServiceManager service;

    // Возврат списка объектов Person
    @GetMapping("/person")
    public Iterable<Person> getPersons() {
        return repository.findAll();
    }

    // Возврат объекта Person по id
    @GetMapping("/person/{id}")
    public Optional<Person> findPersonById(@PathVariable int id) {
        return repository.findById(id);
    }

    // Добавление объекта Person
    @PostMapping("/person")
    public Person addPerson(@RequestBody Person person) {
        repository.save(person);
        return person;
    }

    // Изменение объекта Person по id
    @PutMapping("/person/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable int id, @RequestBody Person person) {

        if (repository.existsById(id)) {
            person.setId(id);
            return new ResponseEntity<>(repository.save(person), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(addPerson(person), HttpStatus.CREATED);
        }
    }

    // Удаление объекта Person по id
    @DeleteMapping("/person/{id}")
    public void deletePerson(@PathVariable int id) {
        repository.deleteById(id);
    }

    // Возврат списка сообщений Message для объекта Person по p_id
    @GetMapping ("/person/{p_id}/message")
    public Iterable<Message> getMessages(@PathVariable int p_id) {
        return service.getMessagesToPerson(p_id);
    }

    //Возврат сообщения Message с m_id для объекта Person по p_id
    @GetMapping ("/person/{p_id}/message/{m_id}")
    public Message getMessage(@PathVariable int p_id, @PathVariable int m_id) {
        return service.getMessageToPerson(p_id, m_id);
    }

    // Добавление сообщения Message в объект Person с p_id
    @PostMapping("/person/{p_id}/message")
    public ResponseEntity<Person> addMessage(@PathVariable int p_id, @RequestBody Message message) {
        return service.addMessageToPerson(p_id, message);
    }

    // Удаление сообщения Message с m_id из объекта Person с p_id
    @DeleteMapping("/person/{p_id}/message/{m_id}")
    public void deleteMessage(@PathVariable int p_id, @PathVariable int m_id) {
        service.deleteMessageFromPerson(p_id, m_id);
    }
}