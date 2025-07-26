package org.codewithmanish.addressbook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codewithmanish.addressbook.model.Contact;
import org.codewithmanish.addressbook.model.SearchRequest;
import org.codewithmanish.addressbook.repository.ContactRepository;
import org.codewithmanish.addressbook.service.ContactService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ContactController.class)
@Import(ContactControllerTest.MockConfig.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ContactService contactService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ContactService contactService() {
            return Mockito.mock(ContactService.class);
        }


        @Bean
        public ContactRepository contactRepository() {
            return Mockito.mock(ContactRepository.class);
        }
    }


    private final String sampleId1 = UUID.randomUUID().toString();
    private final String sampleId2 = UUID.randomUUID().toString();

    @Test
    void testCreateContacts() throws Exception {
        List<Contact> input = List.of(
                new Contact(null, "Alice Smith", "1234567890", "alice@example.com"),
                new Contact(null, "Bob Jones", "2345678901", "bob@example.com")
        );

        List<Contact> response = List.of(
                new Contact(sampleId1, "Alice Smith", "1234567890", "alice@example.com"),
                new Contact(sampleId2, "Bob Jones", "2345678901", "bob@example.com")
        );

        when(contactService.createContacts(input)).thenReturn(response);

        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(sampleId1))
                .andExpect(jsonPath("$[0].name").value("Alice Smith"))
                .andExpect(jsonPath("$[0].phone").value("1234567890"))
                .andExpect(jsonPath("$[0].email").value("alice@example.com"));
    }

    @Test
    void testUpdateContacts() throws Exception {
        List<Contact> input = List.of(
                new Contact(sampleId1, null, "9999999999", null)
        );

        List<Contact> response = List.of(
                new Contact(sampleId1, "Alice Smith", "9999999999", "alice@example.com")
        );

        when(contactService.updateContacts(input)).thenReturn(response);

        mockMvc.perform(put("/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(sampleId1))
                .andExpect(jsonPath("$[0].name").value("Alice Smith"))
                .andExpect(jsonPath("$[0].phone").value("9999999999"))
                .andExpect(jsonPath("$[0].email").value("alice@example.com"));
    }

    @Test
    void testDeleteContacts() throws Exception {
        List<String> ids = List.of(sampleId1, sampleId2);

        when(contactService.deleteContacts(ids)).thenReturn(2);

        mockMvc.perform(delete("/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted").value(2));
    }

    @Test
    void testSearchContacts() throws Exception {
        SearchRequest search = new SearchRequest("Smith");

        List<Contact> results = List.of(
                new Contact(sampleId1, "Alice Smith", "1234567890", "alice@example.com"),
                new Contact(sampleId2, "Charlie Smith", "3456789012", "charlie@example.com")
        );

        when(contactService.searchContacts("Smith")).thenReturn(results);

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(search)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice Smith"))
                .andExpect(jsonPath("$[1].name").value("Charlie Smith"));
    }
}