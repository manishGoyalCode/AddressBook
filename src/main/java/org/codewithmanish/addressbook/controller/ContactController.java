package org.codewithmanish.addressbook.controller;

import org.codewithmanish.addressbook.model.Contact;
import org.codewithmanish.addressbook.model.SearchRequest;
import org.codewithmanish.addressbook.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping("/create")
    public ResponseEntity<List<Contact>> createContacts(@RequestBody List<Contact> contacts) {
        return ResponseEntity.ok(contactService.createContacts(contacts));
    }

    @PutMapping("/update")
    public ResponseEntity<List<Contact>> updateContacts(@RequestBody List<Contact> contacts) {
        return ResponseEntity.ok(contactService.updateContacts(contacts));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Integer>> deleteContacts(@RequestBody List<String> ids) {
        int count = contactService.deleteContacts(ids);
        return ResponseEntity.ok(Map.of("deleted", count));
    }

    @PostMapping("/search")
    public ResponseEntity<List<Contact>> searchContacts(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(contactService.searchContacts(request.getQuery()));
    }

    @GetMapping("/suggest-duplicates")
    public ResponseEntity<List<List<Contact>>> suggestDuplicates() {
        return ResponseEntity.ok(contactService.getSuggestedDuplicates());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Contact>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }
}
