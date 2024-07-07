package com.example.demo.service;

import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.dto.Person;
import com.example.demo.dto.Message;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceManager {
    @Autowired
    PersonRepository repositoryPerson;

    @Autowired
    MessageRepository repositoryMessage;

    public Iterable<Message> getMessagesToPerson(int p_id) {

        Optional<Person> optionalPerson = repositoryPerson.findById(p_id);
        if(optionalPerson.isPresent()) {

            Person person = optionalPerson.get();
            List<Message> messagesAll = person.getMessages();
            List<Message> messagesRes = new ArrayList<>();

            for (Message message : messagesAll) {
                if (message.getPerson().getId() == p_id)
                    messagesRes.add(message);
            }
            return messagesRes;
        } else {
            return new ArrayList<>();
        }
    }

    public Message getMessageToPerson(int p_id, int m_id) {

        Optional<Person> optionalPerson = repositoryPerson.findById(p_id);

        if(optionalPerson.isPresent()) {

            Person person = optionalPerson.get();
            List<Message> messagesAll = person.getMessages();

            for (Message message : messagesAll) {
                if (message.getId() == m_id)
                    return message;
            }
        }

        return null;
    }

    public ResponseEntity<Person> addMessageToPerson(int p_id, Message message) {
        if (repositoryPerson.existsById(p_id)) {
            Person person = repositoryPerson.findById(p_id).get();
            message.setPerson(person);
            message.setTime(LocalDateTime.now());
            person.addMessage(message);
            return new ResponseEntity<>(repositoryPerson.save(person), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteMessageFromPerson(int p_id, int m_id) {
        Optional<Person> optionalPerson = repositoryPerson.findById(p_id);
        Optional<Message> optionalMessage = repositoryMessage.findById(m_id);

        if (optionalPerson.isPresent() && optionalMessage.isPresent()) {
            Person person = optionalPerson.get();
            Message message = optionalMessage.get();

            person.getMessages().remove(message);  // Удалить сообщение из списка сообщений пользователя
            repositoryMessage.delete(message);  // Удалить сообщение из репозитория
            repositoryPerson.save(person);  // Сохранить пользователя, чтобы обновить отношение
        } else {
            // Обработка случая, когда пользователь или сообщение не найдены
            throw new EntityNotFoundException("Пользователь или сообщение не найдены");
        }
    }

}