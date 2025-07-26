package org.codewithmanish.addressbook.repository;

import org.codewithmanish.addressbook.model.Contact;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class ContactRepository {
    private final Map<String, Contact> contactsById = new ConcurrentHashMap<>();
    private final Map<String, Set<Contact>> nameIndex = new ConcurrentHashMap<>();

    public List<Contact> createContacts(List<Contact> contacts) {
        for (Contact contact : contacts) {
            contactsById.put(contact.getId(), contact);
            indexContact(contact);
        }
        return contacts;
    }

    public List<Contact> updateContacts(List<Contact> updates) {
        for (Contact update : updates) {
            Contact existing = contactsById.get(update.getId());
            if (existing != null) {
                unindexContact(existing);
                if (update.getName() != null) existing.setName(update.getName());
                if (update.getPhone() != null) existing.setPhone(update.getPhone());
                if (update.getEmail() != null) existing.setEmail(update.getEmail());
                indexContact(existing);
            }
        }
        return updates.stream().map(update -> contactsById.get(update.getId())).collect(Collectors.toList());
    }

    public List<Contact> search(String query) {
        Set<Contact> result = nameIndex.getOrDefault(query.toLowerCase(), Collections.emptySet());
        return new ArrayList<>(result);
    }

    public int deleteContacts(List<String> ids) {
        int count = 0;
        for (String id : ids) {
            Contact removed = contactsById.remove(id);
            if (removed != null) {
                unindexContact(removed);
                count++;
            }
        }
        return count;
    }

    private void indexContact(Contact contact) {
        for (String token : contact.getName().toLowerCase().split("\\s+")) {
            nameIndex.computeIfAbsent(token, k -> ConcurrentHashMap.newKeySet()).add(contact);
        }
    }

    private void unindexContact(Contact contact) {
        for (String token : contact.getName().toLowerCase().split("\\s+")) {
            Set<Contact> set = nameIndex.get(token);
            if (set != null) {
                set.remove(contact);
                if (set.isEmpty()) nameIndex.remove(token);
            }
        }
    }

    public List<List<Contact>> findExactDuplicateGroups() {
        Map<String, List<Contact>> groupMap = new HashMap<>();

        for (Contact contact : contactsById.values()) {
            String key = (contact.getName() + "|" + contact.getPhone() + "|" + contact.getEmail()).toLowerCase();
            groupMap.computeIfAbsent(key, k -> new ArrayList<>()).add(contact);
        }

        return groupMap.values().stream()
                .filter(list -> list.size() > 1)
                .toList();
    }

    public List<Contact> getAllContacts() {
        return new ArrayList<>(contactsById.values());
    }
}
