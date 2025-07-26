package org.codewithmanish.addressbook.service;

import org.codewithmanish.addressbook.model.Contact;
import org.codewithmanish.addressbook.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactService {

    @Autowired
    private ContactRepository repository;

    public List<Contact> createContacts(List<Contact> contacts) {
        for (Contact contact : contacts) {
            contact.setId(UUID.randomUUID().toString());
        }
        return repository.createContacts(contacts);
    }

    public List<Contact> updateContacts(List<Contact> contacts) {
        return repository.updateContacts(contacts);
    }

    public int deleteContacts(List<String> ids) {
        return repository.deleteContacts(ids);
    }

    public List<Contact> searchContacts(String query) {
        return repository.search(query);
    }

    public List<List<Contact>> getSuggestedDuplicates() {
        return repository.findExactDuplicateGroups();
    }

    public List<Contact> getAllContacts() {
        return repository.getAllContacts();
    }
}
